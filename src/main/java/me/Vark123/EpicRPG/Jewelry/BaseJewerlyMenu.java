package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BaseJewerlyMenu implements InventoryHolder {

	private Player viewer;
	private Player owner;
	
	public BaseJewerlyMenu(Player viewer, Player owner) {
		this.viewer = viewer;
		this.owner = owner;
	}
	
	public BaseJewerlyMenu(Player owner) {
		this(owner, owner);
	}
	
	@Override
	public Inventory getInventory() {
		return null;
	}

	public Player getViewer() {
		return viewer;
	}

	public Player getOwner() {
		return owner;
	}

}
