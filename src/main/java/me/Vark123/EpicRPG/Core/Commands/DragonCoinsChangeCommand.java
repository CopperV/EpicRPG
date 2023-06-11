package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.internal.flywaydb.core.internal.util.StringUtils;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class DragonCoinsChangeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("dragoncoins")) return false;
		if(!sender.hasPermission("epicrpg.coins")) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie posiadasz uprawnien do tej komendy!");
			return false;
		}
		if(args.length<3) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §aPoprawne uzycie komendy: §c§o/coins add/remove <nick> <ilosc>");
			return false;
		}
		if(Bukkit.getPlayerExact(args[1])==null) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cGracz §a§o"+args[0]+" §cjest offline");
			return false;
		}
		if(!StringUtils.isNumeric(args[2])){
			sender.sendMessage(Main.getInstance().getPrefix()+args[2]+" §cnie jest liczba");
			return false;
		}
		
		Player p = Bukkit.getPlayer(args[1]);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgVault vault = rpg.getVault();
		int cena = Integer.parseInt(args[2]);
		switch(args[0].toLowerCase()) {
		case "add":
			vault.addDragonCoins(cena);
			p.sendMessage(Main.getInstance().getPrefix()+" §aOtrzymales §c§o"+cena+" Smoczych Monet");
			break;
		case "remove":
			vault.removeDragonCoins(cena);
			p.sendMessage(Main.getInstance().getPrefix()+" §aOdebrano Ci §c§o"+cena+" Smoczych Monet");
			break;
		}
		return true;
	}

}
