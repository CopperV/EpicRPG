package me.Vark123.EpicRPG.Players;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;

public class AdditionalMenuCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("ekwipunek"))
			return false;
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cKomenda tylko dla graczy");
			return false;
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rpgmenu "+sender.getName()+" 21");
		return true;
	}

}
