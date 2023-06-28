package me.Vark123.EpicRPG.Chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Vark123.EpicRPG.Main;

public class ChatToggleCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("chat"))
			return false;
		if(!sender.hasPermission("rpg.chatonoff")) {
			sender.sendMessage(Main.getInstance().getPrefix()
					+" §cNie masz uprawnien do tej komendy");
			return false;
		}
		boolean state = !ChatManager.getInstance().isChatToggle();
		if(args.length > 0)
			switch(args[0].toLowerCase()) {
				case "on":
					state = true;
					break;
				case "off":
					state = false;
					break;
				}
		ChatManager.getInstance().setChatToggle(state);
		Bukkit.broadcastMessage(Main.getInstance().getPrefix()
				+" §e"+sender.getName()+" §e"+(state ? "wlaczyl" : "wylaczyl")+" czat");
		return true;
	}

}
