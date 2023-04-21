package me.Vark123.EpicRPG;

import java.awt.Color;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.RpgStats;
import me.Vark123.EpicRPG.Players.RpgVault;
import net.md_5.bungee.api.ChatColor;

public class RpgScoreboard {

	public static void createScore(Player p) {
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		Scoreboard board = rpg.getBoard();
		Objective obj = board.registerNewObjective("test", "dummy", ChatColor.of(new Color(132, 165, 184)).toString()+""+ChatColor.BOLD+"§9§lArcholos §9§l§o#1");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		setScore(rpg, board);
	}
	
	public static void updateScore(Player p) {
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		Scoreboard board = p.getScoreboard();
		setScore(rpg, board);
	}
	
	private static void setScore(RpgPlayer rpg, Scoreboard board) {
		Objective obj = board.getObjective(DisplaySlot.SIDEBAR);
		RpgStats stats = rpg.getStats();
		RpgPlayerInfo playerInfo = rpg.getInfo();
		RpgVault vault = rpg.getVault();
		String nick = rpg.getPlayer().getName();
		
		if(playerInfo.getLevel() > 10 && !playerInfo.getProffesion().toLowerCase().contains("obywatel")) {
			
			Team team = board.getTeam("rpg_info");
			if(team == null)
				team = board.registerNewTeam("rpg_info");
			team.setSuffix("§3》 "+ChatColor.of(new Color(66, 104, 124)).toString() + "" + ChatColor.BOLD + "INFO §3《");
			team.addEntry(ChatColor.translateAlternateColorCodes('&', "&a"));
			obj.getScore(ChatColor.translateAlternateColorCodes('&', "&a")).setScore(15);
			
//			Score score = obj.getScore("§3》 §x§0§0§8§0§b§4§lINFO §3《");
//			score.setScore(15);
			
			String klasa = "  §bGracz: ";
			switch(ChatColor.stripColor(playerInfo.getProffesion().toLowerCase())) {
				case "wojownik":
					klasa += "§c§o";
					break;
				case "mag":
					klasa += "§5§o";
					break;
				case "mysliwy":
					klasa += "§2§o";
					break;
				default:
					klasa += "§e§o";
					break;
			}
			klasa += nick;
			Score score = obj.getScore(klasa);
			score.setScore(14);
			
			String poziom = "  §bPoziom: §e";
			if(playerInfo.getLevel() == 95)
				poziom += "MAX";
			else
				poziom += playerInfo.getLevel();
			score = obj.getScore(poziom);
			score.setScore(13);
			
			String exp = "  §bExp: §e";
			if(playerInfo.getLevel() == 95)
				exp += "MAX";
			else 
				exp += (playerInfo.getExp()+"§a/§e"+playerInfo.getNextLevel());
			score = obj.getScore(exp);
			score.setScore(12);
			
			score = obj.getScore("  §bPunkty nauki: §e"+playerInfo.getPn());
			score.setScore(11);
			
			Team zasoby = board.getTeam("rpg_zasoby");
			if(zasoby == null)
				zasoby = board.registerNewTeam("rpg_zasoby");
			zasoby.setSuffix("§3》 "+ChatColor.of(new Color(66, 104, 124)).toString() + "" + ChatColor.BOLD + "ZASOBY §3《");
			zasoby.addEntry(ChatColor.translateAlternateColorCodes('&', "&e"));
			obj.getScore(ChatColor.translateAlternateColorCodes('&', "&e")).setScore(10);
			
//			score = obj.getScore("§3》 §x§0§0§8§0§b§4§lZASOBY §3《");
//			score.setScore(10);
			
			score = obj.getScore("  §bSaldo: §e"+((int)Main.eco.getBalance(rpg.getPlayer()))+"$");
			score.setScore(9);
			
			score = obj.getScore("  §bStygia: §3"+vault.getStygia());
			score.setScore(8);
			
			score = obj.getScore("  §bSmocze monety: §4"+vault.getDragonCoins());
			score.setScore(7);
			
			score = obj.getScore("  §bBrylki rudy: §9"+vault.getBrylkiRudy());
			score.setScore(6);

			Team staty = board.getTeam("rpg_staty");
			if(staty == null)
				staty = board.registerNewTeam("rpg_staty");
			staty.setSuffix("§3》 "+ChatColor.of(new Color(66, 104, 124)).toString() + "" + ChatColor.BOLD + "STATY §3《");
			staty.addEntry(ChatColor.translateAlternateColorCodes('&', "&e"));
			obj.getScore(ChatColor.translateAlternateColorCodes('&', "&e")).setScore(5);
			
//			score = obj.getScore("§3》 §x§0§0§8§0§b§4§lSTATY §3《");
//			score.setScore(5);
			
			switch(ChatColor.stripColor(playerInfo.getProffesion().toLowerCase())) {
			case "wojownik":
					score = obj.getScore("  §bSila: §e"+stats.getFinalSila());
					score.setScore(4);
					score = obj.getScore("  §bWytrzymalosc: §e"+stats.getFinalWytrzymalosc());
					score.setScore(3);
					break;
				case "mag":
					score = obj.getScore("  §bKrag: §e"+stats.getKrag());
					score.setScore(4);
					score = obj.getScore("  §bInteligencja: §e"+stats.getFinalInteligencja());
					score.setScore(3);
					break;
				case "mysliwy":
					score = obj.getScore("  §bZrecznosc: §e"+stats.getFinalZrecznosc());
					score.setScore(4);
					score = obj.getScore("  §bZdolnosci mysliwskie: §e"+stats.getFinalZdolnosci());
					score.setScore(3);
					break;
				default:
					score = obj.getScore("  §bSila: §e"+stats.getFinalSila());
					score.setScore(4);
					score = obj.getScore("  §bWytrzymalosc: §e"+stats.getFinalWytrzymalosc());
					score.setScore(3);
					break;
			}
			
			score = obj.getScore("  §bMana: §9"+stats.getPresentMana()+"§b/§9"+stats.getFinalMana());
			score.setScore(2);
		} else {
			Score score = obj.getScore("§bGracz: §e"+nick);
			score.setScore(15);
			score = obj.getScore("§bKlasa: "+playerInfo.getProffesion());
			score.setScore(14);
			if(playerInfo.getLevel()==95) {
				score = obj.getScore("§bPoziom: §eMAX");
				score.setScore(13);
				score = obj.getScore("§bStygia: §e"+vault.getStygia());
				score.setScore(12);
			} else {
				score = obj.getScore("§bPoziom: §e"+playerInfo.getLevel());
				score.setScore(13);
				score = obj.getScore("§bExp: §e"+playerInfo.getExp()+"§a/§e"+playerInfo.getNextLevel());
				score.setScore(12);
			}
			score = obj.getScore("§bPunkty nauki: §e"+playerInfo.getPn());
			score.setScore(11);
			score = obj.getScore("§bObrazenia: §e"+stats.getObrazenia());
			score.setScore(10);
			score = obj.getScore("§bObrona: §e"+stats.getOchrona());
			score.setScore(9);
			score = obj.getScore("§bSila: §e"+stats.getFinalSila());
			score.setScore(8);
			score = obj.getScore("§bWytrzymalosc: §e"+stats.getFinalWytrzymalosc());
			score.setScore(7);
			score = obj.getScore("§bZrecznosc: §e"+stats.getFinalZrecznosc());
			score.setScore(6);
			score = obj.getScore("§bZdolnosci mysliwskie: §e"+stats.getFinalZdolnosci());
			score.setScore(5);
			score = obj.getScore("§bWalka: §e"+stats.getFinalWalka());
			score.setScore(4);
			score = obj.getScore("§bKrag: §e"+stats.getKrag());
			score.setScore(3);
			score = obj.getScore("§bInteligencja: §e"+stats.getFinalInteligencja());
			score.setScore(2);
			score = obj.getScore("§bMana: §9"+stats.getPresentMana()+"§b/§9"+stats.getFinalMana());
			score.setScore(1);
		}
	}
	
}
