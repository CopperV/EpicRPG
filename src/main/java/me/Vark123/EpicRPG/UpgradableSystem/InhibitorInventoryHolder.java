package me.Vark123.EpicRPG.UpgradableSystem;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InhibitorInventoryHolder implements InventoryHolder {
	
	private UpgradableInhibitor inhibitor;
	private int page;
	
	@Override
	public Inventory getInventory() {
		return null;
	}

}
