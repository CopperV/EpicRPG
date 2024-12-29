package me.Vark123.EpicRPG.RuneSystem.Functional;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface IRuneHitCondition {

	public boolean check(Player caster, LivingEntity hit);
	
}
