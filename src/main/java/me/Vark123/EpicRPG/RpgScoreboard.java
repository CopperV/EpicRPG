package me.Vark123.EpicRPG;

import java.awt.Color;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
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
		Objective obj = board.registerNewObjective("test", "dummy", ChatColor.of(new Color(132, 165, 184)).toString()+""+ChatColor.BOLD+"Archolos §9§l§o#1");
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
		
		setScore(rpg, board);
	}
	
	public static void updateScore(Player p) {
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		Scoreboard board = rpg.getBoard();

//		board.clearSlot(DisplaySlot.SIDEBAR);
//		if(board.getEntries() != null && !board.getEntries().isEmpty())
		board.getEntries().stream().forEach(s -> {
			board.resetScores(s);
		});
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
			team.setSuffix("§3》 "+ChatColor.of(new Color(66, 104, 124)).toString() + "" + ChatColor.BOLD + "INFO §3《");
			team.addEntry(ChatColor.BLUE+""+ChatColor.WHITE);
			obj.getScore(ChatColor.BLUE+""+ChatColor.WHITE).setScore(15);
			
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
			team = board.getTeam("rpg_nick");
			team.setSuffix(klasa);
			team.addEntry(ChatColor.YELLOW+""+ChatColor.WHITE);
			obj.getScore(ChatColor.YELLOW+""+ChatColor.WHITE).setScore(14);
			
			String poziom = "  §bPoziom: §e";
			if(playerInfo.getLevel() == 95)
				poziom += "MAX";
			else
				poziom += playerInfo.getLevel();
			team = board.getTeam("rpg_level");
			team.setSuffix(poziom);
			team.addEntry(ChatColor.YELLOW+""+ChatColor.GREEN);
			obj.getScore(ChatColor.YELLOW+""+ChatColor.GREEN).setScore(13);
			
			String exp = "  §bExp: §e";
			if(playerInfo.getLevel() == 95)
				exp += "MAX";
			else 
				exp += (playerInfo.getExp()+"§a/§e"+playerInfo.getNextLevel());
			team = board.getTeam("rpg_exp");
			team.setSuffix(exp);
			team.addEntry(ChatColor.YELLOW+""+ChatColor.GRAY);
			obj.getScore(ChatColor.YELLOW+""+ChatColor.GRAY).setScore(12);
			
			String pn = "  §bPunkty nauki: §e"+playerInfo.getPn();
			team = board.getTeam("rpg_pn");
			team.setSuffix(pn);
			team.addEntry(ChatColor.YELLOW+""+ChatColor.LIGHT_PURPLE);
			obj.getScore(ChatColor.YELLOW+""+ChatColor.LIGHT_PURPLE).setScore(11);
			
			team = board.getTeam("rpg_zasoby");
			team.setSuffix("§3》 "+ChatColor.of(new Color(66, 104, 124)).toString() + "" + ChatColor.BOLD + "ZASOBY §3《");
			team.addEntry(ChatColor.BLUE+""+ChatColor.GRAY);
			obj.getScore(ChatColor.BLUE+""+ChatColor.GRAY).setScore(10);
			
			team = board.getTeam("rpg_money");
			team.setSuffix("  §bSaldo: §e"+((int)Main.eco.getBalance(rpg.getPlayer()))+"$");
			team.addEntry(ChatColor.GOLD+""+ChatColor.GRAY);
			obj.getScore(ChatColor.GOLD+""+ChatColor.GRAY).setScore(9);
			
			team = board.getTeam("rpg_stygia");
			team.setSuffix("  §bStygia: §3"+vault.getStygia());
			team.addEntry(ChatColor.GOLD+""+ChatColor.YELLOW);
			obj.getScore(ChatColor.GOLD+""+ChatColor.YELLOW).setScore(8);
			
			team = board.getTeam("rpg_coins");
			team.setSuffix("  §bSmocze monety: §4"+vault.getDragonCoins());
			team.addEntry(ChatColor.GOLD+""+ChatColor.RED);
			obj.getScore(ChatColor.GOLD+""+ChatColor.RED).setScore(7);
			
			team = board.getTeam("rpg_brylki");
			team.setSuffix("  §bBrylki rudy: §9"+vault.getBrylkiRudy());
			team.addEntry(ChatColor.GOLD+""+ChatColor.BLUE);
			obj.getScore(ChatColor.GOLD+""+ChatColor.BLUE).setScore(6);

			team = board.getTeam("rpg_staty");
			team.setSuffix("§3》 "+ChatColor.of(new Color(66, 104, 124)).toString() + "" + ChatColor.BOLD + "STATY §3《");
			team.addEntry(ChatColor.BLUE+""+ChatColor.BLACK);
			obj.getScore(ChatColor.BLUE+""+ChatColor.BLACK).setScore(5);
			
			switch(ChatColor.stripColor(playerInfo.getProffesion().toLowerCase())) {
			case "wojownik":
					team = board.getTeam("rpg_stat1");
					team.setSuffix("  §bSila: §e"+stats.getFinalSila());
					team.addEntry(ChatColor.RED+""+ChatColor.GREEN);
					obj.getScore(ChatColor.RED+""+ChatColor.GREEN).setScore(4);
					
					team = board.getTeam("rpg_stat2");
					team.setSuffix("  §bWytrzymalosc: §e"+stats.getFinalWytrzymalosc());
					team.addEntry(ChatColor.RED+""+ChatColor.YELLOW);
					obj.getScore(ChatColor.RED+""+ChatColor.YELLOW).setScore(3);
					break;
				case "mag":
					team = board.getTeam("rpg_stat1");
					team.setSuffix("  §bKrag: §e"+stats.getKrag());
					team.addEntry(ChatColor.RED+""+ChatColor.GREEN);
					obj.getScore(ChatColor.RED+""+ChatColor.GREEN).setScore(4);
					
					team = board.getTeam("rpg_stat2");
					team.setSuffix("  §bInteligencja: §e"+stats.getFinalInteligencja());
					team.addEntry(ChatColor.RED+""+ChatColor.YELLOW);
					obj.getScore(ChatColor.RED+""+ChatColor.YELLOW).setScore(3);
					break;
				case "mysliwy":
					team = board.getTeam("rpg_stat1");
					team.setSuffix("  §bZrecznosc: §e"+stats.getFinalZrecznosc());
					team.addEntry(ChatColor.RED+""+ChatColor.GREEN);
					obj.getScore(ChatColor.RED+""+ChatColor.GREEN).setScore(4);
					
					team = board.getTeam("rpg_stat2");
					team.setSuffix("  §bZdolnosci mysliwskie: §e"+stats.getFinalZdolnosci());
					team.addEntry(ChatColor.RED+""+ChatColor.YELLOW);
					obj.getScore(ChatColor.RED+""+ChatColor.YELLOW).setScore(3);
					break;
				default:
					team = board.getTeam("rpg_stat1");
					team.setSuffix("  §bSila: §e"+stats.getFinalSila());
					team.addEntry(ChatColor.RED+""+ChatColor.GREEN);
					obj.getScore(ChatColor.RED+""+ChatColor.GREEN).setScore(4);
					
					team = board.getTeam("rpg_stat2");
					team.setSuffix("  §bWytrzymalosc: §e"+stats.getFinalWytrzymalosc());
					team.addEntry(ChatColor.RED+""+ChatColor.YELLOW);
					obj.getScore(ChatColor.RED+""+ChatColor.YELLOW).setScore(3);
					break;
			}
			
			team = board.getTeam("rpg_stat3");
			team.setSuffix("  §bMana: §9"+stats.getPresentMana()+"§b/§9"+stats.getFinalMana());
			team.addEntry(ChatColor.RED+""+ChatColor.BLUE);
			obj.getScore(ChatColor.RED+""+ChatColor.BLUE).setScore(2);
			
		} else {
			Team team = board.getTeam("rpg_nick");
			team.setSuffix("§bGracz: §e"+nick);
			team.addEntry(ChatColor.AQUA+""+ChatColor.WHITE);
			obj.getScore(ChatColor.AQUA+""+ChatColor.WHITE).setScore(15);
			
			team = board.getTeam("rpg_klasa");
			team.setSuffix("§bKlasa: "+playerInfo.getProffesion());
			team.addEntry(ChatColor.AQUA+""+ChatColor.BLACK);
			obj.getScore(ChatColor.AQUA+""+ChatColor.BLACK).setScore(14);
			
			String poziom = "  §bPoziom: §e";
			if(playerInfo.getLevel() == 95)
				poziom += "MAX";
			else
				poziom += playerInfo.getLevel();
			String exp = "  §bExp: §e";
			if(playerInfo.getLevel() == 95)
				exp += "MAX";
			else 
				exp += (playerInfo.getExp()+"§a/§e"+playerInfo.getNextLevel());

			team = board.getTeam("rpg_level");
			team.setSuffix(poziom);
			team.addEntry(ChatColor.AQUA+""+ChatColor.BLUE);
			obj.getScore(ChatColor.AQUA+""+ChatColor.BLUE).setScore(13);
			team = board.getTeam("rpg_exp");
			team.setSuffix(exp);
			team.addEntry(ChatColor.AQUA+""+ChatColor.DARK_AQUA);
			obj.getScore(ChatColor.AQUA+""+ChatColor.DARK_AQUA).setScore(12);
			
			team = board.getTeam("rpg_pn");
			team.setSuffix("§bPunkty nauki: §e"+playerInfo.getPn());
			team.addEntry(ChatColor.AQUA+""+ChatColor.DARK_BLUE);
			obj.getScore(ChatColor.AQUA+""+ChatColor.DARK_BLUE).setScore(11);
			
			team = board.getTeam("rpg_dmg");
			team.setSuffix("§bObrazenia: §e"+stats.getFinalObrazenia());
			team.addEntry(ChatColor.AQUA+""+ChatColor.DARK_GRAY);
			obj.getScore(ChatColor.AQUA+""+ChatColor.DARK_GRAY).setScore(10);
			
			team = board.getTeam("rpg_def");
			team.setSuffix("§bObrona: §e"+stats.getFinalOchrona());
			team.addEntry(ChatColor.AQUA+""+ChatColor.DARK_GREEN);
			obj.getScore(ChatColor.AQUA+""+ChatColor.DARK_GREEN).setScore(9);
			
			team = board.getTeam("rpg_str");
			team.setSuffix("§bSila: §e"+stats.getFinalSila());
			team.addEntry(ChatColor.AQUA+""+ChatColor.DARK_PURPLE);
			obj.getScore(ChatColor.AQUA+""+ChatColor.DARK_PURPLE).setScore(8);
			
			team = board.getTeam("rpg_wytrz");
			team.setSuffix("§bWytrzymalosc: §e"+stats.getFinalWytrzymalosc());
			team.addEntry(ChatColor.AQUA+""+ChatColor.DARK_RED);
			obj.getScore(ChatColor.AQUA+""+ChatColor.DARK_RED).setScore(7);
			
			team = board.getTeam("rpg_zr");
			team.setSuffix("§bZrecznosc: §e"+stats.getFinalZrecznosc());
			team.addEntry(ChatColor.AQUA+""+ChatColor.GOLD);
			obj.getScore(ChatColor.AQUA+""+ChatColor.GOLD).setScore(6);
			
			team = board.getTeam("rpg_zd");
			team.setSuffix("§bZdolnosci mysliwskie: §e"+stats.getFinalZdolnosci());
			team.addEntry(ChatColor.AQUA+""+ChatColor.GRAY);
			obj.getScore(ChatColor.AQUA+""+ChatColor.GRAY).setScore(5);
			
			team = board.getTeam("rpg_walka");
			team.setSuffix("§bWalka: §e"+stats.getFinalWalka());
			team.addEntry(ChatColor.AQUA+""+ChatColor.GREEN);
			obj.getScore(ChatColor.AQUA+""+ChatColor.GREEN).setScore(4);
			
			team = board.getTeam("rpg_krag");
			team.setSuffix("§bKrag: §e"+stats.getKrag());
			team.addEntry(ChatColor.AQUA+""+ChatColor.LIGHT_PURPLE);
			obj.getScore(ChatColor.AQUA+""+ChatColor.LIGHT_PURPLE).setScore(3);
			
			team = board.getTeam("rpg_int");
			team.setSuffix("§bInteligencja: §e"+stats.getFinalInteligencja());
			team.addEntry(ChatColor.AQUA+""+ChatColor.RED);
			obj.getScore(ChatColor.AQUA+""+ChatColor.RED).setScore(2);
			
			team = board.getTeam("rpg_mana");
			team.setSuffix("§bMana: §9"+stats.getPresentMana()+"§b/§9"+stats.getFinalMana());
			team.addEntry(ChatColor.AQUA+""+ChatColor.WHITE);
			obj.getScore(ChatColor.AQUA+""+ChatColor.WHITE).setScore(1);
			
		}
		rpg.getPlayer().setScoreboard(board);
	}
	
}
