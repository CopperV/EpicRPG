package me.Vark123.EpicRPG.FightSystem.Calculators;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
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
			double armor = aMob.getArmor();
			
			result.damage = baseDamage * (1 - (armor)/(armor + baseDamage*0.5));
			
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
			result.damage -= result.isCrit ? 
					stats.getFinalOchrona() * 0.3 :
					(stats.getFinalOchrona() + 1) * 0.1;
			return result;
		}
		
		double defense = stats.getFinalOchrona();
		double wytrz = stats.getFinalWytrzymalosc();
		
		result.damage = baseDamage * (1 - (defense + wytrz)/(defense + wytrz + baseDamage*0.5));
		
		return result;
	}

}
