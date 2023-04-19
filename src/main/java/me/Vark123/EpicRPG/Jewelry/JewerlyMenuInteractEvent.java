package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.ChatColor;

public class JewerlyMenuInteractEvent implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(!e.getInventory().getType().equals(InventoryType.CHEST)) return;
		String title = ChatColor.stripColor(e.getView().getTitle().toLowerCase());
		if(!title.equalsIgnoreCase("bizuteria")) return;
		Inventory inv = e.getClickedInventory();
		if(inv.getHolder() != null && inv.getHolder() instanceof BaseJewerlyMenu 
				&& e.getCurrentItem() != null && e.getCurrentItem().equals(JewerlyMenu.getJewerlyitem())) {
			JewerlyMenu.openMenu((Player) e.getWhoClicked());
			return;
		}
		if(e.getClickedInventory() == null || !e.getClickedInventory().getType().equals(InventoryType.CHEST)) return;
		
		if(!JewerlyMenu.freeSlots.contains(e.getSlot())) {
			e.setCancelled(true);
			return;
		}
	}
	
}
