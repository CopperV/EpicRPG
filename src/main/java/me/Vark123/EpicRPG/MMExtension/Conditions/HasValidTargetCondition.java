package me.Vark123.EpicRPG.MMExtension.Conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillCondition;

public class HasValidTargetCondition extends SkillCondition implements IEntityCondition {
	
	public HasValidTargetCondition(final String line, final MythicLineConfig mlc) {
		super(line);
	}

	@Override
	public boolean check(AbstractEntity aEntity) {
		if(!MythicBukkit.inst().getMobManager().isActiveMob(aEntity))
			return false;
		
		AbstractEntity target = aEntity.getTarget();
		
		if(target == null || target.isDead())
			return false;
		
		return true;
	}
}
