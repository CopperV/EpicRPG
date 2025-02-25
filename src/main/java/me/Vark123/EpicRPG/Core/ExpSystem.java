package me.Vark123.EpicRPG.Core;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import me.Vark123.EpicOptions.OptionsAPI;
import me.Vark123.EpicRPG.Config;
import me.Vark123.EpicRPG.Core.Events.ExpModifyEvent;
import me.Vark123.EpicRPG.Core.Events.PlayerLevelUpdateEvent;
import me.Vark123.EpicRPG.Options.Serializables.ResourcesInfoSerializable;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import net.md_5.bungee.api.ChatColor;

public class ExpSystem {

	private final static ExpSystem instance = new ExpSystem();
	
	public final int MAX_LEVEL = 100;
//	public final int PN_PER_LEVEL = Config.get().getPnPerLevel();
	
	private final FireworkEffect fe1;
	private final FireworkEffect fe2;
	private final FireworkEffect fe3;
	private final FireworkEffect fe4;
	
	private ExpSystem() {
		fe1 = FireworkEffect.builder()
				.flicker(true)
				.trail(true)
				.with(Type.BALL_LARGE)
				.withColor(Color.BLUE, Color.WHITE)
				.withFade(Color.FUCHSIA, Color.GRAY)
				.build();
		fe2 = FireworkEffect.builder()
				.flicker(true)
				.trail(true)
				.with(Type.BALL_LARGE)
				.withColor(Color.RED, Color.WHITE)
				.withFade(Color.FUCHSIA, Color.GRAY)
				.build();
		fe3 = FireworkEffect.builder()
				.flicker(true)
				.trail(true)
				.with(Type.BALL_LARGE)
				.withColor(Color.GREEN, Color.WHITE)
				.withFade(Color.FUCHSIA, Color.GRAY)
				.build();
		fe4 = FireworkEffect.builder()
				.flicker(true)
				.trail(true)
				.with(Type.BALL_LARGE)
				.withColor(Color.YELLOW, Color.WHITE)
				.withFade(Color.FUCHSIA, Color.GRAY)
				.build();
	}
	
	public static ExpSystem getInstance() {
		return instance;
	}
	
	public void addExp(RpgPlayer rpg, int amount, String reason) {
		ExpModifyEvent event = new ExpModifyEvent(rpg, amount, 1, reason);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		int exp = (int) (event.getAmount()*event.getModifier());
		if(exp == 0)
			return;
		
		RpgPlayerInfo info = rpg.getInfo();
		if(info.getLevel() >= MAX_LEVEL &&
				(info.getExp() - getNextLevelExp(MAX_LEVEL-1)) > (0.9 * (getNextLevelExp(MAX_LEVEL) - getNextLevelExp(MAX_LEVEL-1)))) {
			info.setExp((int) (0.9 * (getNextLevelExp(MAX_LEVEL) - getNextLevelExp(MAX_LEVEL-1))) + getNextLevelExp(MAX_LEVEL-1));
			return;
		}
		
		info.addXP(exp);
		checkLvl(info);
		
		OptionsAPI.get().getPlayerManager().getPlayerOptions(rpg.getPlayer())
			.ifPresent(op -> {
				op.getPlayerOptionByID("epicrpg_resources")
					.ifPresent(pOption -> {
						ResourcesInfoSerializable option = (ResourcesInfoSerializable) pOption.getValue();
						if(!option.isExpInfo())
							return;
						rpg.getPlayer().sendMessage("§a+"+ exp +" xp §7[§a"+info.getExp()+" xp§7/§a"+info.getNextLevel()+" xp§7]");
					});
			});
	}
	
	public void addRawExp(RpgPlayer rpg, int amount) {
		RpgPlayerInfo info = rpg.getInfo();
		info.addXP(amount);
		checkLvl(info);
	}
	
	public void addQuestExp(RpgPlayer rpg, int xp) {
		addExp(rpg, xp, "quest");
	}
	
	public void addMobExp(RpgPlayer rpg, int xp) {
		addExp(rpg, xp, "mob");
	}
	
	private void checkLvl(RpgPlayerInfo info) {
		if(info.getLevel() >= MAX_LEVEL)
			return;
		if(info.getExp() >= info.getNextLevel())
			updateLvl(info);
//		info.getRpg().updateBarExp();
	}
	
	private void updateLvl(RpgPlayerInfo info) {
		info.addPN(Config.get().getPnPerLevel());
		
		RpgPlayer rpg = info.getRpg();
		RpgStats stats = rpg.getStats();
		Player p = rpg.getPlayer();
		
		if(ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("mag")) {
			stats.setHealth(stats.getHealth()+6);
		} else {
			stats.setHealth(stats.getHealth()+5);
		}
		rpg.updateHp();
		
		info.addLevel(1);
		info.setNextLevel(getNextLevelExp(info.getLevel()));

//		info.getRpg().updateBarLevel();
		
		p.sendTitle("§6§lGRATULACJE!", "§aAwansowales na §6"+(info.getLevel())+" §apoziom", 5, 10, 15);
		Bukkit.broadcastMessage("§6§lGracz " + p.getName() + " awansowal na " + (info.getLevel()) + " poziom!!!");
		
		p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
		
		Firework f1 = p.getWorld().spawn(p.getLocation().add(1, 1, 0), Firework.class);
		Firework f2 = p.getWorld().spawn(p.getLocation().add(-1, 1, 0), Firework.class);
		Firework f3 = p.getWorld().spawn(p.getLocation().add(1, -1, 0), Firework.class);
		Firework f4 = p.getWorld().spawn(p.getLocation().add(-1, -1, 0), Firework.class);
		FireworkMeta fm1 = f1.getFireworkMeta();
		FireworkMeta fm2 = f2.getFireworkMeta();
		FireworkMeta fm3 = f3.getFireworkMeta();
		FireworkMeta fm4 = f4.getFireworkMeta();
		fm1.addEffect(fe1);
		fm2.addEffect(fe2);
		fm3.addEffect(fe3);
		fm4.addEffect(fe4);
		f1.setFireworkMeta(fm1);
		f2.setFireworkMeta(fm2);
		f3.setFireworkMeta(fm3);
		f4.setFireworkMeta(fm4);
		
		PlayerLevelUpdateEvent event = new PlayerLevelUpdateEvent(rpg, info.getLevel());
		Bukkit.getPluginManager().callEvent(event);
		
	}
	
	public int getNextLevelExp(int level) {
		return Config.get().getLevelExpRequirements().getOrDefault(level, Integer.MAX_VALUE);
	}
	
}
