package me.Vark123.EpicRPG.MMExtension.Mechanics;

import java.io.File;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import me.Vark123.EpicOptions.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class ManaDrainPercentMechanic extends SkillMechanic implements ITargetedEntitySkill {

	private PlaceholderDouble manaPercent;
	
	public ManaDrainPercentMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
		super(manager, file, line, mlc);
		this.manaPercent = mlc.getPlaceholderDouble(new String[] {"mana"}, 1);
	}

	@Override
	public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		Entity e = target.getBukkitEntity();
		if(!(e instanceof Player))
			return SkillResult.INVALID_TARGET;
		
		Player p = (Player)e;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		
		int toRemove = (int) (stats.getFinalMana() * manaPercent.get(data, target));
		new BukkitRunnable() {
			@Override
			public void run() {
				stats.removePresentManaSmart(toRemove);
			}
		}.runTask(Main.getInst());
		
		return SkillResult.SUCCESS;
	}
	
}
