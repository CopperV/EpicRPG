package me.Vark123.EpicRPG.MenuSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;

public class MenuCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("rpgmenu"))
			return false;
		if(!sender.hasPermission("epicrpg.gm")) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie posiadasz uprawnien do tej komendy!");
			return false;
		}
		if(args.length<2) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §aPoprawne uzycie komendy: §c§o/rpgmenu <nick> TYP");
			return false;
		}
		if(Bukkit.getPlayerExact(args[0])==null) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cGracz §a§o"+args[0]+" §cjest offline");
			return false;
		}

		Player p = Bukkit.getPlayer(args[0]);
		int id = Integer.parseInt(args[1]);
		
		return MenuManager.getInstance().openMenu(p, id);
	}

}
