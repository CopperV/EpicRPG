package me.Vark123.EpicRPG.Stats;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgJewelry;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Utils.Utils;

public class ChangeStats {
	
	private static final String STR_GET_METHOD = "getFinal";
	private static final String STR_SET_METHOD = "setFinal";
	
	private static final double CRAFTED_ITEM_MODIFIER = 1.1;

	public static void change(RpgPlayer rpg) {
		change(rpg, false);
	}

	public static void change(RpgPlayer rpg, boolean useBackItem) {
		change(rpg, rpg.getPlayer().getInventory().getItemInMainHand(), useBackItem);
	}
	
	public static void change(RpgPlayer rpg, ItemStack weapon) {
		change(rpg, weapon, false);
	}
	
	public static void change(RpgPlayer rpg, ItemStack weapon, boolean useBackItem) {
		Player p = rpg.getPlayer();
		
		RpgJewelry jewelry = rpg.getJewelry();
		RpgStats stats = rpg.getStats();
		RpgPlayerInfo info = rpg.getInfo();
		ItemStack backItem = rpg.getBackItem();
		
		info.getSetCounts().clear();
		info.getSetItems().clear();
		
		rpg.updateHp();
		stats.setFinalInteligencja(stats.getInteligencja()+stats.getPotionInteligencja());
		stats.setFinalSila(stats.getSila()+stats.getPotionSila());
		stats.setFinalWytrzymalosc(stats.getWytrzymalosc()+stats.getPotionWytrzymalosc());
		stats.setFinalZrecznosc(stats.getZrecznosc()+stats.getPotionZrecznosc());
		stats.setFinalZdolnosciMysliwskie(stats.getZdolnosciMysliwskie()+stats.getPotionZdolnosciMysliwskie());
		stats.setFinalMana(stats.getMana()+stats.getPotionMana());
		stats.setFinalWalka(stats.getWalka()+stats.getPotionWalka());
		stats.setFinalOchrona(stats.getOchrona() + stats.getPotionOchrona());
		stats.setFinalObrazenia(stats.getObrazenia() + stats.getPotionObrazenia());
		stats.setFinalSummonPoints(stats.getSummonPoints());
		
		boolean weaponCheck = (weapon != null);

		Map<Integer,ItemStack> itemy = new ConcurrentHashMap<>();
		Map<Integer,ItemStack> akcesoria = new ConcurrentHashMap<>();

		if((p.getEquipment().getHelmet() != null && !p.getEquipment().getHelmet().getType().equals(Material.AIR)) && (p.getEquipment().getHelmet().getItemMeta().hasLore()))
			itemy.put(39, p.getEquipment().getHelmet());
		if((p.getEquipment().getChestplate() != null && !p.getEquipment().getChestplate().getType().equals(Material.AIR)) && (p.getEquipment().getChestplate().getItemMeta().hasLore()))
			itemy.put(38, p.getEquipment().getChestplate());
		if((p.getEquipment().getLeggings() != null && !p.getEquipment().getLeggings().getType().equals(Material.AIR)) && (p.getEquipment().getLeggings().getItemMeta().hasLore()))
			itemy.put(37, p.getEquipment().getLeggings());
		if((p.getEquipment().getBoots() != null && !p.getEquipment().getBoots().getType().equals(Material.AIR)) && (p.getEquipment().getBoots().getItemMeta().hasLore()))
			itemy.put(36, p.getEquipment().getBoots());
		
		if(useBackItem && backItem != null && backItem.getItemMeta().hasLore()) {
			if(backItem.getItemMeta() instanceof ArmorMeta
					|| backItem.getItemMeta().getDisplayName().contains("Demoniczny Sen"))
				dropBackItem(rpg);
			else
				itemy.put(-1, backItem);
		}
		
		jewelry.getAkcesoria().forEach((i, jewItem) -> {
			ItemStack tmp = jewItem.getItem();
			if(tmp == null || tmp.getType().equals(Material.AIR) || !tmp.hasItemMeta() || !tmp.getItemMeta().hasLore())
				return;
			itemy.put(i, tmp);
			akcesoria.put(1000+i, tmp);
		});
		
		
		ItemStack off = p.getInventory().getItemInOffHand();
		if(off!=null && !off.getType().equals(Material.AIR)) {
			EpicComponent compi = new EpicComponent(off, MythicBukkit.inst());
			if(compi.hasKey("rpgtype") && compi.getString("rpgtype").equalsIgnoreCase("gem") && Utils.canUseItem(off, p))
				changeStatsItem(stats, off);
		}
		
		int lastSize = Integer.MAX_VALUE;
		int presentSize = itemy.size();
		while(true) {
			if(presentSize >= lastSize)
				break;
			if(weaponCheck && CheckStats.check(rpg, weapon)) {
				changeStatsItem(stats, weapon);
				weaponCheck = false;
			}
			itemy.forEach((slot, item) -> {
				if(CheckStats.check(rpg, item)) {
					if(slot == -1) 
						changeStatsBackItem(stats, item);
					else
						changeStatsItem(stats, item);
					itemy.remove(slot);
					
					EpicComponent itemNBT = new EpicComponent(item, MythicBukkit.inst());
					if(itemNBT.hasKey("epic_set")) {
						String set = itemNBT.getString("epic_set");
						info.getSetCounts().put(set, info.getSetCounts().getOrDefault(set, 0) + 1);
						List<ItemStack> items = info.getSetItems().getOrDefault(set, new LinkedList<>());
						items.add(item);
						info.getSetItems().put(set, items);
					}
				}
			});
			
			lastSize = presentSize;
			presentSize = itemy.size();
		}
		
		info.getSetItems().entrySet().forEach(entry -> {
			int level = info.getSetCounts().get(entry.getKey());
			entry.getValue().stream().forEach(item -> {
				Utils.setItemSetInfo(item, level);
			});
		});
		itemy.forEach((slot, item) -> {
			p.sendMessage("§cNie mozesz zalozyc "+item.getItemMeta().getDisplayName()+" §cna siebie!");
			if(akcesoria.containsKey(slot+1000) && akcesoria.get(slot+1000).equals(item)) {
				dropAkcesoria(jewelry, item, slot);
			} else if(slot == -1) {
				dropBackItem(rpg);
			} else {
				dropItem(p, item, slot);
			}
		});
		
		if(weaponCheck && CheckStats.check(rpg, weapon)) {
			changeStatsItem(stats, weapon);
			weaponCheck = false;
		}
		
		new StatChangeEvent.StatChangeEventManager(rpg, StatTypes.OBRAZENIA,
				() -> stats.getFinalObrazenia(), 
				newValue -> stats.setFinalObrazenia(newValue))
			.invoke();
		new StatChangeEvent.StatChangeEventManager(rpg, StatTypes.OCHRONA,
				() -> stats.getFinalOchrona(), 
				newValue -> stats.setFinalOchrona(newValue))
			.invoke();
		new StatChangeEvent.StatChangeEventManager(rpg, StatTypes.SILA,
				() -> stats.getFinalSila(), 
				newValue -> stats.setFinalSila(newValue))
			.invoke();
		new StatChangeEvent.StatChangeEventManager(rpg, StatTypes.WYTRZYMALOSC,
				() -> stats.getFinalWytrzymalosc(), 
				newValue -> stats.setFinalWytrzymalosc(newValue))
			.invoke();
		new StatChangeEvent.StatChangeEventManager(rpg, StatTypes.ZRECZNOSC,
				() -> stats.getFinalZrecznosc(), 
				newValue -> stats.setFinalZrecznosc(newValue))
			.invoke();
		new StatChangeEvent.StatChangeEventManager(rpg, StatTypes.ZDOLNOSCI_MYSLIWSKIE,
				() -> stats.getFinalZdolnosciMysliwskie(), 
				newValue -> stats.setFinalZdolnosciMysliwskie(newValue))
			.invoke();
		new StatChangeEvent.StatChangeEventManager(rpg, StatTypes.INTELIGENCJA,
				() -> stats.getFinalInteligencja(), 
				newValue -> stats.setFinalInteligencja(newValue))
			.invoke();
		new StatChangeEvent.StatChangeEventManager(rpg, StatTypes.MANA,
				() -> stats.getFinalMana(), 
				newValue -> stats.setFinalMana(newValue))
			.invoke();
		new StatChangeEvent.StatChangeEventManager(rpg, StatTypes.ZYCIE,
				() -> stats.getFinalHealth(), 
				newValue -> stats.setFinalHealth(newValue))
			.invoke();
		new StatChangeEvent.StatChangeEventManager(rpg, StatTypes.WALKA,
				() -> stats.getFinalWalka(), 
				newValue -> stats.setFinalWalka(newValue))
			.invoke();
		new StatChangeEvent.StatChangeEventManager(rpg, StatTypes.SUMMONS,
				() -> stats.getFinalSummonPoints(), 
				newValue -> stats.setFinalSummonPoints(newValue))
			.invoke();
	}
	
