package me.Vark123.EpicRPG.MMExtension.Mechanics;

import java.io.File;

import org.bukkit.entity.Entity;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;

public class RemoveTargetMechanic extends SkillMechanic implements ITargetedEntitySkill {

	public RemoveTargetMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
		super(manager, file, line, mlc);
	}

	@Override
	public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		Entity entity = target.getBukkitEntity();
		ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(entity);
		if(mob == null)
			return SkillResult.CONDITION_FAILED;
		if(mob.isDead())
			return SkillResult.CONDITION_FAILED;
		if(!mob.hasTarget())
			return SkillResult.CONDITION_FAILED;
		mob.resetTarget();
		return SkillResult.SUCCESS;
	}
	
	

}
