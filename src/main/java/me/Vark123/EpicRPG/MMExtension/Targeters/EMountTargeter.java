package me.Vark123.EpicRPG.MMExtension.Targeters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.targeters.IEntitySelector;

public class EMountTargeter extends IEntitySelector {

	public EMountTargeter(SkillExecutor manager, MythicLineConfig mlc) {
		super(manager, mlc);
	}

	@Override
	public Collection<AbstractEntity> getEntities(SkillMetadata data) {
		Set<AbstractEntity> targets = new HashSet<>();
		if(!(data.getCaster() instanceof ActiveMob))
			return targets;

		AbstractEntity e = ((ActiveMob) data.getCaster()).getEntity().getVehicle();
		if(e != null)
			targets.add(e);
		
		return targets;
	}

}
