package me.Vark123.EpicRPG.MMExtension.Mechanics;

import java.io.File;

import org.bukkit.entity.Entity;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;

public class SetTargetMechanic extends SkillMechanic implements ITargetedEntitySkill {
	
	public SetTargetMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
		super(manager, file, line, mlc);
	}

	@Override
	public SkillResult castAtEntity(SkillMetadata arg0, AbstractEntity arg1) {
		if(arg1 == null)
			return SkillResult.CONDITION_FAILED;
		AbstractEntity entity = arg0.getCaster().getEntity();
		Entity bukkitEntity = BukkitAdapter.adapt(entity);
		ActiveMob activeMob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(bukkitEntity);
		if(activeMob == null) {
			return SkillResult.CONDITION_FAILED;
		}
		if(activeMob.hasThreatTable()) {
			activeMob.getThreatTable().Taunt(arg1);
			activeMob.getThreatTable().targetHighestThreat();
			return SkillResult.SUCCESS;
		}
		activeMob.setTarget(arg1);
		return SkillResult.SUCCESS;
	}

}
