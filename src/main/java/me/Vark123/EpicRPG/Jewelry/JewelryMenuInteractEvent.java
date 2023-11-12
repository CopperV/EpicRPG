package me.Vark123.EpicRPG.Jewelry;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import me.Vark123.EpicRPG.Utils.Utils;
import net.md_5.bungee.api.ChatColor;

@Deprecated
public class JewelryMenuInteractEvent implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getType().equals(InventoryType.CHEST)) return;
		String title = ChatColor.stripColor(e.getView().getTitle().toLowerCase());
		if(!title.equalsIgnoreCase("bizuteria")) return;
		if(e.getClickedInventory() == null || !e.getClickedInventory().getType().equals(InventoryType.CHEST)) return;
		
		List<Integer> tmpList = Utils.intArrayToList(JewelryMenuManager.getInstance().getFreeSlots());
		if(!tmpList.contains(e.getSlot())) {
			e.setCancelled(true);
			return;
		}
	}
	
}
