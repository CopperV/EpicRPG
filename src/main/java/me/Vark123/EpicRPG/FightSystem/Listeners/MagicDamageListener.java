package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.DamageManager;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.FightSystem.Events.MagicEntityDamageByEntityEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.Utils.Pair;

public class MagicDamageListener implements Listener {

	@EventHandler
	public void onDamage(MagicEntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		Entity victim = e.getEntity();
		double dmg = e.getDamage();
		Pair<Double, Boolean> damageInfo = new Pair<>(dmg, false);
		EpicDamageType damageType = EpicDamageType.MAGIC;
		
		if(victim instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) victim;
			le.setNoDamageTicks(0);
			
			if(MythicBukkit.inst().getMobManager().isActiveMob(le.getUniqueId())) {
				ActiveMob am = MythicBukkit.inst().getMobManager().getMythicMobInstance(le);
				if(am.hasImmunityTable()) {
					am.getImmunityTable().clearCooldown(BukkitAdapter.adapt(damager));
				}
			}
		}
		
		ItemStackRune ir = e.getRune();
		damageInfo = DamageManager.getInstance()
				.getMagicCalculator()
				.calc(damager, victim, dmg, ir);
		
		EpicAttackEvent attackEvent = new EpicAttackEvent(damager, victim, damageType, 
				damageInfo.getKey(), 1, damageInfo, ir);
		Bukkit.getPluginManager().callEvent(attackEvent);
		
		if(attackEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		if(attackEvent.getDmg() <= 0) {
			e.setCancelled(true);
			return;
		}
		
		dmg = attackEvent.getDmg() * attackEvent.getModifier();
		if(dmg <= 0) {
			e.setCancelled(true);
			return;
		}
		
		//DEFENSE STATEMENT
		if(victim instanceof Player) {
			Pair<Double, Boolean> defenseInfo = DamageManager.getInstance()
					.getDefenseCalculator().calc(damager, victim, dmg);
			
			EpicDefenseEvent defenseEvent = new EpicDefenseEvent(damager, victim, damageType,
					defenseInfo.getKey(), 1, defenseInfo, ir);
			Bukkit.getPluginManager().callEvent(defenseEvent);
			
			if(defenseEvent.isCancelled()) {
				e.setCancelled(true);
				return;
			}
			
			dmg = defenseEvent.getDmg() * defenseEvent.getModifier();
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) victim);
			int level = rpg.getInfo().getLevel();
			if(dmg < ((level / 10.) + 2))
				dmg = (level / 10.) + 2;
		}
		
		EpicEffectEvent effectEvent = new EpicEffectEvent(damager, victim, damageType,
				dmg, 1, damageInfo, ir);
		Bukkit.getPluginManager().callEvent(effectEvent);
		
		if(effectEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		
		if(effectEvent.getDmg() <= 0) {
			e.setCancelled(true);
			return;
		}
		
		dmg = effectEvent.getFinalDamage();
		if(dmg <= 0) {
			e.setCancelled(true);
			return;
		}
		
		e.setDamage(dmg);
	}
	
}