	private static void dropBackItem(RpgPlayer rpg) {
		ItemStack it = rpg.getBackItem();
		if(it == null)
			return;
		rpg.setBackItem(null);
		Player p = rpg.getPlayer();
		PlayerInventory inv = p.getInventory();
		int freeSlot = inv.firstEmpty();
		if(freeSlot >= 0 && freeSlot < 36) {
			inv.setItem(freeSlot, it);
		}else {
			p.getWorld().dropItem(p.getLocation(), it);
		}
		p.updateInventory();
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
		
		double modifier = Utils.isItemCrafted(item) && Utils.isItemCraftedByPlayer(item, stats.getRpg().getPlayer()) ? 
				CRAFTED_ITEM_MODIFIER :
				1;

		item.getItemMeta().getLore().parallelStream().filter(s -> {
			if(!s.contains(": §7"))
				return false;
			if(!s.contains("§4- §8"))
				return false;
			if(s.contains("Klasa") || s.contains("Krag"))
				return false;
			if(s.endsWith("%"))
				return false;
			return true;
		}).forEach(s -> {
			s = s.replace("+", "");
			String tmp = ChatColor.stripColor(s.split(": ")[1]);
			if(!StringUtils.isNumeric(tmp))
				return;
			int toAdd = (int) (Integer.parseInt(tmp) * modifier);
			s = s.replace("§4- §8", "");
			s = s.split(":")[0];
			s = Utils.convertToClassConvention(s);
			
			Class<?> _class = stats.getClass();
			Method getMethod;
			Method setMethod;
			
			try {
				getMethod = _class.getMethod(STR_GET_METHOD+s);
				setMethod = _class.getMethod(STR_SET_METHOD+s, int.class);
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
	
	private static void changeStatsBackItem(RpgStats stats, ItemStack item) {
		if(item == null ||
				item.getType().equals(Material.AIR) ||
				!item.hasItemMeta() ||
				!item.getItemMeta().hasLore())
			return;
		
		double modifier = Utils.isItemCrafted(item) && Utils.isItemCraftedByPlayer(item, stats.getRpg().getPlayer()) ? 
				CRAFTED_ITEM_MODIFIER :
				1;
		
		item.getItemMeta().getLore().parallelStream().filter(s -> {
			if(!s.contains(": §7"))
				return false;
			if(!s.contains("§4- §8"))
				return false;
			if(s.contains("Klasa") || s.contains("Krag"))
				return false;
			if(s.contains("Sila") || s.contains("Wytrzymalosc") 
					|| s.contains("Zrecznosc") || s.contains("Zdolnosci"))
				return false;
			return true;
		}).forEach(s -> {
			s = s.replace("+", "");
			int toAdd = (int) (Integer.parseInt(ChatColor.stripColor(s.split(": ")[1])) * modifier);
			s = s.replace("§4- §8", "");
			s = s.split(":")[0];
			s = Utils.convertToClassConvention(s);
			
			Class<?> _class = stats.getClass();
			Method getMethod;
			Method setMethod;
			
			try {
				getMethod = _class.getMethod(STR_GET_METHOD+s);
				setMethod = _class.getMethod(STR_SET_METHOD+s, int.class);
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
