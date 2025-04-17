package me.Vark123.EpicRPG.OldFightSystem.StatsCalculator;

import org.bukkit.entity.Entity;

import me.Vark123.EpicRPG.Utils.Pair;

public class DefenseCalculator implements IDamageCalculator {

	@Override
	public Pair<Double, Boolean> calc(Entity damager, Entity victim, double dmg, Object... args) {
		Pair<Double, Boolean> pair = new Pair<>(dmg, false);
		
		return pair;
	}

}
