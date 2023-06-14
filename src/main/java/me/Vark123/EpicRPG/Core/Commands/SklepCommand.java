package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;

public class SklepCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("sklep"))
			return false;
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cTylko gracz moze uzyc tej komendy!");
			return false;
		}
		if(args.length < 1) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §c/sklep <gracz>");
			return false;
		}
		Bukkit.dispatchCommand(sender, "ah view "+args[0]);
		return true;
	}

}
