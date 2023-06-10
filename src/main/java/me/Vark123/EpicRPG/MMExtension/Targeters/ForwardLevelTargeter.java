package me.Vark123.EpicRPG.MMExtension.Targeters;

import java.util.Collection;
import java.util.HashSet;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.targeters.ILocationSelector;

public class ForwardLevelTargeter extends ILocationSelector {
	
	protected double forward;
	protected float rotate;
	protected boolean useEyeLocation;
	protected boolean rotated = false;

	public ForwardLevelTargeter(SkillExecutor exec, MythicLineConfig mlc) {
		super(exec, mlc);
		this.forward = mlc.getDouble(new String[] { "forward", "f", "amount", "a" }, 5.0D);
	    this.rotate = mlc.getFloat(new String[] { "rotate", "rot" }, 0.0F);
	    this.useEyeLocation = mlc.getBoolean(new String[] { "useeyelocation", "uel" }, false);
	}

	@Override
	public Collection<AbstractLocation> getLocations(SkillMetadata data) {
		SkillCaster am = data.getCaster();
		HashSet<AbstractLocation> targets = new HashSet<>();
	    AbstractLocation location;
	    if (this.useEyeLocation) {
	        location = am.getEntity().getEyeLocation();
	    } else {
	    	location = am.getEntity().getLocation();
	    }
	    location.setY(am.getLocation().getY());
	    if (this.rotate != 0.0F) {
	        location.add(location.getDirection().setY(0).rotate(this.rotate).normalize().multiply(this.forward));
	    } else {
	        location.add(location.getDirection().setY(0).normalize().multiply(this.forward));
	    }
	    targets.add(mutate(data, location));
	    
	    return targets;
	}

}
