package me.Vark123.EpicRPG.MMExtension.Conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillCondition;

public class AnyStanceCondition extends SkillCondition implements IEntityCondition {
	public AnyStanceCondition(final String line, final MythicLineConfig mlc) {
		super(line);
	}

	@Override
	public boolean check(AbstractEntity aEntity) {
		if(!MythicBukkit.inst().getMobManager().isActiveMob(aEntity))
			return false;
		
		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(aEntity);
		String stance = aMob.getStance();
		
		return stance != null && !stance.isBlank() && !stance.equalsIgnoreCase("default");
	}
}
