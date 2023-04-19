package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.inventory.ItemStack;

public abstract class JewerlyItem {

	private JewerlyType type;
	private ItemStack item;
	private int slot;

	public JewerlyItem(JewerlyType type, ItemStack item, int slot) {
		super();
		this.type = type;
		this.item = item;
		this.slot = slot;
	}

	public JewerlyItem(JewerlyType type, int slot) {
		super();
		this.type = type;
		this.slot = slot;
	}
	
	public JewerlyType getType() {
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
