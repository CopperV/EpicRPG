package me.Vark123.EpicRPG.HorseSystem;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.HorseInventory;

public class HorseInventoryEvent implements Listener {
	
	@EventHandler
	public static void onHorseInv(InventoryClickEvent e) {
		if(e.isCancelled())
			return;
		
		Player p = (Player) e.getWhoClicked();
		if(!p.isInsideVehicle())
			return;
		
		Entity vehicle = p.getVehicle();
		if(!(vehicle instanceof AbstractHorse))
			return;
		
		if(!(e.getClickedInventory() instanceof HorseInventory))
			return;
		
		e.setCancelled(true);
	}

}
