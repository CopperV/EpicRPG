package me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;

public class EmptyEntityRuneCondition implements IRuneHitCondition {

	@Override
	public boolean check(Player caster, LivingEntity hit) {
		return false;
	}

}
