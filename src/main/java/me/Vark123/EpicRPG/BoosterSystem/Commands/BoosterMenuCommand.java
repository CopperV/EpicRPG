package me.Vark123.EpicRPG.BoosterSystem.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.BoosterSystem.MenuSystem.BoosterMenuManager;

public class BoosterMenuCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("modyfikatory"))
			return false;
		if(!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		BoosterMenuManager.get().openMenu(p);
		return true;
	}

}
