package me.Vark123.EpicRPG.MMExtension.Targeters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.targeters.IEntitySelector;

public class ConditionalTarget extends IEntitySelector {
	
	private String[] conditions;
	private double radius = 6;
	private String selector = "NEAREST";

	public ConditionalTarget(SkillExecutor exec, MythicLineConfig config) {
		super(exec, config);
		StringBuilder sb = new StringBuilder(config.getString("conditions"));
		if(sb.charAt(0) == '[') {
			sb.delete(0, 4);
			sb.delete(sb.length()-2, sb.length());
		}
		this.conditions = sb.toString().split("- ");
		if(config.getDouble("d")>0) {
			this.radius = config.getDouble("d");
		}
		List<String> selectors = Arrays.asList("NEAREST","FURTHEST","RANDOM");
		if(config.getString("type") != null && selectors.contains(config.getString("type").toUpperCase())) {
			selector = config.getString("type").toUpperCase();
		}
	}

	@Override
	public Collection<AbstractEntity> getEntities(SkillMetadata arg0) {
		List<AbstractEntity> toReturn = new ArrayList<>();
		AbstractLocation aLoc = arg0.getCaster().getLocation();
		Collection<AbstractPlayer> set = aLoc.getWorld().getPlayersInRadius(aLoc, radius);
		
		AbstractEntity aEntity = null;
		double distance = -1;
		
		for(AbstractEntity aE : set) {
			if(aE.equals(arg0.getCaster().getEntity()))
				continue;
			
			if(aE.getLocation().distance(aLoc) > radius)
				continue;
			
			boolean passCond = true;
			for(String condition : conditions) {
				if(!MythicBukkit.inst().getSkillManager().getCondition(condition).evaluateEntity(aE)) {
					passCond = false;
					break;
				}
			}
			if(!passCond) 
				continue;
			
			if(selector.equals("RANDOM")) {
				toReturn.add(aE);
				continue;
			}
			
			if(aEntity == null) {
				aEntity = aE;
				distance = aE.getLocation().distance(aLoc);
				continue;
			}
			
			if(selector.equals("NEAREST") && aE.getLocation().distance(aLoc) < distance) {
				aEntity = aE;
				distance = aE.getLocation().distance(aLoc);
				continue;
			}
			
			if(selector.equals("FURTHEST") && aE.getLocation().distance(aLoc) > distance) {
				aEntity = aE;
				distance = aE.getLocation().distance(aLoc);
				continue;
			}
			
		}
		
		if(selector.equals("RANDOM")) {
			return toReturn;
		}else {
			Bukkit.broadcastMessage(aEntity.getName());
			toReturn.add(aEntity);
			return toReturn;
		}
	}

}
