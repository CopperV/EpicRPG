package me.Vark123.EpicRPG;

import java.awt.Color;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.Vark123.EpicRPG.Options.Serializables.ScoreboardSerializable;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class RpgScoreboard {
	
	public static void createScore(Player p) {
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		Scoreboard board = rpg.getBoard();
		Objective obj = board.registerNewObjective("test", "dummy", ChatColor.translateAlternateColorCodes('&', ChatColor.of(new Color(132, 165, 184)).toString()+""+ChatColor.BOLD+"Archolos &o#1"));
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		board.registerNewTeam("rpg_info");
		board.registerNewTeam("rpg_zasoby");
		board.registerNewTeam("rpg_staty");
		
		board.registerNewTeam("rpg_nick");
		board.registerNewTeam("rpg_klasa");
		board.registerNewTeam("rpg_level");
		board.registerNewTeam("rpg_exp");
		board.registerNewTeam("rpg_pn");

		board.registerNewTeam("rpg_money");
		board.registerNewTeam("rpg_stygia");
		board.registerNewTeam("rpg_coins");
		board.registerNewTeam("rpg_brylki");

		board.registerNewTeam("rpg_str");
		board.registerNewTeam("rpg_wytrz");
		board.registerNewTeam("rpg_zr");
		board.registerNewTeam("rpg_zd");
		board.registerNewTeam("rpg_int");
		board.registerNewTeam("rpg_mana");
		board.registerNewTeam("rpg_krag");
		board.registerNewTeam("rpg_walka");
		
		board.registerNewTeam("rpg_stat1");
		board.registerNewTeam("rpg_stat2");
		board.registerNewTeam("rpg_stat3");

		board.registerNewTeam("rpg_dmg");
		board.registerNewTeam("rpg_def");
		
		board.registerNewTeam("rpg_line_15");
		board.registerNewTeam("rpg_line_14");
		board.registerNewTeam("rpg_line_13");
		board.registerNewTeam("rpg_line_12");
		board.registerNewTeam("rpg_line_11");
		board.registerNewTeam("rpg_line_10");
		board.registerNewTeam("rpg_line_9");
		board.registerNewTeam("rpg_line_8");
		board.registerNewTeam("rpg_line_7");
		board.registerNewTeam("rpg_line_6");
		board.registerNewTeam("rpg_line_5");
		board.registerNewTeam("rpg_line_4");
		board.registerNewTeam("rpg_line_3");
		board.registerNewTeam("rpg_line_2");
		board.registerNewTeam("rpg_line_1");
		board.registerNewTeam("rpg_line_0");

		if(!rpg.getScoreboard().getOption().getValue().isEnabled())
			return;
		setScore(rpg, board);
	}
	
	public static void updateScore(Player p) {
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(!rpg.getScoreboard().getOption().getValue().isEnabled())
			return;
		
		Scoreboard board = rpg.getBoard();

		board.getEntries().stream().forEach(s -> {
			board.resetScores(s);
		});
		setScore(rpg, board);
	}

	private static void setScore(RpgPlayer rpg, Scoreboard board) {
		Objective obj = board.getObjective(DisplaySlot.SIDEBAR);
		ScoreboardSerializable sc = rpg.getScoreboard().getOption().getValue();
		MutableInt index = new MutableInt(15);
		Player p = rpg.getPlayer();
		sc.getLines().stream().forEachOrdered(line -> {
			int i = index.getValue();
			Team team = board.getTeam("rpg_line_"+i);
			team.setSuffix(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(p, line)));
			team.addEntry(ChatColor.of(new Color(i, i, i)).toString());
			obj.getScore(ChatColor.of(new Color(i, i, i)).toString()).setScore(i);
			index.decrement();
		});
		p.setScoreboard(board);
	}
	
}
