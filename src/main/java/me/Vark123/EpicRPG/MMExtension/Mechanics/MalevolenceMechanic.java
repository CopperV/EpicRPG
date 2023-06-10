package me.Vark123.EpicRPG.MMExtension.Mechanics;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.adapters.SkillAdapter;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderFloat;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;

public class MalevolenceMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected PlaceholderFloat velocity;
	protected PlaceholderFloat velocityY;
	protected PlaceholderFloat radius;
	
	public MalevolenceMechanic(SkillExecutor exec, String skill, MythicLineConfig mlc) {
		super(exec, skill, mlc);
		
		this.velocity = mlc.getPlaceholderFloat(new String[] { "velocity", "v" }, 1.0F, new String[0]);
	    this.velocityY = mlc.getPlaceholderFloat(new String[] { "velocityy", "yvelocity", "vy", "yv" }, 1.0F, new String[0]);
	    this.radius = mlc.getPlaceholderFloat(new String[] {"radius","r"}, 10.0F, new String[0]);
	}

	@Override
	public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		AbstractLocation tLoc = target.getLocation();
		AbstractLocation cLoc = data.getCaster().getLocation();
		float distance = (float) tLoc.distance(cLoc);
		float velocity = this.velocity.get(data, target) / distance;
		float velocityY = this.velocityY.get(data, target) / (10.0F);
		SkillAdapter.get().throwSkill(cLoc, target, velocity, velocityY);
		return SkillResult.SUCCESS;
	}

}
