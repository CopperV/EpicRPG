package me.Vark123.EpicRPG.MMExtension.Mechanics;

import java.io.File;

import org.bukkit.Bukkit;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderFloat;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.mechanics.ProjectileMechanic;

public class PlaceholderProjectileMechanic extends ProjectileMechanic {
	
	private PlaceholderFloat pgravity;

	public PlaceholderProjectileMechanic(SkillExecutor executor, File file, String line, MythicLineConfig config) {
		super(executor, file, line, config);
		this.pgravity = config.getPlaceholderFloat(new String[] {"pgravity", "pg"}, 1.0f, new String[0]);
	}

	@Override
	public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		Bukkit.broadcastMessage("§c"+pgravity.get(data));
		super.projectileGravity = pgravity.get(data);
		Bukkit.broadcastMessage("§b"+projectileGravity);
		return super.castAtEntity(data, target);
	}

	@Override
	public SkillResult castAtLocation(SkillMetadata data, AbstractLocation target) {
		Bukkit.broadcastMessage("§c"+pgravity.get(data));
		super.projectileGravity = pgravity.get(data);
		Bukkit.broadcastMessage("§b"+projectileGravity);
		return super.castAtLocation(data, target);
	}
	
}
