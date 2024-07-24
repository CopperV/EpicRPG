package me.Vark123.EpicRPG.MMExtension.Conditions;

import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.core.skills.SkillCondition;

public class SneakingCondition extends SkillCondition implements IEntityCondition {

	public SneakingCondition(final String line, final MythicLineConfig mlc) {
		super(line);
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if(!entity.isPlayer())
			return false;
		return ((Player) entity.getBukkitEntity()).isSneaking();
	}

}
