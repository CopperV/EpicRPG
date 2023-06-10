package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.inventory.ItemStack;

public class Amulet extends JewelryItem {

	public Amulet(int slot) {
		super(JewelryType.AMULET, slot);
	}
	
	public Amulet(ItemStack item, int slot) {
		super(JewelryType.AMULET, item, slot);
	}


}
