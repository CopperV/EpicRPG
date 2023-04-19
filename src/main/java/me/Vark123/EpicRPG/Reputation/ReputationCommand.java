package me.Vark123.EpicRPG.Reputation;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class ReputationCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("reputation"))
			return false;
		
		String prefix = Main.getInstance().getPrefix();
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(prefix+" §cTylko gracz moze uzywac tej komendy!");
			return false;
		}
		
		Player tmp = (Player)sender;
		Player p = (Player)sender;
		
		if(args.length > 0) {
			if(Bukkit.getPlayerExact(args[0]).isOnline() && p.hasPermission("epicrpg.gm")) p = Bukkit.getPlayerExact(args[0]);
		}

		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		tmp.sendMessage(prefix+" §2Reputacja gracza §7"+p.getName());
		for(String id : rpg.getReputacja().keySet()) {
			Reputation rep = rpg.getReputacja().get(id);
			tmp.sendMessage("§4> §r"+rep.getDisplayFraction()+"§7: "
					+ "§a"+rep.getReputationLevel().getName()+" "
					+ "§7(§a"+rep.getReputationAmount()+"§7/§a"+rep.getReputationLevel().getAmount()+"§7)");
		}
		
		return true;
	}

}
