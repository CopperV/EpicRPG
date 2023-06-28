package me.Vark123.EpicRPG.MMExtension.Conditions;

import java.util.Collection;

import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.conditions.ILocationCondition;
import io.lumine.mythic.api.skills.conditions.ISkillMetaCondition;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.utils.numbers.RangedInt;
import io.lumine.mythic.core.skills.SkillCondition;

public class GeneralMobsInRadiusCondition extends SkillCondition implements ISkillMetaCondition, ILocationCondition {

	private RangedInt amount;
	private PlaceholderDouble radius;
	
	public GeneralMobsInRadiusCondition(final String line, final MythicLineConfig mlc) {
		super(line);
		final String k = mlc.getString(new String[] { "amount", "a" }, "1", this.conditionVar);
        this.amount = new RangedInt(k);
        this.radius = mlc.getPlaceholderDouble(new String[] { "radius", "r" }, "5", this.conditionVar);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean check(SkillMetadata meta) {
		SkillCaster caster = meta.getCaster();
		AbstractLocation l = caster.getEntity().getLocation();
		double radius = this.radius.get(meta);
		double radiusSq = radius*radius;
		Collection<AbstractEntity> collection = SkillCondition.getPlugin().getVolatileCodeHandler().getWorldHandler().getEntitiesNearLocation(l, radius);
		int count = (int) collection.stream().filter(entity -> {
			if(l.distanceSquared(entity.getLocation()) > radiusSq)
				return false;
			if(!MythicBukkit.inst().getMobManager().isActiveMob(entity))
				return false;
			if(entity.getBukkitEntity() instanceof Player)
				return false;
			return true;
		}).count();
		return this.amount.equals(count);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean check(AbstractLocation l) {
		double radius = this.radius.get();
		double radiusSq = radius*radius;
		Collection<AbstractEntity> collection = SkillCondition.getPlugin().getVolatileCodeHandler().getWorldHandler().getEntitiesNearLocation(l, radius);
		int count = (int) collection.stream().filter(entity -> {
			if(l.distanceSquared(entity.getLocation()) > radiusSq)
				return false;
			if(!MythicBukkit.inst().getMobManager().isActiveMob(entity))
				return false;
			if(entity.getBukkitEntity() instanceof Player)
				return false;
			return true;
		}).count();
		return this.amount.equals(count);
	}

}
