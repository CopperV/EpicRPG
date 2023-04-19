package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class JewerlyMenuOpenEvent implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
//		p.sendMessage(e.getClickedInventory().getType().name()+" "+e.getSlot());
		if(e.getClickedInventory() == null)
			return;
		if(!e.getClickedInventory().getType().equals(InventoryType.CRAFTING))
			return;
		if(e.getSlot() != 3)
			return;
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rpgmenu "+p.getName()+" 15");
	}
	
}
