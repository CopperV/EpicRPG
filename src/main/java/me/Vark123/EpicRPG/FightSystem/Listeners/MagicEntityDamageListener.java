package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.Utils.Utils;

public class MagicEntityDamageListener implements Listener {
	
	@EventHandler
	public void onDamage(MagicEntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		DamageSource source = e.getDamageSource();
		Entity _damager = source.getCausingEntity();
		Entity _victim = e.getEntity();
		if(!(_damager instanceof LivingEntity) || !(_victim instanceof LivingEntity))
			return;
		
		LivingEntity damager = (LivingEntity) _damager;
		LivingEntity victim = (LivingEntity) _victim;

		double damage = e.getDamage();
		DamageType damageType = DamageType.MAGIC;
		
		EpicRune rune = e.getRune();
		DamageCalculatorResult damageInfo = DamageManager.get()
				.getMagicCalculator().calc(damager, victim, damage, rune);
		
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
				damageInfo,
				rune);
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
				.getDefenseCalculator().calc(damager, victim, damage, damageInfo, rune);
		
		EpicDefenseEvent defenseEvent = new EpicDefenseEvent(
				damager, 
				victim, 
				source, 
				damageType, 
				damageInfo.damage,
				damageInfo,
				rune);
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
					damageInfo,
					rune);
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
				damageInfo,
				rune);
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
	}

}
