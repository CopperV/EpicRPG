package me.Vark123.EpicRPG.Core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.Vark123.EpicRPG.Main;

public class KoszCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("kosz"))
			return false;
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.getInstance().getPrefix()+" §cTylko gracz moze uzyc tej komendy!");
			return false;
		}
		
		Player p = (Player) sender;
		Inventory inv = Bukkit.createInventory(null, 54, "§7§lKOSZ");
		p.openInventory(inv);
		
		return true;
	}

}
