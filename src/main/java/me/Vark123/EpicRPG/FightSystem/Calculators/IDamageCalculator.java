package me.Vark123.EpicRPG.FightSystem.Calculators;

import org.bukkit.entity.LivingEntity;

import lombok.AllArgsConstructor;

public interface IDamageCalculator {

	public DamageCalculatorResult calc(LivingEntity damager, LivingEntity victim, double damage, Object... args);
	
	@AllArgsConstructor
	public static class DamageCalculatorResult {
		public double damage;
		public boolean isCrit;
	}
}
