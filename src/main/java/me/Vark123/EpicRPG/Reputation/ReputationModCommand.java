package me.Vark123.EpicRPG.Reputation;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.internal.flywaydb.core.internal.util.StringUtils;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class ReputationModCommand implements CommandExecutor {

	// /repmod add/remove <nick> <ilosc> <frakcja>
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("reputationmod"))
			return false;
		
		String prefix = Main.getInstance().getPrefix();
		
		if(!sender.hasPermission("epicrpg.gm")) {
			sender.sendMessage(prefix+" §cNie masz uprawnien, by uzyc tej komendy!");
			return false;
		}
		
		if(args.length < 4) {
			sender.sendMessage(prefix+" §cPoprawne uzycie komendy: §c§o/repmod add/remove <nick> <ilosc> <frakcja>");
			return false;
		}
		
		if(Bukkit.getPlayerExact(args[1])==null) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cGracz §a§o"+args[1]+" §cjest offline");
			return false;
		}
		if(!StringUtils.isNumeric(args[2])){
			sender.sendMessage(Main.getInstance().getPrefix()+args[2]+" §cnie jest liczba");
			return false;
		}
		if(!ReputationContainer.getInstance().getContainer().containsKey(args[3].toLowerCase())) {
			sender.sendMessage(Main.getInstance().getPrefix()+"§cFrakcja §r"+args[3]+" §cnie istnieje");
			return false;
		}
		
		Player p = Bukkit.getPlayer(args[1]);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		int cena = Integer.parseInt(args[2]);
		String frakcja = args[3].toLowerCase();
		
		switch(args[0].toLowerCase()) {
			case "add":
				rpg.getReputation().getReputacja().get(frakcja).addSmartReputation(cena);
				sender.sendMessage(prefix+" §7"+p.getName()+" otrzymal §f"+cena+" §7 pkt reputacji do frakcji §r"+rpg.getReputation().getReputacja().get(frakcja).getDisplayFraction());
				return true;
			case "remove":
				sender.sendMessage(prefix+" §7"+p.getName()+" stracil §f"+cena+" §7 pkt reputacji z frakcji §r"+rpg.getReputation().getReputacja().get(frakcja).getDisplayFraction());
				rpg.getReputation().getReputacja().get(frakcja).removeSmartReputation(cena);
				return true;
		}
		
		sender.sendMessage(prefix+" §cPoprawne uzycie komendy: §c§o/repmod add/remove <nick> <ilosc> <frakcja>");
		return false;
	}

}
