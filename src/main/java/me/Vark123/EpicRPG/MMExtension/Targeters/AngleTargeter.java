package me.Vark123.EpicRPG.MMExtension.Targeters;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.bukkit.util.Vector;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.targeters.ILocationSelector;

public class AngleTargeter extends ILocationSelector {

	private PlaceholderDouble angle;
	private PlaceholderInt points;
	@SuppressWarnings("unused")
	private PlaceholderDouble yOffset;

	public AngleTargeter(SkillExecutor exec, MythicLineConfig mlc) {
		super(exec, mlc);
		this.angle = mlc.getPlaceholderDouble(new String[] {"angle","a","ang"}, Math.PI/2);
		this.points = mlc.getPlaceholderInteger(new String[] {"points","p"}, 3);
		this.yOffset = mlc.getPlaceholderDouble(new String[] {"y","yOffset"}, 0.25);
	}

	@Override
	public Collection<AbstractLocation> getLocations(SkillMetadata arg0) {
		Collection<AbstractLocation> radiuses;
		if(arg0.getLocationTargets() != null && !arg0.getEntityTargets().isEmpty())
			radiuses = arg0.getLocationTargets();
		else if (arg0.getEntityTargets() != null && !arg0.getEntityTargets().isEmpty() ) {
			radiuses = new LinkedList<>();
			Collection<AbstractEntity> tmp = arg0.getEntityTargets();
			for(AbstractEntity ent : tmp)
				radiuses.add(ent.getLocation());
		} else {
			radiuses = new LinkedList<>(Arrays.asList(arg0.getCaster().getEntity().getTarget().getLocation()));
		}
		
		float ang = (float) (angle.get()%360);
		float halfAngle = ang/2.0f;
		
		float changeAngle = 0;
		if(points.get() > 1) {
			changeAngle = ang / (float)(points.get() - 1);
		}
		AbstractLocation mainLoc = arg0.getCaster().getLocation();
		Collection<AbstractLocation> toReturn = new LinkedList<>();
		for(AbstractLocation loc : radiuses) {
			Vector vec = new Vector(loc.getX()-mainLoc.getX(), loc.getY()-mainLoc.getY(), loc.getZ()-mainLoc.getZ());
			double distance = Math.sqrt(vec.getX()*vec.getX() + vec.getZ()*vec.getZ());
			float startAngle = (float) Math.toDegrees(Math.atan2(vec.getY(), distance)) * (-1);
			float mainYaw = (float) Math.toDegrees(Math.atan2(vec.getX(), vec.getZ())) * (-1);
			float endAngle = startAngle * (-1);
			
			float modYaw = -halfAngle;
			for(int i = 0; i < points.get(); ++i, modYaw += changeAngle) {
				float tmpPitch = convertPoint(Math.abs(modYaw)%180, 0, 180, startAngle, endAngle);
				float pitch = tmpPitch;
				float yaw = mainYaw + modYaw;
				
				AbstractLocation tmpLoc = mainLoc.clone();
				tmpLoc.setPitch(pitch);
				tmpLoc.setYaw(yaw);
				
				tmpLoc.add(tmpLoc.getDirection().normalize().multiply(10));
				toReturn.add(tmpLoc);
				
			}
			
		}
		 
		return toReturn;
	}
	
	private float convertPoint(float p1, float a1, float b1, float a2, float b2) {
		return (a2 + ((b2-a2)/(b1-a1))*(p1-a1));
	}

}
