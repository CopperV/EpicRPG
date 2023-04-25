package me.Vark123.EpicRPG.Stats;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.Players.RpgJewelry;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.RpgStats;
import me.Vark123.EpicRPG.Utils.Utils;

public class ChangeStats {
	
	private static final String STR_GET_METHOD = "getFinal";
	private static final String STR_SET_METHOD = "setFinal";

	public static void change(RpgPlayer rpg) {
		change(rpg, rpg.getPlayer().getInventory().getItemInMainHand());
	}

	public static void change(RpgPlayer rpg, ItemStack weapon) {
		Player p = rpg.getPlayer();
		
		RpgJewelry jewelry = rpg.getJewelry();
		RpgStats stats = rpg.getStats();
		stats.setFinalInteligencja(stats.getInteligencja()+stats.getPotionInteligencja());
		stats.setFinalSila(stats.getSila()+stats.getPotionSila());
		stats.setFinalWytrzymalosc(stats.getWytrzymalosc()+stats.getPotionWytrzymalosc());
		stats.setFinalZrecznosc(stats.getZrecznosc()+stats.getPotionZrecznosc());
		stats.setFinalZdolnosci(stats.getZdolnosci()+stats.getPotionZdolnosci());
		stats.setFinalMana(stats.getMana()+stats.getPotionMana());
		stats.setFinalWalka(stats.getWalka()+stats.getPotionWalka());
		stats.setFinalOchrona(stats.getOchrona() + stats.getPotionOchrona());
		stats.setFinalObrazenia(stats.getFinalObrazenia() + stats.getPotionObrazenia());
		
		boolean weaponCheck = (weapon != null);

		Map<Integer,ItemStack> itemy = new ConcurrentHashMap<>();
		Map<Integer,ItemStack> akcesoria = new ConcurrentHashMap<>();

		if((p.getEquipment().getHelmet() != null) && (p.getEquipment().getHelmet().getItemMeta().hasLore()))
			itemy.put(39, p.getEquipment().getHelmet());
		if((p.getEquipment().getChestplate() != null) && (p.getEquipment().getChestplate().getItemMeta().hasLore()))
			itemy.put(38, p.getEquipment().getChestplate());
		if((p.getEquipment().getLeggings() != null) && (p.getEquipment().getLeggings().getItemMeta().hasLore()))
			itemy.put(37, p.getEquipment().getLeggings());
		if((p.getEquipment().getBoots() != null) && (p.getEquipment().getBoots().getItemMeta().hasLore()))
			itemy.put(36, p.getEquipment().getBoots());
		
		jewelry.getAkcesoria().forEach((i, jewItem) -> {
			ItemStack tmp = jewItem.getItem();
			if(tmp == null || tmp.getType().equals(Material.AIR) || !tmp.hasItemMeta() || !tmp.getItemMeta().hasLore())
				return;
			itemy.put(i, tmp);
			akcesoria.put(1000+i, tmp);
		});
		
		
		ItemStack off = p.getInventory().getItemInOffHand();
		if(off!=null && !off.getType().equals(Material.AIR)) {
			NBTItem nbti = new NBTItem(off);
			if(nbti.hasTag("RPGType") && nbti.getString("RPGType").equalsIgnoreCase("gem"))
				changeStatsItem(stats, off);
		}
		
		//TODO
		//Trzeba sprawdzic czy sie nie buguje
		for(int i = 0; i < itemy.size(); ++i) {
			if(weaponCheck && CheckStats.check(rpg, weapon)) {
				changeStatsItem(stats, weapon);
				weaponCheck = false;
			}
			itemy.forEach((slot, item) -> {
				if(CheckStats.check(rpg, item)) {
					changeStatsItem(stats, item);
					itemy.remove(slot);
				}
			});
		}
		
		itemy.forEach((slot, item) -> {
			p.sendMessage("§cNie mozesz zalozyc "+item.getItemMeta().getDisplayName()+" §cna siebie!");
			if(akcesoria.containsKey(slot+1000) && akcesoria.get(slot+1000).equals(item)) {
				dropAkcesoria(jewelry, item, slot);
			} else {
				dropItem(p, item, slot);
			}
		});
		
		if(weaponCheck && CheckStats.check(rpg, weapon)) {
			changeStatsItem(stats, weapon);
			weaponCheck = false;
		}
	}
	
	private static void dropAkcesoria(RpgJewelry jewelry, ItemStack item, int slot) {
		if(!jewelry.getAkcesoria().containsKey(slot))
			return;
		jewelry.getAkcesoria().get(slot).setItem(null);
		Player p = jewelry.getRpg().getPlayer();
		PlayerInventory inv = p.getInventory();
		int freeSlot = inv.firstEmpty();
		if(freeSlot >= 0 && freeSlot < 36) {
			inv.setItem(freeSlot, item);
		}else {
			p.getWorld().dropItem(p.getLocation(), item);
		}
		p.updateInventory();
	}
	
	private static void dropItem(Player p, ItemStack item, int slot) {
		PlayerInventory inv = p.getInventory();
		inv.setItem(slot, new ItemStack(Material.AIR));
		int freeSlot = p.getInventory().firstEmpty();
		if(freeSlot >= 0 && freeSlot < 36) {
			inv.setItem(freeSlot, item);
		}else {
			p.getWorld().dropItem(p.getLocation(), item);
		}
		p.updateInventory();
	}
	
	private static void changeStatsItem(RpgStats stats, ItemStack item) {
		if(item == null ||
				item.getType().equals(Material.AIR) ||
				!item.hasItemMeta() ||
				!item.getItemMeta().hasLore())
			return;
		
		item.getItemMeta().getLore().parallelStream().filter(s -> {
			if(!s.contains(": §7"))
				return false;
			if(!s.contains("§4- §8"))
				return false;
			if(s.contains("Klasa") || s.contains("Krag"))
				return false;
			return true;
		}).forEach(s -> {
			s = s.replace("+", "");
			int toAdd = Integer.parseInt(ChatColor.stripColor(s.split(": ")[1]));
			s = s.replace("§4- §8", "");
			s = s.split(":")[0];
			s = Utils.convertToClassConvention(s);
			
			Class<?> _class = stats.getClass();
			Method getMethod;
			Method setMethod;
			
			try {
				getMethod = _class.getMethod(STR_GET_METHOD+s);
				setMethod = _class.getMethod(STR_SET_METHOD+s, Integer.class);
			} catch (NoSuchMethodException e) {
				return;
			} catch (SecurityException e) {
				return;
			}
			
			try {
				setMethod.invoke(stats, 
						((int)getMethod.invoke(stats)) + toAdd);
			} catch (IllegalAccessException e) {
				return;
			} catch (IllegalArgumentException e) {
				return;
			} catch (InvocationTargetException e) {
				return;
			}
		});
	}
	
}
