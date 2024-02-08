package me.Vark123.EpicRPG.MMExtension.Conditions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.api.skills.conditions.ISkillMetaCondition;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.core.skills.SkillCondition;

public class HealthCheckpointCondition extends SkillCondition implements ISkillMetaCondition, IEntityCondition {

	private PlaceholderDouble hp;
	private boolean below = false;
	
	public HealthCheckpointCondition(final String line, final MythicLineConfig mlc) {
		super(line);
		this.hp = mlc.getPlaceholderDouble(new String[] {"hp", "health", "a", "amount"}, 100.);
	}

	@Override
	public boolean check(SkillMetadata meta) {
		SkillCaster caster = meta.getCaster();
		AbstractEntity ae = caster.getEntity();
		Entity entity = ae.getBukkitEntity();
		if(!(entity instanceof LivingEntity))
			return false;
		LivingEntity le = (LivingEntity) entity;
		double _hp = hp.get(ae);
		if(le.getHealth() > _hp) {
			below = false;
			return false;
		}
		if(below)
			return false;
		below = true;
		return true;
	}

	@Override
	public boolean check(AbstractEntity ae) {
		Entity entity = ae.getBukkitEntity();
		if(!(entity instanceof LivingEntity))
			return false;
		LivingEntity le = (LivingEntity) entity;
		double _hp = hp.get(ae);
		if(le.getHealth() > _hp) {
			below = false;
			return false;
		}
		if(below)
			return false;
		below = true;
		return true;
	}

}
