package me.Vark123.EpicRPG.Core.Commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.internal.flywaydb.core.internal.util.StringUtils;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;

public class UniquetemCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("uniquegive"))
			return false;
		if(!sender.hasPermission("epicrpg.ugive")) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie posiadasz uprawnien do tej komendy!");
			return false;
		}
		if(args.length<3) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §aPoprawne uzycie komendy: §c§o/ugive <nick> <mmItem> <ilosc>");
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
		if(!StringUtils.isNumeric(args[2])){
			sender.sendMessage(Main.getInstance().getPrefix()+ args[2]+" §cnie jest liczba");
			return false;
		}
		Player p = Bukkit.getPlayer(args[0]);
		ItemStack it = manag.getItemStack(args[1]);
		it.setAmount(Integer.parseInt(args[2]));
		
		NBTItem nbtit = new NBTItem(it);
		Random rand = new Random();
		nbtit.setInteger("Random"+rand.nextInt(), rand.nextInt());
		nbtit.applyNBT(it);
		

		Utils.dropItemStack(p, it);
		sender.sendMessage(Main.getInstance().getPrefix()+" §aDodano "+it.getItemMeta().getDisplayName()+" do ekwipunku "+args[0]);
		return true;
	}

}
