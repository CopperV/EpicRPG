package me.Vark123.EpicRPG.Core.Commands.SystemCmds;

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

public class DragonCoinsCmdRunCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("coinscmd")) return false;
		if(!sender.hasPermission("epicrpg.coinsbuy")) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie posiadasz uprawnien do tej komendy!");
			return false;
		}
		if(args.length<3) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §aPoprawne uzycie komendy: §c§o/coinscmd <cena> <gracz> <command>");
			return false;
		}
		
		if(Bukkit.getPlayerExact(args[1])==null) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cGracz §a§o"+args[1]+" §cjest offline");
			return false;
		}
		
		if(!StringUtils.isNumeric(args[0])){
			sender.sendMessage(Main.getInstance().getPrefix()+args[0]+" §cnie jest liczba");
			return false;
		}
		int cena = Integer.parseInt(args[0]);
		Player p = Bukkit.getPlayer(args[1]);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgVault vault = rpg.getVault();

		if(!vault.hasEnoughDragonCoins(cena)) {
			p.sendMessage(Main.getInstance().getPrefix()+" §aPosiadasz zbyt malo smoczych monet na to: §3§o"+cena);
			return false;
		}
		vault.removeDragonCoins(cena);
		String command = "";
		for(int i = 2; i < args.length; ++i) {
			command += args[i];
			command += " ";
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		return true;
	}

}
