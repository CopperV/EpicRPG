package me.Vark123.EpicRPG.Core.Listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CraftingBlockersListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	private void onPrepareCraft(PrepareItemCraftEvent e) {
		ItemStack result = e.getInventory().getResult();
		
		if(result == null || result.getType().equals(Material.AIR))
			return;
		
		switch(result.getType()) {
			case WIND_CHARGE:
				e.getInventory().setResult(new ItemStack(Material.AIR));
				break;
			default:
		}
	}
	
}
