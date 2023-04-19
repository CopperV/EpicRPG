package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.inventory.ItemStack;

public class Ring extends JewerlyItem {

	public Ring(ItemStack item, int slot) {
		super(JewerlyType.RING, item, slot);
	}

	public Ring(int slot) {
		super(JewerlyType.RING, slot);
	}

}
