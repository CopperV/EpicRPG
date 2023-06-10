package me.Vark123.EpicRPG.MMExtension.Targeters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.targeters.ILocationSelector;

public class CircleSegmentTargeter extends ILocationSelector {

	private PlaceholderDouble angle;
	private PlaceholderDouble radius;
	private PlaceholderDouble startRadius;
	private PlaceholderDouble update;
	private PlaceholderInt pointsPerUpdate;
	private PlaceholderDouble yOffset;
	
	public CircleSegmentTargeter(SkillExecutor exec, MythicLineConfig mlc) {
		super(exec, mlc);
		this.angle = mlc.getPlaceholderDouble(new String[] {"angle","a","ang"}, Math.PI/2);
		this.radius = mlc.getPlaceholderDouble(new String[] {"radius", "r"}, 5.0);
		this.startRadius = mlc.getPlaceholderDouble(new String[] {"start", "startRadius", "sR"}, 0.5);
		this.update = mlc.getPlaceholderDouble(new String[] {"update","upd","u"}, 0.5);
		this.pointsPerUpdate = mlc.getPlaceholderInteger(new String[] {"points","p","pointsPerUpdate"}, 1);
		this.yOffset = mlc.getPlaceholderDouble(new String[] {"y","yOffset"}, 0.25);
	}

	@Override
	public Collection<AbstractLocation> getLocations(SkillMetadata arg0) {
		double mainAngle = Math.toRadians(arg0.getCaster().getLocation().getYaw()) * (-1);
		List<AbstractLocation> toReturn = new ArrayList<>();
		AbstractLocation base = arg0.getOrigin().clone().add(0, yOffset.get(), 0);
		double points = pointsPerUpdate.get();
		double x, z;
		AbstractLocation tmp;
		for(double r = startRadius.get(); r <= radius.get(); r += update.get()) {
			double tmpAngle = angle.get()/2;
			x = r * Math.sin(mainAngle - tmpAngle);
			z = r * Math.cos(mainAngle - tmpAngle);
			tmp = base.clone().add(x, 0, z);
			toReturn.add(tmp);
			x = r * Math.sin(mainAngle + tmpAngle);
			z = r * Math.cos(mainAngle + tmpAngle);
			tmp = base.clone().add(x, 0, z);
			toReturn.add(tmp);
			if(points < 0) {
				continue;
			}
			double jump = angle.get()/(1+points);
			for(int i = 1; i <= points; ++i) {
				x = r * Math.sin(mainAngle - tmpAngle + i*jump);
				z = r * Math.cos(mainAngle - tmpAngle + i*jump);
				tmp = base.clone().add(x, 0, z);
				toReturn.add(tmp);
			}
			points += pointsPerUpdate.get();
		}
		return toReturn;
	}

}
