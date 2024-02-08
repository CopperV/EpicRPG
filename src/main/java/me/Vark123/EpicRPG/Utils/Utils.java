package me.Vark123.EpicRPG.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;

public class Utils {

	private Utils() {}
	
	public static String convertToClassConvention(String s) {
		String[] tab = s.split(" ");
		StringBuilder toReturn = new StringBuilder(tab[0]);
		for(int i = 1; i < tab.length; ++i) {
			toReturn.append(StringUtils.capitalize(tab[i]));
		}
		return toReturn.toString();
	}
	
	public static Direction getLookingDirection(Location loc, boolean combineDirections) {
		float yaw = loc.getYaw();
		if(yaw < 0)
			yaw += 360;
		double offset = combineDirections ? 22.5 : 0;
		double offset2 = combineDirections ? 0 : 45;
		int divide = combineDirections ? 45 : 90;
		
		int index = ((int)((yaw + offset2) % 360 + offset)) / divide;
		return combineDirections ? Direction.doubleDirection[index] : Direction.Directions[index];
	}
	
	public static double normalizeValue(double min, double max, double value) {
		if(min > max)
			throw new IllegalArgumentException("Minimum value cannot be greater than maximum value. Min: "+min+"; Max: "+max);
		if(value < min)
			return min;
		if(value > max)
			return max;
		return value;
	}
	
	public static double scaleValue(double min1, double max1, double min2, double max2, double value) {
		double percent = (value - min1) / (max1 - min1);
		return percent*(max2 - min2) + min2;
	}
	
	public static boolean isRune(ItemStack item) {
		if(!MythicBukkit.inst().getItemManager().isMythicItem(item))
			return false;
		if(item.getType().name().contains("MUSIC_DISC")) 
			return true;
		else 
			return false;
	}
	
	public static void dropItemStack(Player p, ItemStack it) {
		Inventory inv = p.getInventory();
		int freeSlot = inv.firstEmpty();
		if(freeSlot >= 0 && freeSlot < 36) {
			inv.setItem(freeSlot, it);
		} else {
			p.getWorld().dropItem(p.getLocation(), it);
		}
	}
	
	public static void takeItems(Player p, int slot, int amount) {
		ItemStack it = p.getInventory().getItem(slot);
		if(it == null
				|| it.getType().equals(Material.AIR))
			return;
		if(it.getAmount() < amount) {
			p.getInventory().setItem(slot, new ItemStack(Material.AIR));
		} else {
			it.setAmount(it.getAmount() - amount);
		}
		p.updateInventory();
	}
	
	public static void takeItems(Player p, Inventory inv, int slot, int amount) {
		ItemStack it = inv.getItem(slot);
		if(it == null
				|| it.getType().equals(Material.AIR))
			return;
		if(it.getAmount() < amount) {
			inv.setItem(slot, new ItemStack(Material.AIR));
		} else {
			it.setAmount(it.getAmount() - amount);
		}
		p.updateInventory();
	}
	
	public static void takeItems(Player p, EquipmentSlot slot, int amount) {
		if(slot.equals(EquipmentSlot.HAND)) {
			takeItems(p, p.getInventory().getHeldItemSlot(), amount);
			return;
		}
		takeItems(p, slot.ordinal(), amount);
	}
	
	public static List<Integer> intArrayToList(int[] arr){
		return Arrays.stream(arr).boxed().collect(Collectors.toList());
	}
	
}
