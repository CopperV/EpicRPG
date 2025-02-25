package me.Vark123.EpicRPG;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import me.Vark123.EpicRPG.Options.Serializables.ScoreboardSerializable;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class RpgScoreboard {
	
	private static BukkitTask updateTask;
	
	private static final Map<UUID, Scoreboard> playerScoreboards = new HashMap<>();
	
	public static void createScoreboard(Player player) {
		UUID uid = player.getUniqueId();
		if(playerScoreboards.containsKey(uid))
			return;
		
		if(!PlayerManager.getInstance().playerExists(player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		if(!rpg.getScoreboard().getOption().getValue().isEnabled())
			return;
		
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard scoreboard = manager.getNewScoreboard();
		
		Objective obj = scoreboard.registerNewObjective(
				"epicrpg_scoreboard",
				Criteria.DUMMY,
				ChatColor.of(new Color(132, 165, 184)).toString()+ChatColor.BOLD
					+"ArcholosRPG");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for(int i = 0; i < 16; ++i) {
			String teamName = "rpg_line_" + i;
			if(scoreboard.getTeam(teamName) == null) {
				scoreboard.registerNewTeam(teamName);
			}
		}
		
		playerScoreboards.put(uid, scoreboard);
		player.setScoreboard(scoreboard);
		updateScoreboard(player);
	}
	
	public static void removeScoreboard(Player player) {
		UUID uid = player.getUniqueId();
		playerScoreboards.remove(uid);
		
		player.setScoreboard(player.getServer().getScoreboardManager().getMainScoreboard());
	}
	
	public static void updateScoreboard(Player player) {
		UUID uid = player.getUniqueId();
		if(!playerScoreboards.containsKey(uid))
			return;
		
		if(!PlayerManager.getInstance().playerExists(player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		if(!rpg.getScoreboard().getOption().getValue().isEnabled())
			return;

        Scoreboard scoreboard = playerScoreboards.get(uid);
        Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if(obj == null)
        	return;

		ScoreboardSerializable serializableSB = rpg.getScoreboard().getOption().getValue();
		int index = 15;
		for(String line : serializableSB.getLines()) {
			String teamName = "rpg_line_" + index;
			Team team = scoreboard.getTeam(teamName);
			if(team == null) {
				team = scoreboard.registerNewTeam(teamName);
			}
			
			String entry = ChatColor.of(new Color(index)).toString();
			String content = ChatColor.translateAlternateColorCodes(
					'&',
					PlaceholderAPI.setPlaceholders(player, line));
			
			team.setSuffix(content);
			if(!team.hasEntry(entry)) {
				team.addEntry(entry);
			}
			
			obj.getScore(entry).setScore(index);
			
			--index;
		}
	}
	
	public static void startAutoUpdate(JavaPlugin plugin) {
		if(updateTask != null && !updateTask.isCancelled())
			return;
		
		updateTask = new BukkitRunnable() {
			@Override
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
					updateScoreboard(player);
				}
			}
		}.runTaskTimer(plugin, 0, 60L);
	}
	
	public static void stopAutoUpdate() {
		updateTask.cancel();
		updateTask = null;
	}
	
	public static void removeAllScoreboards() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			removeScoreboard(player);
		}
		
		playerScoreboards.clear();
	}
	
//	public static void createScore(Player p) {
//		if(!PlayerManager.getInstance().playerExists(p))
//			return;
//		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
//		Scoreboard board = rpg.getBoard();
//		Objective obj = board.registerNewObjective(
//				"test", 
//				Criteria.DUMMY, 
//				ChatColor.translateAlternateColorCodes('&', ChatColor.of(new Color(132, 165, 184)).toString()+""+ChatColor.BOLD+"Archolos &o#1"),
//				RenderType.INTEGER);
//		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
//		
////		board.registerNewTeam("rpg_info");
////		board.registerNewTeam("rpg_zasoby");
////		board.registerNewTeam("rpg_staty");
////		
////		board.registerNewTeam("rpg_nick");
////		board.registerNewTeam("rpg_klasa");
////		board.registerNewTeam("rpg_level");
////		board.registerNewTeam("rpg_exp");
////		board.registerNewTeam("rpg_pn");
////
////		board.registerNewTeam("rpg_money");
////		board.registerNewTeam("rpg_stygia");
////		board.registerNewTeam("rpg_coins");
////		board.registerNewTeam("rpg_brylki");
////
////		board.registerNewTeam("rpg_str");
////		board.registerNewTeam("rpg_wytrz");
////		board.registerNewTeam("rpg_zr");
////		board.registerNewTeam("rpg_zd");
////		board.registerNewTeam("rpg_int");
////		board.registerNewTeam("rpg_mana");
////		board.registerNewTeam("rpg_krag");
////		board.registerNewTeam("rpg_walka");
////		
////		board.registerNewTeam("rpg_stat1");
////		board.registerNewTeam("rpg_stat2");
////		board.registerNewTeam("rpg_stat3");
////
////		board.registerNewTeam("rpg_dmg");
////		board.registerNewTeam("rpg_def");
//		
//		board.registerNewTeam("rpg_line_15");
//		board.registerNewTeam("rpg_line_14");
//		board.registerNewTeam("rpg_line_13");
//		board.registerNewTeam("rpg_line_12");
//		board.registerNewTeam("rpg_line_11");
//		board.registerNewTeam("rpg_line_10");
//		board.registerNewTeam("rpg_line_9");
//		board.registerNewTeam("rpg_line_8");
//		board.registerNewTeam("rpg_line_7");
//		board.registerNewTeam("rpg_line_6");
//		board.registerNewTeam("rpg_line_5");
//		board.registerNewTeam("rpg_line_4");
//		board.registerNewTeam("rpg_line_3");
//		board.registerNewTeam("rpg_line_2");
//		board.registerNewTeam("rpg_line_1");
//		board.registerNewTeam("rpg_line_0");
//
//		if(!rpg.getScoreboard().getOption().getValue().isEnabled())
//			return;
//		setScore(rpg, board);
//	}
	
//	public static void updateScore(Player p) {
//		if(!PlayerManager.getInstance().playerExists(p))
//			return;
//		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
//		if(!rpg.getScoreboard().getOption().getValue().isEnabled())
//			return;
//		
//		Scoreboard board = rpg.getBoard();
//
//		if(board.getEntries() != null)
//			board.getEntries().stream().forEach(s -> {
//				board.resetScores(s);
//			});
//		setScore(rpg, board);
//	}

//	private static void setScore(RpgPlayer rpg, Scoreboard board) {
//		Objective obj = board.getObjective(DisplaySlot.SIDEBAR);
//		ScoreboardSerializable sc = rpg.getScoreboard().getOption().getValue();
//		MutableInt index = new MutableInt(15);
//		Player p = rpg.getPlayer();
//		sc.getLines().stream().forEachOrdered(line -> {
//			int i = index.getValue();
//			Team team = board.getTeam("rpg_line_"+i);
//			team.setSuffix(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(p, line)));
//			team.addEntry(ChatColor.of(new Color(i, i, i)).toString());
//			obj.getScore(ChatColor.of(new Color(i, i, i)).toString()).setScore(i);
//			index.decrement();
//		});
//		p.setScoreboard(board);
//	}
	
}
