package me.Vark123.EpicRPG.Core.Commands.SystemCmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.internal.flywaydb.core.internal.util.StringUtils;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Core.StygiaSystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class StygiaChangeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("stygiamod")) return false;
		if(!sender.hasPermission("epicrpg.stygia")) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie posiadasz uprawnien do tej komendy!");
			return false;
		}
		if(args.length<3) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §aPoprawne uzycie komendy: §c§o/stygiamod add/remove <nick> <ilosc>");
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
			StygiaSystem.getInstance().addStygia(rpg, cena, "cmd");
			p.sendMessage(Main.getInstance().getPrefix()+" §aOtrzymales §9§o"+cena+" Stygii");
			break;
		case "remove":
			vault.removeStygia(cena);
			p.sendMessage(Main.getInstance().getPrefix()+" §aOdebrano Ci §9§o"+cena+" Stygii");
			break;
		}
		return true;
	}

}
