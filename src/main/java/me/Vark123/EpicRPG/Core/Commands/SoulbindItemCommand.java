package me.Vark123.EpicRPG.Core.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldguard.internal.flywaydb.core.internal.util.StringUtils;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;

public class SoulbindItemCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("soulbindgive"))
			return false;
		if(!sender.hasPermission("epicrpg.sbgive")) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cNie posiadasz uprawnien do tej komendy!");
			return false;
		}
		if(args.length<3) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §aPoprawne uzycie komendy: §c§o/sbgive <nick> <mmItem> <ilosc>");
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
		ItemStack it = manag.getItemStack(args[1]);
		it.setAmount(Integer.parseInt(args[2]));
		
		ItemMeta im = it.getItemMeta();
		List<String> lore;
		if(im.hasLore()) lore = im.getLore();
		else lore = new ArrayList<>();
		lore.add(" ");
		lore.add("§aPrzypisanie: §e§o"+args[0]);
		lore.add(" ");
		im.setLore(lore);
		it.setItemMeta(im);
		
		NBTItem nbtit = new NBTItem(it);
		nbtit.setString("soulbind", args[0]);
		Random rand = new Random();
		nbtit.setInteger("Random"+rand.nextInt(), rand.nextInt());
		nbtit.applyNBT(it);
		
		Player p = Bukkit.getPlayer(args[0]);
		Utils.dropItemStack(p, it);
		sender.sendMessage(Main.getInstance().getPrefix()+" §aDodano "+it.getItemMeta().getDisplayName()+" do ekwipunku "+args[0]);
		
		return true;
	}

}
