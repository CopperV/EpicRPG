package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.internal.flywaydb.core.internal.util.StringUtils;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;

public class RpgMMCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("rpgmm")) return false;
		if(!sender.hasPermission("epicrpg.rpgmm")) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie posiadasz uprawnien do tej komendy!");
			return false;
		}
		if(args.length<4) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §aPoprawne uzycie komendy: §c§o/rpgmm give <nick> <mmItem> <ilosc>");
			return false;
		}
		switch(args[0].toLowerCase()) {
			case "give":
				if(Bukkit.getPlayerExact(args[1])==null) {
					sender.sendMessage(Main.getInstance().getPrefix()+" §cGracz §a§o"+args[1]+" §cjest offline");
					return false;
				}
				ItemExecutor manag = MythicBukkit.inst().getItemManager();
				if(!manag.getItemNames().contains(args[2])) {
					sender.sendMessage(Main.getInstance().getPrefix()+" §cItem §a§o"+args[2]+" §cnie istnieje");
					return false;
				}
				if(!StringUtils.isNumeric(args[3])){
					sender.sendMessage(Main.getInstance().getPrefix()+ args[3]+" §cnie jest liczba");
					return false;
				}
				ItemStack it = manag.getItemStack(args[2]);
				it.setAmount(Integer.parseInt(args[3]));
				Player p = Bukkit.getPlayer(args[1]);
				Utils.dropItemStack(p, it);
				sender.sendMessage(Main.getInstance().getPrefix()+" §aDodano "+it.getItemMeta().getDisplayName()+" do ekwipunku "+args[1]);
				break;
		}
		return true;
	}

}
