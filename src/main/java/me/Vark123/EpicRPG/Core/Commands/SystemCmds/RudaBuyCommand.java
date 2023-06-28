package me.Vark123.EpicRPG.Core.Commands.SystemCmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.internal.flywaydb.core.internal.util.StringUtils;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class RudaBuyCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("brbuy")) return false;
		if(!sender.hasPermission("epicrpg.rudabuy")) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie posiadasz uprawnien do tej komendy!");
			return false;
		}
		if(args.length<4) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §aPoprawne uzycie komendy: §c§o/brbuy <nick> <mmItem> <ilosc> <cena>");
			return false;
		}
		if(Bukkit.getPlayerExact(args[0])==null) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cGracz §a§o"+args[0]+" §cjest offline");
			return false;
		}
		
		ItemExecutor manag = MythicBukkit.inst().getItemManager();
		if(!manag.getItemNames().contains(args[1])) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cItem §a§o"+args[1]+" §cnie istnieje");
			return false;
		}
		if(!StringUtils.isNumeric(args[2]) || !StringUtils.isNumeric(args[3])){
			sender.sendMessage(Main.getInstance().getPrefix()+(StringUtils.isNumeric(args[2])?args[3]:args[2])+" §cnie jest liczba");
			return false;
		}
		
		Player p = Bukkit.getPlayer(args[0]);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgVault vault = rpg.getVault();
		int cena = Integer.parseInt(args[3]);
		if(!vault.hasEnoughBrylkiRudy(cena)) {
			p.sendMessage(Main.getInstance().getPrefix()+" §aPosiadasz zbyt malo brylek rudy na to: §3§o"+cena);
			return false;
		}
		
		vault.removeBrylkiRudy(cena);
		p.sendMessage(Main.getInstance().getPrefix() + " §aPobrano §9§o"+cena+" brylek rudy");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rpgmm give "+args[0]+" "+args[1]+" "+args[2]);
		return true;
	}

}
