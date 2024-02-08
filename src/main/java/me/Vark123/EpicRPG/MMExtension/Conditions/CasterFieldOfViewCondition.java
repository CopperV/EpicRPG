package me.Vark123.EpicRPG.MMExtension.Conditions;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityComparisonCondition;
import io.lumine.mythic.api.skills.conditions.IEntityLocationComparisonCondition;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillCondition;

public class CasterFieldOfViewCondition extends SkillCondition implements IEntityComparisonCondition, IEntityLocationComparisonCondition {

	private PlaceholderDouble angle;
	
	public CasterFieldOfViewCondition(final String line, final MythicLineConfig mlc) {
		super(line);
        this.angle = mlc.getPlaceholderDouble(new String[] { "angle", "a" }, 90);
	}

	@Override
	public boolean check(AbstractEntity entity, AbstractEntity target) {
		Location loc1 = entity.getBukkitEntity().getLocation();
		Location loc2 = target.getBukkitEntity().getLocation();
		
		Vector vec1 = loc2.clone().subtract(loc1).toVector().normalize();
		Vector vec2 = target.getBukkitEntity().getLocation().getDirection();
		
		vec2.setY(0);
		Location loc3 = loc2.clone().add(vec2.multiply(3));
		vec2 = loc3.clone().subtract(loc2).toVector().normalize();
		
		vec1.setY(0);
		vec2.setY(0);
		double calcAngle = Math.toDegrees(vec1.angle(vec2)) - 180;
		
		double _angle = angle.get(target);
		return Math.abs(calcAngle) <= _angle;
	}

	@Override
	public boolean check(AbstractEntity entity, AbstractLocation loc) {
		Vector vec1 = entity.getBukkitEntity().getLocation().getDirection();
		Vector vec2 = BukkitAdapter.adapt(loc).getDirection().multiply(-1);
		double xv = vec1.getX() * vec2.getZ() - vec1.getZ() * vec2.getX();
		double zv = vec1.getX() * vec2.getX() - vec1.getZ() * vec2.getZ();
		double calcAngle = Math.atan2(xv, zv);
		calcAngle = Math.toDegrees(calcAngle);
		double _angle = angle.get(entity);
		return Math.abs(calcAngle) <= _angle;
	}
	
}
