package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class LevelCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("level"))
			return false;
		
		RpgPlayer rpg;
		switch(args.length) {
			case 0:
				if(!(sender instanceof Player)) {
					sender.sendMessage(Main.getInstance().getPrefix()+" Bedac na konsoli nie mozesz sprawdzic swoich statystyk");
					return false;
				}
				{
					Player p = (Player) sender;
					rpg = PlayerManager.getInstance().getRpgPlayer(p);
				}
				break;
			default:
				if(Bukkit.getPlayerExact(args[0])==null) {
					sender.sendMessage(Main.getInstance().getPrefix()+" §cGracz §a§o"+args[0]+" §cjest offline");
					return false;
				}
				{
					Player p = (Player) Bukkit.getPlayer(args[0]);
					rpg = PlayerManager.getInstance().getRpgPlayer(p);
				}
		}
		RpgPlayerInfo info = rpg.getInfo();
		sender.sendMessage("§bInfo o §r"+rpg.getPlayer().getName()+"§b:");
		sender.sendMessage("§2Poziom: §a" + info.getLevel());
		sender.sendMessage("§7[§a" + info.getExp() +" xp§7/§a"+ info.getNextLevel() +" xp§7]");
		return true;
	}

}
