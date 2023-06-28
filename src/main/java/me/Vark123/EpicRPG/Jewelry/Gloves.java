package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.inventory.ItemStack;

public class Gloves extends JewelryItem {

	public Gloves(ItemStack item, int slot) {
		super(JewelryType.GLOVES, item, slot);
	}

	public Gloves(int slot) {
		super(JewelryType.GLOVES, slot);
	}

}
