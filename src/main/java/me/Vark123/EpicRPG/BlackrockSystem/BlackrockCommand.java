package me.Vark123.EpicRPG.BlackrockSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;

public class BlackrockCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("blackrock"))
			return false;
		if(!sender.hasPermission("epicrpg.admin")) {
			return false;
		}
		if(args.length < 2) {
			showBadUsage(sender);
			return false;
		}

		if(Bukkit.getPlayerExact(args[0])==null) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cGracz §a§o"+args[0]+" §cjest offline");
			return false;
		}
		Player p = Bukkit.getPlayerExact(args[0]);
		BlackRockOperations operation = BlackRockOperations.valueOf(args[1].toUpperCase());
		BlackrockEvent event = new BlackrockEvent(p, operation);
		Bukkit.getPluginManager().callEvent(event);
		return true;
	}
	
	private void showBadUsage(CommandSender sender) {
		sender.sendMessage(Main.getInstance().getPrefix()+" §c§o/blakrock <nick> <ADD/REMOVE/ADD_ALL/REMOVE_ALL");
	}

}
