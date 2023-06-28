package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.inventory.ItemStack;

public abstract class JewelryItem {

	private JewelryType type;
	private ItemStack item;
	private int slot;

	public JewelryItem(JewelryType type, ItemStack item, int slot) {
		super();
		this.type = type;
		this.item = item;
		this.slot = slot;
	}

	public JewelryItem(JewelryType type, int slot) {
		super();
		this.type = type;
		this.slot = slot;
	}
	
	public JewelryType getType() {
		return type;
	}
	public void setItem(ItemStack item) {
		this.item = item;
	}
	public ItemStack getItem() {
		return item;
	}
	public int getSlot() {
		return slot;
	}
	
}
