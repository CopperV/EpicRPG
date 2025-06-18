package me.Vark123.EpicRPG.Core.Commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Stats.ChangeStats;

public class StatModCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("epicstat"))
			return false;
		
		if(args.length < 4) {
			sendCorrectUsage(sender);
			return false;
		}
		
		if(Bukkit.getPlayer(args[1]) == null) {
			sendCorrectUsage(sender);
			return false;
		}
		
		if(!StringUtils.isNumeric(args[3]) || args[3].contains(".")) {
			sendCorrectUsage(sender);
			return false;
		}
		
		int amount = Integer.parseInt(args[3]);
		switch(args[0].toLowerCase()) {
			case "add":
				break;
			case "remove":
				amount *= -1;
				break;
			default:
				sendCorrectUsage(sender);
				return false;
		}
		
		if(!addStat(Bukkit.getPlayer(args[1]), args[2], amount)) {
			sendCorrectUsage(sender);
			return false;
		}
		return true;
	}
	
	private void sendCorrectUsage(CommandSender sender) {
		sender.sendMessage(Main.getInstance().getPrefix()+" §cPoprawne uzycie komendy §o/epicstat");
		sender.sendMessage("§4§o/epicstat add/remove [user] [stat] [amount]");
	}
	
	private boolean addStat(Player target, String name, int amount) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(target);
		RpgStats stats = rpg.getStats();
		String message = Main.getInstance().getPrefix()+" §a%stat_name% "
				+(amount < 0 ? amount : "+"+amount)
				+" §7[§a%stat_amount%§7]";
		
		switch(name.toLowerCase()) {
			case "str":
				stats.addPotionSila(amount);
				ChangeStats.change(rpg);
				message = message.replace("%stat_name%", "Sila");
				message = message.replace("%stat_amount%", (stats.getSila() + stats.getPotionSila())+"");
				break;
			case "wytrz":
				stats.addPotionWytrzymalosc(amount);
				ChangeStats.change(rpg);
				message = message.replace("%stat_name%", "Wytrzymalosc");
				message = message.replace("%stat_amount%", (stats.getWytrzymalosc()+stats.getPotionWytrzymalosc())+"");
				break;
			case "zr":
				stats.addPotionZrecznosc(amount);
				ChangeStats.change(rpg);
				message = message.replace("%stat_name%", "Zrecznosc");
				message = message.replace("%stat_amount%", (stats.getZrecznosc() + stats.getPotionZrecznosc())+"");
				break;
			case "zd":
				stats.addPotionZdolnosci(amount);
				ChangeStats.change(rpg);
				message = message.replace("%stat_name%", "Zdolnosci Mysliwskie");
				message = message.replace("%stat_amount%", (stats.getZdolnosciMysliwskie()+stats.getPotionZdolnosciMysliwskie())+"");
				break;
			case "int":
				stats.addPotionInteligencja(amount);
				ChangeStats.change(rpg);
				message = message.replace("%stat_name%", "Inteligencja");
				message = message.replace("%stat_amount%", (stats.getInteligencja()+stats.getPotionInteligencja())+"");
				break;
			case "mana":
				stats.addPotionMana(amount);
				ChangeStats.change(rpg);
				message = message.replace("%stat_name%", "Mana");
				message = message.replace("%stat_amount%", (stats.getMana()+stats.getPotionMana())+"");
				break;
			case "walka":
				stats.addPotionWalka(amount);
				ChangeStats.change(rpg);
				message = message.replace("%stat_name%", "Walka");
				message = message.replace("%stat_amount%", (stats.getWalka()+stats.getPotionWalka())+"");
				break;
			case "dmg":
				stats.addPotionObrazenia(amount);
				ChangeStats.change(rpg);
				message = message.replace("%stat_name%", "Obrazenia");
				message = message.replace("%stat_amount%", (stats.getObrazenia()+stats.getPotionObrazenia())+"");
				break;
			case "def":
				stats.addPotionOchrona(amount);
				ChangeStats.change(rpg);
				message = message.replace("%stat_name%", "Ochrona");
				message = message.replace("%stat_amount%", (stats.getOchrona()+stats.getPotionOchrona())+"");
				break;
			default:
				return false;
		}
		
		target.sendMessage(message);
		return true;
	}

}
