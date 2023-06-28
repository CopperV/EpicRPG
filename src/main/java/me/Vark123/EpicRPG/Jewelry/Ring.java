package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.inventory.ItemStack;

public class Ring extends JewelryItem {

	public Ring(ItemStack item, int slot) {
		super(JewelryType.RING, item, slot);
	}

	public Ring(int slot) {
		super(JewelryType.RING, slot);
	}

}
