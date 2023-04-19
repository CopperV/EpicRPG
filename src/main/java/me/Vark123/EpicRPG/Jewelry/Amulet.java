package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.inventory.ItemStack;

public class Amulet extends JewerlyItem {

	public Amulet(int slot) {
		super(JewerlyType.AMULET, slot);
	}
	
	public Amulet(ItemStack item, int slot) {
		super(JewerlyType.AMULET, item, slot);
	}


}
