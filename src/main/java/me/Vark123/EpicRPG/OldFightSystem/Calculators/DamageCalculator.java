package me.Vark123.EpicRPG.OldFightSystem.Calculators;

import org.bukkit.entity.Entity;

public interface DamageCalculator {

	public double calc(Entity damager, Entity victim, double dmg);
	
}
