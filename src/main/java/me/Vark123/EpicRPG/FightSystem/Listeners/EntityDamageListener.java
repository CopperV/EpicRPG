package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import me.Vark123.EpicRPG.FightSystem.DamageManager;
import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Calculators.IDamageCalculator.DamageCalculatorResult;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseMinimalDamageEvent;
import me.Vark123.EpicRPG.FightSystem.Events.MagicEntityDamageByEntityEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.Utils;

public class EntityDamageListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onDamage(EntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		if(e instanceof EntityDamageByEntityEvent)
			return;
		
//		Entity _victim = e.getEntity();
//		if(!(_victim instanceof Player))
//			return;
		Entity _victim = e.getEntity();
		if(!(_victim instanceof LivingEntity))
			return;
		
		LivingEntity victim = (LivingEntity) _victim;
		
		DamageSource source = e.getDamageSource();
		DamageType damageType = DamageType.CUSTOM;
		
		DamageCause cause = e.getCause();
		if(cause.equals(DamageCause.FALL)
				|| cause.equals(DamageCause.LAVA)
				|| cause.equals(DamageCause.VOID))
			return;
		
		DamageCalculatorResult damageInfo = DamageManager.get()
				.getDefenseCalculator().calc(null, victim, e.getDamage(), new DamageCalculatorResult(e.getDamage(), false));
		
		EpicDefenseEvent defenseEvent = new EpicDefenseEvent(
				null, 
				victim, 
				source, 
				damageType, 
				damageInfo.damage,
				damageInfo);
		Bukkit.getPluginManager().callEvent(defenseEvent);
		if(defenseEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		
		damageInfo.damage = defenseEvent.getFinalDamage();
		
		double minimalBaseDamage;
		
		if(victim instanceof Player) {
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) victim);
			int level = rpg.getInfo().getLevel();
			minimalBaseDamage = (level * 0.1) + 2;
		} else {
			minimalBaseDamage = 1;
		}
		
		if(minimalBaseDamage > damageInfo.damage) {
			damageInfo.damage = minimalBaseDamage;
			
			EpicDefenseMinimalDamageEvent minimalDamageEvent = new EpicDefenseMinimalDamageEvent(
					null, 
					victim, 
					source, 
					damageType, 
					damageInfo.damage,
					damageInfo);
			Bukkit.getPluginManager().callEvent(minimalDamageEvent);
			if(minimalDamageEvent.isCancelled()) {
				e.setCancelled(true);
				return;
			}
			
			damageInfo.damage = minimalDamageEvent.getFinalDamage();
		}
		
		if(damageInfo.damage <= 0) {
			e.setCancelled(true);
			return;
		}
		
		EpicDamageEffectEvent effectEvent = new EpicDamageEffectEvent(
				null, 
				victim, 
				source, 
				damageType, 
				damageInfo.damage,
				damageInfo);
		Bukkit.getPluginManager().callEvent(effectEvent);
		if(effectEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		
		damageInfo.damage = effectEvent.getFinalDamage();
		if(damageInfo.damage <= 0) {
			e.setCancelled(true);
			return;
		}
		
		e.setDamage(damageInfo.damage);
		
		if(damageInfo.isCrit) {
			AbstractEntity ae = BukkitAdapter.adapt(victim);
			ae.setMetadata("EpicCrit", true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(e instanceof MagicEntityDamageByEntityEvent)
			return;

		DamageSource source = e.getDamageSource();
		Entity _damager = source.getCausingEntity();
		Entity _victim = e.getEntity();
		if(!(_damager instanceof LivingEntity) || !(_victim instanceof LivingEntity))
			return;
		
		LivingEntity damager = (LivingEntity) _damager;
		LivingEntity victim = (LivingEntity) _victim;
		
		if(e.getCause().equals(DamageCause.PROJECTILE)) {
			Utils.neutralizeEntityNoDamageTicks(damager, victim);
		}
		if(Utils.hasNoDamageTicks(damager, victim)) {
			e.setCancelled(true);
			return;
		}
		
		double damage = e.getDamage();
		DamageType damageType;
		DamageCalculatorResult damageInfo = new DamageCalculatorResult(damage, false);
		
		switch(e.getCause()) {
			case PROJECTILE:
				damageInfo = DamageManager.get()
					.getProjectileCalculator().calc(damager, victim, damage, source.getDirectEntity());
				damageType = DamageType.PROJECTILE;
				break;
			case ENTITY_ATTACK:
				damageInfo = DamageManager.get()
					.getMeleeCalculator().calc(damager, victim, damage);
				damageType = DamageType.MELEE;
				break;
			case CUSTOM:
				if(source.getDamageType().equals(org.bukkit.damage.DamageType.MAGIC))
					damageType = DamageType.MAGIC;
				else
					damageType = DamageType.CUSTOM;
				break;	
			default:
				damageType = DamageType.CUSTOM;
				break;
		}
		
		if(damageInfo.damage <= 0) {
			e.setCancelled(true);
			return;
		}
		
		EpicAttackEvent attackEvent = new EpicAttackEvent(
				damager, 
				victim, 
				source, 
				damageType,
				damageInfo.damage,
				damageInfo);
		Bukkit.getPluginManager().callEvent(attackEvent);
		if(attackEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		
		damageInfo.damage = attackEvent.getFinalDamage();
		if(damageInfo.damage <= 0) {
			e.setCancelled(true);
			return;
		}
		
		//DEFENSE STATEMENT
		damageInfo = DamageManager.get()
				.getDefenseCalculator().calc(damager, victim, damage, damageInfo);
		
		EpicDefenseEvent defenseEvent = new EpicDefenseEvent(
				damager, 
				victim, 
				source, 
				damageType, 
				damageInfo.damage,
				damageInfo);
		Bukkit.getPluginManager().callEvent(defenseEvent);
		if(defenseEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		
		damageInfo.damage = defenseEvent.getFinalDamage();
		double minimalBaseDamage;
		
		if(victim instanceof Player) {
			
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) victim);
			int level = rpg.getInfo().getLevel();
			minimalBaseDamage = (level * 0.1) + 2;
		} else {
			minimalBaseDamage = 1;
		}
		if (minimalBaseDamage > damageInfo.damage) {
			damageInfo.damage = minimalBaseDamage;

			EpicDefenseMinimalDamageEvent minimalDamageEvent = new EpicDefenseMinimalDamageEvent(
					damager,
					victim,
					source, 
					damageType, 
					damageInfo.damage, 
					damageInfo);
			Bukkit.getPluginManager().callEvent(minimalDamageEvent);
			if (minimalDamageEvent.isCancelled()) {
				e.setCancelled(true);
				return;
			}

			damageInfo.damage = minimalDamageEvent.getFinalDamage();
		}

		if (damageInfo.damage <= 0) {
			e.setCancelled(true);
			return;
		}
		
		EpicDamageEffectEvent effectEvent = new EpicDamageEffectEvent(
				damager, 
				victim, 
				source, 
				damageType, 
				damageInfo.damage,
				damageInfo);
		Bukkit.getPluginManager().callEvent(effectEvent);
		if(effectEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		
		damageInfo.damage = effectEvent.getFinalDamage();
		if(damageInfo.damage <= 0) {
			e.setCancelled(true);
			return;
		}
		
		e.setDamage(damageInfo.damage);
		Utils.setLastDamageCalc(victim, damageInfo.damage);
		
		if(damageInfo.isCrit) {
			AbstractEntity ae = BukkitAdapter.adapt(victim);
			ae.setMetadata("EpicCrit", true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void overrideMythicMobsDamage(EntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		AbstractEntity aEntity = BukkitAdapter.adapt(e.getEntity());
		if(aEntity == null || !aEntity.hasMetadata("EpicLastDamage")) {
			return;
		}
		
		double damage = (double) aEntity.getMetadata("EpicLastDamage").get();
		e.setDamage(damage);
		aEntity.removeMetadata("EpicLastDamage");
	}
	
}
