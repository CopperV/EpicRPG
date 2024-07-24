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
	public final int PN_PER_LEVEL = 10;
	
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
		info.getRpg().updateBarExp();
	}
	
	private void updateLvl(RpgPlayerInfo info) {
		info.addPN(PN_PER_LEVEL);
		
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

		info.getRpg().updateBarLevel();
		
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
		switch(level) {
		case 0:
			return 0;
		case 1:
			return 400;
		case 2:
			return 800;
		case 3:
			return 1350;
		case 4:
			return 1900;
		case 5:
			return 2500;
		case 6:
			return 3250;
		case 7:
			return 4000;
		case 8:
			return 5000;
		case 9:
			return 6250;
		case 10:
			return 7500;
		case 11:
			return 9000;
		case 12:
			return 11000;
		case 13:
			return 13500;
		case 14:
			return 16500;
		case 15:
			return 20000;
		case 16:
			return 24000;
		case 17:
			return 29000;
		case 18:
			return 35000;
		case 19:
			return 42000;
		case 20:
			return 50000;
		case 21:
			return 60000;
		case 22:
			return 72500;
		case 23:
			return 87500;
		case 24:
			return 105000;
		case 25:
			return 125000;
		case 26:
			return 150000;
		case 27:
			return 175000;
		case 28:
			return 200000;
		case 29:
			return 230000;
		case 30:
			return 265000;
		case 31:
			return 305000;
		case 32:
			return 350000;
		case 33:
			return 400000;
		case 34:
			return 450000;
		case 35:
			return 500000;
		case 36:
			return 550000;
		case 37:
			return 600000;
		case 38:
			return 650000;
		case 39:
			return 700000;
		case 40:
			return 750000;
		case 41:
			return 825000;
		case 42:
			return 900000;
		case 43:
			return 1000000;
		case 44:
			return 1105000;
		case 45:
			return 1215000;
		case 46:
			return 1330000;
		case 47:
			return 1550000;
		case 48:
			return 1680000;
		case 49:
			return 1830000;
		case 50:
			return 2000000;
		case 51:
			return 2200000;
		case 52:
			return 2450000;
		case 53:
			return 2750000;
		case 54:
			return 3100000;
		case 55:
			return 3500000;
		case 56:
			return 4000000;
		case 57:
			return 4500000;
		case 58:
			return 5000000;
		case 59:
			return 5500000;
		case 60:
			return 6000000;
		case 61:
			return 6600000;
		case 62:
			return 7300000;
		case 63:
			return 8000000;
		case 64:
			return 8700000;
		case 65:
			return 9500000;
		case 66:
			return 10400000;
		case 67:
			return 11300000;
		case 68:
			return 12200000;
		case 69:
			return 13_100_000;
		case 70:
			return 14_000_000;
		case 71:
			return 15_000_000;
		case 72:
			return 16_250_000;
		case 73:
			return 17_750_000;
		case 74:
			return 19_500_000;
		case 75:
			return 21_000_000;
		case 76:
			return 23_900_000;
		case 77:
			return 26_700_000;
		case 78:
			return 29_900_000;
		case 79:
			return 33_500_000;
		case 80:
			return 37_500_000;
		case 81:
			return 42_000_000;
		case 82:
			return 47_000_000;
		case 83:
			return 52_500_000;
		case 84:
			return 58_500_000;
		case 85:
			return 65_000_000;
		case 86:
			return 72_000_000;
		case 87:
			return 79_500_000;
		case 88:
			return 87_500_000;
		case 89:
			return 96_000_000;
		case 90:
			return 105_000_000;
		case 91:
			return 115_000_000;
		case 92:
			return 126_250_000;
		case 93:
			return 139_000_000;
		case 94:
			return 153_500_000;
		case 95:
			return 170_000_000;
		case 96:
			return 188_000_000;
		case 97:
			return 208_000_000;
		case 98:
			return 230_000_000;
		case 99:
			return 255_000_000;
		case 100:
			return 280_000_000;
		default:
			return Integer.MAX_VALUE;
		}
	}
	
}
