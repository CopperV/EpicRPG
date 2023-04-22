package me.Vark123.EpicRPG.Stats;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.RpgStats;

public class StatsCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("staty"))
			return false;
		Player check;
		RpgPlayer rpg;
		boolean self = true;
		
		switch(args.length) {
			case 0:
				if(!(sender instanceof Player)) {
					sender.sendMessage(Main.getInstance().getPrefix()
							+" §cNie jestes graczem - nie mozesz sprawdzic swoich statystyk");
					return false;
				}
				check = (Player) sender;
				rpg = PlayerManager.getInstance().getRpgPlayer(check);
				break;
			default:
				if(Bukkit.getPlayerExact(args[0]) != null) {
					check = Bukkit.getPlayerExact(args[0]);
					self = false;
				} else {
					if(!(sender instanceof Player)) {
						sender.sendMessage(Main.getInstance().getPrefix()
								+" §cNie jestes graczem - nie mozesz sprawdzic swoich statystyk");
						return false;
					}
					check = (Player) sender;
				}
				rpg = PlayerManager.getInstance().getRpgPlayer(check);
		}
		

		RpgStats stats = rpg.getStats();
		RpgPlayerInfo playerInfo = rpg.getInfo();
		String nick = check.getName();

		sender.sendMessage("§6§l========================= ");
		if(self) {
			sender.sendMessage("§2Twoje statystyki:");
		} else {
			sender.sendMessage("§2Statystyki gracza §a" + nick + "§2:");
		}

		sender.sendMessage("§2Poziom: §a" + playerInfo.getLevel());
		sender.sendMessage("§a" + playerInfo.getExp() +" §7xp/§a"+ playerInfo.getNextLevel() +" §7xp");
		sender.sendMessage("§2Punkty nauki: §a" + playerInfo.getPn());
		sender.sendMessage("§2Obrazenia §a" + stats.getObrazenia() + "§7/§a" + stats.getPotionObrazenia() + "§7/§a" + stats.getFinalObrazenia());
		sender.sendMessage("§2Obrona §a" + stats.getOchrona() + "§7/§a" + stats.getPotionOchrona() + "§7/§a" + stats.getFinalOchrona());
		sender.sendMessage("§2Sila: §a" + stats.getSila() + "§7/§a" + stats.getPotionSila() + "§7/§a" + stats.getFinalSila());
		sender.sendMessage("§2Zrecznosc: §a" + stats.getZrecznosc() + "§7/§a" + stats.getPotionZrecznosc() + "§7/§a" + stats.getFinalZrecznosc());
		sender.sendMessage("§2Wytrzymalosc: §a" + stats.getWytrzymalosc() + "§7/§a" + stats.getPotionWytrzymalosc() + "§7/§a" + stats.getFinalWytrzymalosc());
		sender.sendMessage("§2Zdolnosci mysliwskie: §a" + stats.getZdolnosci() + "§7/§a" + stats.getPotionZdolnosci() + "§7/§a" + stats.getFinalZdolnosci());
		sender.sendMessage("§2Walka: §a" + stats.getWalka() + "§7/§a" + stats.getPotionWalka() + "§7/§a" + stats.getFinalWalka());
		if(playerInfo.getShortProf().toLowerCase().contains("mys")) {
			sender.sendMessage("§2Krytyk: §a" + String.format("%.1f",stats.getFinalWalka()/5.0+10)+"%");
		}else {
			sender.sendMessage("§2Krytyk: §a" + String.format("%.1f",stats.getFinalWalka()/5.0)+"%");
		}
		sender.sendMessage("§2Krag: §a" + stats.getKrag());
		sender.sendMessage("§2Mana: §a"+stats.getPresentMana()+" §7/§2(§a" + stats.getMana() + "§7/§a" + stats.getPotionMana() + "§7/§a" + stats.getFinalMana()+"§2)");
		sender.sendMessage("§2Inteligencja: §a" + stats.getInteligencja() + "§7/§a" + stats.getPotionInteligencja() + "§7/§a" + stats.getFinalInteligencja());
		
		sender.sendMessage("§6§l========================= ");
		
		return true;
	}

}
