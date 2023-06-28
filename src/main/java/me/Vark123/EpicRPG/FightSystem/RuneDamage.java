package me.Vark123.EpicRPG.FightSystem;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifierManager;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.RuneEffect;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;

public class RuneDamage {
	
	@Deprecated
	public static boolean damageSwietaStrzala(Player p, LivingEntity e, double dmg) {
		return true;
	}

	@Deprecated
	public static boolean damageKrwawaStrzala(Player p, LivingEntity e, double damage) {
		return true;
	}

	@Deprecated
	public static boolean damageOgnistaStrzala(Player p, LivingEntity e, double damage) {
		return true;		
	}

	@Deprecated
	public static boolean damageZatrutaStrzala(Player p, LivingEntity e, double damage) {
		return true;
	}
	
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
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		dmg = DamageManager.getInstance().getRuneCalculator().calc(p, e, dmg, dr);
		
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, e, DamageCause.CUSTOM, dmg);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return false;

		RpgModifiers modifiers = rpg.getModifiers();
		if(modifiers.hasRytualWzniesienia()) {
			double restoreHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.03;
			RpgPlayerHealEvent event2 = new RpgPlayerHealEvent(rpg, restoreHp);
			Bukkit.getPluginManager().callEvent(event2);
			p.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, p.getLocation().add(0,1,0), 12, 0.4F, 0.4F, 0.4F, 0.05f);
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, .7f);
		}

		if(e.getHealth() - event.getDamage() < 1.0D 
				&& modifiers.hasKrewPrzodkow() 
				&& dr.getMagicType().equalsIgnoreCase("krew")) {
			RuneUtils.krewPrzodkowEffect(rpg);
		}

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
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		dmg = DamageManager.getInstance().getRuneCalculator().calc(p, e, dmg, dr);
		
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, e, DamageCause.CUSTOM, dmg);
		if(e instanceof Player)
			dmg = DamageManager.getInstance().getDefenseCalculator().calc(p, e, dmg);
		
		Map<EventPriority, List<DamageModifier>> mods = DamageModifierManager.getInstance().getModifiers();
		for(EventPriority priority : mods.keySet()) {
			for(DamageModifier modifier : mods.get(priority)) {
				dmg = modifier.modifyDamage(p, e, dmg, DamageCause.CUSTOM);
				if(dmg < 0){
					return false;
				}
			}
		}
		
		if(!ManualDamage.doDamageWithCheck(p, e, event.getDamage(), event))
			return false;
		
		RpgModifiers modifiers = rpg.getModifiers();
		if(modifiers.hasRytualWzniesienia()) {
			double restoreHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.03;
			RpgPlayerHealEvent event2 = new RpgPlayerHealEvent(rpg, restoreHp);
			Bukkit.getPluginManager().callEvent(event2);
			p.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, p.getLocation().add(0,1,0), 12, 0.4F, 0.4F, 0.4F, 0.05f);
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, .7f);
		}
		
		if(e.getHealth() - event.getDamage() < 1.0D 
				&& modifiers.hasKrewPrzodkow() 
				&& dr.getMagicType().equalsIgnoreCase("krew")) {
			RuneUtils.krewPrzodkowEffect(rpg);
		}

		
		return true;
	}
	
}
