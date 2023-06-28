package me.Vark123.EpicRPG.MMExtension.Conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.conditions.ISkillMetaComparisonCondition;
import io.lumine.mythic.api.skills.conditions.ISkillMetaCondition;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.core.skills.SkillCondition;

public class SzansaCondition extends SkillCondition implements ISkillMetaCondition, ISkillMetaComparisonCondition {

	private static final List<UUID> tmp = new ArrayList<>();
	private PlaceholderDouble chance;
	
	public SzansaCondition(final String line, final MythicLineConfig mlc) {
		super(line);
		this.chance = mlc.getPlaceholderDouble(new String[] { "chance", "c", "value", "val" }, "0.5", this.conditionVar);
	}

	@Override
	public boolean check(SkillMetadata arg0) {
		UUID id = arg0.getCaster().getEntity().getUniqueId();
		if(tmp.contains(id)) {
			tmp.remove(id);
			return true;
		}
		double los = Math.random();
		final double szansa = chance.get(arg0);
		boolean check = los <= szansa;
		if(check)
			tmp.add(id);
		return check;	
	}

	@Override
	public boolean check(SkillMetadata arg0, AbstractEntity arg1) {
		UUID id = arg1.getUniqueId();
		if(tmp.contains(id)) {
			tmp.remove(id);
			return true;
		}
		double los = Math.random();
		final double szansa = chance.get(arg0);
		boolean check = los <= szansa;
		if(check)
			tmp.add(id);
		return check;	
	}

}
