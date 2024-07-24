package me.Vark123.EpicRPG.MMExtension.Conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.conditions.ISkillMetaComparisonCondition;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillCondition;
import io.lumine.mythic.core.utils.Patterns;

public class VariableStanceCondition extends SkillCondition implements ISkillMetaComparisonCondition {

	private PlaceholderString stance;
	private final boolean strictMatch;
    private final boolean parse;
	
	public VariableStanceCondition(final String line, final MythicLineConfig mlc) {
		super(line);
		this.stance = mlc.getPlaceholderString(new String[] { "stance", "s", "value", "val", "v" }, "stance", this.conditionVar);
		this.strictMatch = mlc.getBoolean(new String[]{"strict", "str"}, false);
        this.parse = mlc.getBoolean(new String[]{"parse", "p"}, false);
	}

	@Override
	public boolean check(SkillMetadata data, AbstractEntity entity) {
		if (!MythicBukkit.inst().getMobManager().isActiveMob(entity))
			return false;
		ActiveMob am = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
        String stance = am.getStance();
        if(stance == null)
        	return this.stance == null || this.stance.get(data, entity) == null;
        if(this.parse)
        	Patterns.CompilePatterns();
		return this.strictMatch ? stance.equals(this.stance.get(data, entity)) : stance.contains(this.stance.get(data, entity));
	}

	
	
}
