package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.MenuSystem.MenuManager;

public class EpicRPGCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equals("epicrpg"))
			return false;
		if(args.length == 0)
			return false;
		
		switch(args[0].toLowerCase()) {
			case "kupiec":
				if(!(sender instanceof Player))
					return false;
				if(!sender.hasPermission("epicrpg.kupiec")) {
					sender.sendMessage(Main.getInstance().getPrefix()+" §cNie masz uprawnien do uzycia tej komendy");
					return false;
				}
				Player p = (Player) sender;
				MenuManager.getInstance().openMenu(p, 18);
				break;
		}
		
		return true;
	}
	
}
