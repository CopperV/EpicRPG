package me.Vark123.EpicRPG.Chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Vark123.EpicRPG.Main;

public class ChatClearCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("chatclear"))
			return false;
		if(!sender.hasPermission("rpg.chatclear")) {
			sender.sendMessage(Main.getInstance().getPrefix()
					+" §cNie masz uprawnien do tej komendy");
			return false;
		}
		for(int i = 0; i < 100; ++i) {
			Bukkit.broadcastMessage("");
		}
		Bukkit.broadcastMessage(Main.getInstance().getPrefix()
				+" §e"+sender.getName()+" §awyczyscil czat");
		return true;
	}

}
