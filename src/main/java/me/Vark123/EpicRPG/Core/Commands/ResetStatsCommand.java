package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class ResetStatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("reset"))
			return false;
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.getInstance().getPrefix()+" Tylko gracz moze uzyc tej komendy");
			return false;
		}
		
		Player p = (Player) sender;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(rpg.getInfo().getLevel() > 20) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §c§oMasz za wysoki poziom, by moc uzyc tej komendy.");
			return false;
		}
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rpgmenu "+p.getName()+" 9");	
		return true;
	}

}
