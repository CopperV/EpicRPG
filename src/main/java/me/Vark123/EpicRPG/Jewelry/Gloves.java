package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.inventory.ItemStack;

public class Gloves extends JewerlyItem {

	public Gloves(ItemStack item, int slot) {
		super(JewerlyType.GLOVES, item, slot);
	}

	public Gloves(int slot) {
		super(JewerlyType.GLOVES, slot);
	}

}
