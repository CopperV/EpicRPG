package me.Vark123.EpicRPG.MMExtension.Targeters;

import java.util.Collection;
import java.util.stream.Collectors;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.targeters.PlayersInWorldTargeter;

public class WorldLimitTargeter extends PlayersInWorldTargeter {

	protected PlaceholderInt pLimit;
	
	public WorldLimitTargeter(SkillExecutor manager, MythicLineConfig mlc) {
		super(manager, mlc);
		
		this.pLimit = mlc.getPlaceholderInteger(new String[] {"max"}, 100);
	}

	@Override
	public Collection<AbstractEntity> getEntities(SkillMetadata data) {
		Collection<AbstractEntity> base = super.getEntities(data);
		return base.stream()
				.limit(pLimit.get())
				.collect(Collectors.toList());
	}

}
