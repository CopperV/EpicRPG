package me.Vark123.EpicRPG.Jewelry;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class JewerlyMenu {
	
	private static final String title = "§6§lBizuteria";
	private static final int slots = 18;
	private static final ItemStack baseAmulet = new ItemStack(Material.FLINT, 1);
	private static final ItemStack baseRing = new ItemStack(Material.DIAMOND, 1);
	private static final ItemStack baseGloves = new ItemStack(Material.BUCKET, 1);
	private static final ItemStack pole = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
	private static final ItemStack jewerlyItem = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
	public static final List<Integer> freeSlots = Arrays.asList(10,12,14,16);
	
	static {
		{
			ItemMeta im = baseAmulet.getItemMeta();
			im.setDisplayName("§e§lAMULET");
			baseAmulet.setItemMeta(im);
		}
		{
			ItemMeta im = baseRing.getItemMeta();
			im.setDisplayName("§6§lPIERSCIEN");
			baseRing.setItemMeta(im);
		}
		{
			ItemMeta im = baseGloves.getItemMeta();
			im.setDisplayName("§c§lREKAWICE");
			baseGloves.setItemMeta(im);
		}
		{
			ItemMeta im = pole.getItemMeta();
			im.setDisplayName(" ");
			pole.setItemMeta(im);
		}
		{
			ItemMeta im = jewerlyItem.getItemMeta();
			im.setDisplayName("§6§lBizuteria");
			jewerlyItem.setItemMeta(im);
		}
	}

	public static void openBaseMenu(Player p) {
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		Inventory inv = Bukkit.createInventory(new BaseJewerlyMenu(p), 9, title);
		p.openInventory(inv);
		
		for(int slot = 0; slot < 9; ++slot) {
			inv.setItem(slot, pole);
		}
		inv.setItem(4, jewerlyItem);
	}
	
	public static void openMenu(Player p) {
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		Inventory inv = Bukkit.createInventory(null, slots, title);
		for(int slot = 0; slot < slots; ++slot) {
			if(freeSlots.contains(slot))
				continue;
			inv.setItem(slot, pole);
		}
		inv.setItem(1, baseAmulet);
		inv.setItem(3, baseRing);
		inv.setItem(5, baseRing);
		inv.setItem(7, baseGloves);
		
		rpg.getJewelry().getAkcesoria().forEach((i, item) -> {
			if(item == null)
				return;
			inv.setItem(10+i*2, item.getItem());
		});
		
		p.openInventory(inv);
		
	}

	public static ItemStack getJewerlyitem() {
		return jewerlyItem;
	}
	
}
