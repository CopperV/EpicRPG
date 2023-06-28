package me.Vark123.EpicRPG.Stats;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class StatsCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("staty"))
			return false;
		Player check;
		RpgPlayer rpg;
		
		switch(args.length) {
			case 0:
				if(!(sender instanceof Player)) {
					sender.sendMessage(Main.getInstance().getPrefix()
							+" §cNie jestes graczem - nie mozesz sprawdzic swoich statystyk");
					return false;
				}
				check = (Player) sender;
				rpg = PlayerManager.getInstance().getRpgPlayer(check);
				break;
			default:
				if(Bukkit.getPlayerExact(args[0]) != null) {
					check = Bukkit.getPlayerExact(args[0]);
				} else {
					if(!(sender instanceof Player)) {
						sender.sendMessage(Main.getInstance().getPrefix()
								+" §cNie jestes graczem - nie mozesz sprawdzic swoich statystyk");
						return false;
					}
					check = (Player) sender;
				}
				rpg = PlayerManager.getInstance().getRpgPlayer(check);
		}
		
		rpg.print(sender);
		
		return true;
	}

}
