package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;

public class SprzedajCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("sprzedaj"))
			return false;
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cTylko gracz moze uzyc tej komendy!");
			return false;
		}
		if(args.length < 1) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §c/sprzedaj <cena> [ilosc]");
			return false;
		}
		if(args.length==1) {
			Bukkit.dispatchCommand(sender, "ah sell "+args[0]);
			return true;
		}
		Bukkit.dispatchCommand(sender, "ah sell "+args[0]+" "+args[1]);
		return true;
	}

}
