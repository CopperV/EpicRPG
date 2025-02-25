package me.Vark123.EpicRPG.MMExtension.Targeters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.targeters.IEntitySelector;
import io.lumine.mythic.core.utils.annotations.MythicTargeter;

@MythicTargeter(aliases={"APIR","EPIR"}, description="Targets all possible enemies in radius")
public class AlliesPlayersInRadiusTargeter extends IEntitySelector {

	private static final List<String> playerAlliesFactions = new ArrayList<>(Arrays.asList(
			"DEFENDERS",
			"SUMMONS",
			"NEUTRAL",
			"NEUTRAL_ANIMALS"
	));
	
	private PlaceholderDouble radius;
	
	public AlliesPlayersInRadiusTargeter(SkillExecutor exec, MythicLineConfig mlc) {
		super(exec, mlc);
        this.radius = PlaceholderDouble.of(mlc.getString(new String[]{"radius", "r"}, "5", new String[0]));
	}

    @Override
    public Collection<AbstractEntity> getEntities(SkillMetadata data) {
        SkillCaster am = data.getCaster();
        
        AbstractEntity aCaster = am.getEntity();
        AbstractLocation loc = aCaster.getLocation();
        double r = radius.get(data);
        double sqrRadius = r*r;
        
        Collection<AbstractEntity> targets = aCaster.getWorld().getEntitiesNearLocation(loc, r, aTarget -> {
        	if(loc.distanceSquared(aTarget) > sqrRadius)
        		return false;
        	
    		if(aTarget.getBukkitEntity() instanceof Player)
        		return true;
        	
        	if(!MythicBukkit.inst().getMobManager().isMythicMob(aTarget))
        		return false;
        	
        	ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(aTarget);
        	if(!aMob.hasFaction())
        		return false;
        	
        	return playerAlliesFactions.contains(aMob.getFaction().toUpperCase());
        });
        
        return targets;
    }
}
