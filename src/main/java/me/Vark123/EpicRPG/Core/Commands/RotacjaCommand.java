package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RotacjaCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("rotacja")) 
			return false;
		if(!(sender instanceof Player)) 
			return false;
		if(!sender.hasPermission("epicrpg.kupiec")) {
			sender.sendMessage("Unknown command. Type \"/help\" for help.");
			return false;
		}
		Player p = (Player) sender;
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chestcommands open rotacja "+p.getName());
		return true;
	}

}
