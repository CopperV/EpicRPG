package me.Vark123.EpicRPG.FightSystem;

import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.FightSystem.Events.MagicEntityDamageByEntityEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.RuneEffect;
import me.Vark123.EpicRPG.Utils.Pair;

public final class RuneDamage {

	private RuneDamage() { }
	
	public static boolean directDamageEffect(Player p, LivingEntity e, Optional<Double> oDamage, Optional<RuneEffect> oEffect) {
		if(e == null || e.isDead())
			return false;
		if(e instanceof Player) {
			Player tmp = (Player) e;	
			if(tmp.getGameMode().equals(GameMode.SPECTATOR) 
					|| tmp.getGameMode().equals(GameMode.CREATIVE)) 
				return false;
		}
		
		MutableBoolean result = new MutableBoolean(true);
		oDamage.ifPresent(dmg -> {
			EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, e, DamageCause.CUSTOM, dmg);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled()) {
				result.setFalse();
				return;
			}

			result.setValue(ManualDamage
					.doDamageWithCheck(p, e, event.getDamage(), event));
		});
		
		if(result.isTrue()) {
			oEffect.ifPresent(effect -> {
				effect.playEffect(p, e, null);
			});
		}
		
		return result.booleanValue();
	}
	
	public static boolean damageNormal(Player p, LivingEntity e, ItemStackRune dr) {
		return damageNormal(p, e, dr, dr.getDamage());
	}
	
	public static boolean damageNormal(Player p, LivingEntity e, ItemStackRune dr, double dmg) {
		return damageNormal(p, e, dr, dmg, null);
	}
	
	public static boolean damageNormal(Player p, LivingEntity e, ItemStackRune dr, RuneEffect effect) {
		return damageNormal(p, e, dr, dr.getDamage(), effect);
	}
	
	public static boolean damageNormal(Player p, LivingEntity e, ItemStackRune dr, double dmg, RuneEffect effect) {
		if(!PlayerManager.getInstance().playerExists(p))
			return false;
		
		if(e instanceof Player) {
			Player tmp = (Player) e;
			if(tmp.getGameMode().equals(GameMode.SPECTATOR) 
					|| tmp.getGameMode().equals(GameMode.CREATIVE)) 
				return false;
		}
				
		MagicEntityDamageByEntityEvent event = new MagicEntityDamageByEntityEvent(p, e, dmg, dr);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return false;

		ManualDamage.doDamage(p, e, event.getDamage(), event);

		if(effect != null 
				&& e.getHealth() > 1
				&& !e.isDead()) {
			effect.playEffect(p, e, dr);
		}
		
		return true;
	}
	
	public static boolean damageTiming(Player p, LivingEntity e, ItemStackRune dr) {
		return damageTiming(p, e, dr, dr.getDamage());
	}
	
	public static boolean damageTiming(Player p, LivingEntity e, ItemStackRune dr, double dmg) {
		if(!PlayerManager.getInstance().playerExists(p))
			return false;
		
		if(e == null || e.isDead())
			return false;
		
		if(e instanceof Player) {
			Player tmp = (Player) e;
			if(tmp.getGameMode().equals(GameMode.SPECTATOR) 
					|| tmp.getGameMode().equals(GameMode.CREATIVE)) 
				return false;
		}
		
		MagicEntityDamageByEntityEvent event = new MagicEntityDamageByEntityEvent(p, e, dmg, dr);

		EpicDamageType damageType = EpicDamageType.MAGIC;
		Pair<Double, Boolean> damageInfo = DamageManager.getInstance()
				.getMagicCalculator()
				.calc(p, e, dmg, dr);
		
		EpicAttackEvent attackEvent = new EpicAttackEvent(p, e, damageType, 
				damageInfo.getKey(), 1, damageInfo);
		Bukkit.getPluginManager().callEvent(attackEvent);
		
		if(attackEvent.isCancelled())
			return false;
		if(attackEvent.getDmg() <= 0) 
			return false;
		
		dmg = attackEvent.getDmg() * attackEvent.getModifier();
		if(dmg <= 0)
			return false;
		
		if(e instanceof Player) {
			Pair<Double, Boolean> defenseInfo = DamageManager.getInstance()
					.getDefenseCalculator().calc(p, e, dmg);
			
			EpicDefenseEvent defenseEvent = new EpicDefenseEvent(p, e, damageType,
					defenseInfo.getKey(), 1, defenseInfo);
			Bukkit.getPluginManager().callEvent(defenseEvent);
			
			if(defenseEvent.isCancelled()) 
				return false;
			
			dmg = defenseEvent.getDmg() * defenseEvent.getModifier();
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) e);
			int level = rpg.getInfo().getLevel();
			if(dmg < ((level / 10.) + 2))
				dmg = (level / 10.) + 2;
		}
		
		EpicEffectEvent effectEvent = new EpicEffectEvent(p, e, damageType,
				dmg, 1, damageInfo);
		Bukkit.getPluginManager().callEvent(effectEvent);
		
		if(effectEvent.isCancelled())
			return false;
		
		if(effectEvent.getDmg() <= 0)
			return false;
		
		dmg = effectEvent.getFinalDamage();
		if(dmg <= 0) 
			return false;
		
		if(!ManualDamage.doDamageWithCheck(p, e, event.getDamage(), event))
			return false;

		return true;
	}
	
}
