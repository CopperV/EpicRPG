package me.Vark123.EpicRPG.FightSystem.StatsCalculator;

import org.bukkit.entity.Entity;

import me.Vark123.EpicRPG.Utils.Pair;

public interface IDamageCalculator {

	public Pair<Double, Boolean> calc(Entity damager, Entity victim, double dmg, Object... args);
	
}
