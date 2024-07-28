package me.Vark123.EpicRPG.UpgradableSystem.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.UpgradableSystem.InhibitorInventoryHolder;
import me.Vark123.EpicRPG.UpgradableSystem.InhibitorMenuManager;
import me.Vark123.EpicRPG.Utils.Utils;

public class InhibitorInventoryCloseListener implements Listener {

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Inventory inv = e.getView().getTopInventory();
		if(inv == null)
			return;
		InventoryHolder holder = inv.getHolder();
		if(holder == null
				|| !(holder instanceof InhibitorInventoryHolder))
			return;
		
		Player p = (Player) e.getPlayer();
		InhibitorMenuManager.get().getCraftingSlots().forEach(slot -> {
			ItemStack it = inv.getItem(slot);
			if(it == null || it.getType().equals(Material.AIR))
				return;
			Utils.dropItemStack(p, it);
		});
	}
	
}
