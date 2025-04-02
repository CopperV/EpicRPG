package me.Vark123.EpicRPG.FightSystem.Calculators;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class DefenseCalculator implements IDamageCalculator {

	@Override
	public DamageCalculatorResult calc(
			LivingEntity damager, 
			LivingEntity victim, 
			double damage, 
			Object... args) {
		if(args == null || args.length < 1
				|| !(args[0] instanceof DamageCalculatorResult)) {
			DamageCalculatorResult result = new DamageCalculatorResult(damage, false);
			return result;
		}
		
		DamageCalculatorResult result = (DamageCalculatorResult) args[0];
		double baseDamage = result.damage;
		
		if(MythicBukkit.inst().getMobManager().isMythicMob(victim)) {
			ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(victim);
			if(aMob.getArmor() < 1)
				return result;
			
			double armor = aMob.getArmor();
			
			if(damager instanceof Player && PlayerManager.getInstance().playerExists((Player) damager)
					&& DamageUtils.checkArmorPierce(PlayerManager.getInstance().getRpgPlayer((Player) damager), victim, armor)) {
				damager.getWorld().playSound(damager.getEyeLocation(), Sound.ITEM_MACE_SMASH_AIR, 1, 1.5f);
				damager.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, victim.getLocation().clone().add(0,1,0), 12, 0.4, 0.4, 0.4, 0.1);
				
				return result;
			}
			
			double damageModifier = Math.log1p((baseDamage)/(armor*0.5)) * 0.5;
			
			result.damage = baseDamage * (1 - (armor)/(armor + baseDamage*damageModifier));
			
			return result;
		}
		
		if(!(victim instanceof Player)
				|| !PlayerManager.getInstance().playerExists((Player) victim))
			return result;
		
		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		RpgModifiers modifiers = rpg.getModifiers();
		
		Bukkit.broadcastMessage("Sila Jednosci - Implementacja");
		
		if(damager != null && damager instanceof Player
				&& PlayerManager.getInstance().playerExists((Player) damager)) {
			
			if(damager instanceof Player && PlayerManager.getInstance().playerExists((Player) damager)
					&& DamageUtils.checkArmorPierce(PlayerManager.getInstance().getRpgPlayer((Player) damager), victim, stats.getFinalOchrona())) {
				damager.getWorld().playSound(damager.getEyeLocation(), Sound.ITEM_MACE_SMASH_AIR, 1, 1.5f);
				damager.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, victim.getLocation().clone().add(0,1,0), 12, 0.4, 0.4, 0.4, 0.1);
				
				return result;
			}
			
			result.damage -= stats.getFinalOchrona() * (result.isCrit ? 0.3 : 0.1);
			return result;
		}
		
		double defense = stats.getFinalOchrona();
		double wytrz = stats.getFinalWytrzymalosc();

		double defenseModifier = Math.max(defense + wytrz, 1);
		
		if(damager instanceof Player && PlayerManager.getInstance().playerExists((Player) damager)
				&& DamageUtils.checkArmorPierce(PlayerManager.getInstance().getRpgPlayer((Player) damager), victim, defenseModifier)) {
			damager.getWorld().playSound(damager.getEyeLocation(), Sound.ITEM_MACE_SMASH_AIR, 1, 1.5f);
			damager.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, victim.getLocation().clone().add(0,1,0), 12, 0.4, 0.4, 0.4, 0.1);
			
			return result;
		}
		
		double damageModifier = Math.log1p((baseDamage)/(defenseModifier * 0.5)) * 0.5;
		
		result.damage = baseDamage * (1 - (defenseModifier)/(defenseModifier + baseDamage*damageModifier));
		
		return result;
	}

}
