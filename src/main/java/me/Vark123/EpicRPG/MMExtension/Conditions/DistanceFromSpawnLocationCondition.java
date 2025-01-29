package me.Vark123.EpicRPG.MMExtension.Conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.utils.numbers.RangedDouble;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillCondition;

public class DistanceFromSpawnLocationCondition extends SkillCondition implements IEntityCondition {

	private PlaceholderString distance;
	
	public DistanceFromSpawnLocationCondition(final String line, final MythicLineConfig mlc) {
		super(line);
		this.distance = mlc.getPlaceholderString(new String[] { "distance", "d" }, "20", this.conditionVar);
	}

	@Override
	public boolean check(AbstractEntity arg0) {
		if(!MythicBukkit.inst().getMobManager().isActiveMob(arg0))
			return true;
		
		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(arg0);
		
		AbstractLocation currentLocation = aMob.getLocation();
		AbstractLocation spawnLocation = aMob.getSpawnLocation();
		
		if(!currentLocation.getWorld().getName().equals(spawnLocation.getWorld().getName()))
			return false;
		
		double distSquared = currentLocation.distanceSquared(spawnLocation);
		RangedDouble rangedDouble = new RangedDouble(distance.get(arg0));
		
		return rangedDouble.equalsSquared(distSquared);
	}
	
}
