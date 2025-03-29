package me.Vark123.EpicRPG.Klejnoty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import lombok.Getter;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicInventory.Other.EventCreator;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;
import me.Vark123.EpicRPG.Utils.MergableAttribute;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class KlejnotyEvents {
	
	private static final KlejnotyEvents container = new KlejnotyEvents();
	
	private final EventCreator<InventoryClickEvent> insertClickEvent;
	private final EventCreator<InventoryCloseEvent> insertCloseEvent;
	private final EventCreator<InventoryClickEvent> removeClickEvent;
	private final EventCreator<InventoryCloseEvent> removeCloseEvent;
	
	private KlejnotyEvents() {
		insertClickEvent = insertClickEventCreator();
		insertCloseEvent = insertCloseEventCreator();
		removeClickEvent = removeClickEventCreator();
		removeCloseEvent = removeCloseEventCreator();
	}
	
	public static KlejnotyEvents getEvents() {
		return container;
	}

	private EventCreator<InventoryClickEvent> insertClickEventCreator(){
		Consumer<InventoryClickEvent> event = e -> {
			Inventory inv = e.getClickedInventory();
			if(inv == null || !inv.getType().equals(InventoryType.CHEST)) 
				return;
			int slot = e.getSlot();
			int[] slots = KlejnotyManager.getInstance().getInsertFreeSlots();
			if(Utils.intArrayToList(slots)
					.contains(slot))
				return;
			
			e.setCancelled(true);
			if(slot != 22)
				return;
			
			Player p = (Player) e.getWhoClicked();
			ItemStack item = inv.getItem(slots[0]);
			ItemStack klejnot = inv.getItem(slots[1]);
			if(item == null || item.getType().equals(Material.AIR)
					|| !item.hasItemMeta() || !item.getItemMeta().hasLore()
					|| klejnot == null || klejnot.getType().equals(Material.AIR)) {
				p.closeInventory();
				return;
			}
			if(item.getAmount() > 1
					|| klejnot.getAmount() > 1) {
				p.closeInventory();
				return;
			}
			
			EpicComponent itemComp = new EpicComponent(item, MythicBukkit.inst());
			EpicComponent klejnotNBT = new EpicComponent(klejnot, MythicBukkit.inst());
			if(!itemComp.hasKey("free_slots")
					|| !klejnotNBT.hasKey("klejnot")) {
				p.closeInventory();
				return;
			}

			int freeSlots = itemComp.getInteger("free_slots");
			if(freeSlots < 1) {
				p.closeInventory();
				return;
			}else {
				freeSlots = itemComp.getInteger("free_slots");
			}

			String klejnotName = Utils.getMythicMobItemType(klejnot);
			if(itemComp.hasKey("klejnoty")) {
				EpicComponent comp = itemComp.getComponent("klejnoty");
//				Bukkit.broadcastMessage("Test2 "+Arrays.toString(itemComp.getKeys().toArray(new String[0]))+" "+Arrays.toString(comp.getKeys().toArray(new String[0])));
				int size = comp.getInteger("amount");
				for(int i = 1; i <= size; ++i) {
					if(comp.getString("slot"+i).equalsIgnoreCase(klejnotName)){
						p.closeInventory();
						return;
					}
				}
				size += 1;
				comp.setInteger("amount", size);
				comp.setString("slot"+size, klejnotName);
				itemComp.setComponent("klejnoty", comp);
			}else {
				EpicComponent comp = itemComp.getComponent("klejnoty");
				comp.setInteger("amount", 1);
				comp.setString("slot1", klejnotName);
				itemComp.setComponent("klejnoty", comp);
				
//				Bukkit.broadcastMessage("Test1 "+Arrays.toString(itemComp.getKeys().toArray(new String[0]))+" "+Arrays.toString(comp.getKeys().toArray(new String[0])));
			}

			if(freeSlots == 1) {
				itemComp.setString("free_slots", "0");
			}else {
				itemComp.setInteger("free_slots", (freeSlots-1));
			}

			Map<String, Integer> staty = new ConcurrentHashMap<>();
			List<MergableAttribute> attributes = new ArrayList<>();
			
			klejnot.getItemMeta().getLore().stream().filter(line -> {
				return line.contains(": §7") && line.contains("§4- §8");
			}).forEach(line -> {
				String[] tmpArr = line.split(": §7");
				int value = Integer.parseInt(tmpArr[1]);
				int present = staty.getOrDefault(tmpArr[0], 0);
				staty.put(tmpArr[0], present+value);
			});
			
			List<EquipmentSlotGroup> lookingSlots = new LinkedList<>();
			String type = item.getType().name().toUpperCase();
			if(type.contains("HELMET") || type.contains("SKULL") || type.contains("HEAD"))
				lookingSlots.add(EquipmentSlotGroup.HEAD);
			else if(type.contains("CHESTPLATE") || type.contains("ELYTRA"))
				lookingSlots.add(EquipmentSlotGroup.CHEST);
			else if(type.contains("LEGGINGS"))
				lookingSlots.add(EquipmentSlotGroup.LEGS);
			else if(type.contains("BOOTS"))
				lookingSlots.add(EquipmentSlotGroup.FEET);
			lookingSlots.add(EquipmentSlotGroup.HAND);
			lookingSlots.add(EquipmentSlotGroup.OFFHAND);
			lookingSlots.add(EquipmentSlotGroup.ANY);

			if(klejnot.getItemMeta().getAttributeModifiers() != null)
				klejnot.getItemMeta().getAttributeModifiers().entries()
					.stream()
					.forEach(entry -> {
						if(!lookingSlots.contains(entry.getValue().getSlotGroup()))
							return;
						
						MergableAttribute ma = new MergableAttribute(entry.getKey(), entry.getValue());
						attributes.stream()
							.filter(ma::isSimiliar)
							.findFirst()
							.ifPresentOrElse(
									attr -> attr.merge(ma),
									() -> attributes.add(ma));
					});
			
			ItemMeta meta = item.getItemMeta();
			if(meta.getAttributeModifiers() != null)
				meta.getAttributeModifiers().entries()
					.stream()
					.forEach(entry -> {
						MergableAttribute ma = new MergableAttribute(entry.getKey(), entry.getValue());
						attributes.stream()
							.filter(ma::isSimiliar)
							.findFirst()
							.ifPresentOrElse(
									attr -> attr.merge(ma),
									() -> attributes.add(ma));
					});
			
			meta.getAttributeModifiers().keys().stream().collect(Collectors.toList())
				.forEach(meta::removeAttributeModifier);
			

			attributes.forEach(ma -> {
				meta.addAttributeModifier(ma.getAttribute(), ma.getAttributeModifier());
			});
			item.setItemMeta(meta);
			
			itemComp.applyTo(item);
			List<String> lore = item.getItemMeta().getLore();
			
			for(int i = 0; i < lore.size(); ++i) {
				String line = lore.get(i);
				if(!line.contains("Wolnych slotow"))
					continue;
				line = "§8Wolnych slotow: §7"+(freeSlots-1);
				lore.set(i, line);
				break;
			}
			
			if(!staty.isEmpty()) {
				for(int i = 0; i < lore.size(); ++i) {
					String line = lore.get(i);
					if(!line.contains(": §7") || !line.contains("§4- §8"))
						continue;
					
					String[] tmpArr = line.split(": §7");
					if(!staty.containsKey(tmpArr[0]))
						continue;
					
					int value = staty.get(tmpArr[0]);
					int present = Integer.parseInt(ChatColor.stripColor(tmpArr[1]));
					value += present;
					if(tmpArr[0].contains("Obrazenia") || tmpArr[0].contains("Ochrona")) {
						line = tmpArr[0]+": §7"+value;
					}else {
						if(value >= 0)
							line = tmpArr[0]+": §7+"+value;
						else
							line = tmpArr[0]+": §7"+value;
					}
					lore.set(i, line);
					staty.remove(tmpArr[0]);
				}
			}
			
			if(!staty.isEmpty()) {
				MutableInt lorePlace = new MutableInt(0);
				for(int i = 0; i < lore.size(); ++i) {
					String line = lore.get(i);
					if(!line.contains("Wymagania")
							&& !line.contains("Wolnych slotow")) continue;
					lorePlace.setValue(i-1);
					break;
				}
				staty.forEach((stat, value) -> {
					if(value == 0)
						return;
					if(itemComp.hasKey("epic-upgrades")) {
						EpicComponent statsCompound = itemComp.getComponent("epic-upgrades").getComponent("stats");
						if(statsCompound.hasKey(stat+": "))
							value += statsCompound.getInteger(stat+": ");
					}
					if(!(stat.contains("Obrazenia") || stat.contains("Ochrona"))) {
						if(value > 0) {
							lore.add(lorePlace.getValue(), stat+": §7+"+value);
						}
						else {
							lore.add(lorePlace.getValue(), stat+": §7"+value);
							}
					}else {
						lore.add(lorePlace.getValue(), stat+": §7"+value);
					}
				});
			}
			
			lore.add("§b§l✦ "+klejnot.getItemMeta().getDisplayName());
			item.getItemMeta().setLore(lore);
			
			ItemMeta im = item.getItemMeta();
			im.setLore(lore);
			item.setItemMeta(im);
			
			Utils.dropItemStack(p, item);
			inv.clear();
			p.closeInventory();
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}

	private EventCreator<InventoryCloseEvent> insertCloseEventCreator(){
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : KlejnotyManager.getInstance().getInsertFreeSlots()) {
				ItemStack it = inv.getItem(slot);
				if(it == null 
						|| it.getType().equals(Material.AIR))
					continue;
				Utils.dropItemStack(p, it);
			}
			inv.clear();
		};
		
		EventCreator<InventoryCloseEvent> creator = new EventCreator<>(InventoryCloseEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryClickEvent> removeClickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			int slot = e.getSlot();
			if(slot != 22)
				return;
			
			Player p = (Player) e.getWhoClicked();
			Inventory inv = e.getClickedInventory();
			int itemSlot = KlejnotyManager.getInstance().getRemoveFreeSlots()[0];
			ItemStack it = inv.getItem(itemSlot);
			if(it == null
					|| it.getType().equals(Material.AIR)
					|| it.getAmount() > 1) {
				p.closeInventory();
				return;
			}
			
			EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
			if(!Utils.isMythicMobItem(it)
					|| !comp.hasKey("free_slots")
					|| !comp.hasKey("klejnoty")) {
				p.closeInventory();
				return;
			}
			
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			RpgVault vault = rpg.getVault();
			if(!vault.hasEnoughDragonCoins(KlejnotyManager.getInstance().REMOVE_COST)) {
				p.closeInventory();
				return;
			}
			
			List<ItemStack> toDrop = new LinkedList<>();
			ItemExecutor manager = MythicBukkit.inst().getItemManager();
			
			ItemStack newItem = manager.getItemStack(Utils.getMythicMobItemType(it));
			if(comp.hasKey("epic-upgrades")) {
				EpicComponent newItemComp = new EpicComponent(newItem, MythicBukkit.inst());
				EpicComponent upgradesCompound = comp.getComponent("epic-upgrades");
				EpicComponent newUpgradesCompound = newItemComp.getComponent("epic-upgrades");
				newUpgradesCompound.setComponent("epic-upgrades", upgradesCompound);
				newItemComp.applyTo(newItem);
//				newItem = NBT.itemStackFromNBT(newitemComp);
				
				EpicComponent statsCompound = upgradesCompound.getComponent("stats");
				ItemMeta im = newItem.getItemMeta();
				im.setDisplayName(im.getDisplayName()+" §r§7§l+"+upgradesCompound.getInteger("level"));
				List<String> lore = im.getLore();
				List<String> loreCopy = new LinkedList<>(lore);
				for(String line : loreCopy) {
					if(!line.contains(": §7") || !line.contains("§4- §8") || line.endsWith("%"))
						continue;
					
					String[] arr = line.replace("+", "").split("§7");
					if(arr.length < 2 || !StringUtils.isNumeric(arr[1])
							|| arr[1].contains("."))
						continue;
						
					String key = arr[0];
					Integer value = Integer.parseInt(arr[1]);
					if(!statsCompound.hasKey(key))
						continue;
					
					lore.set(loreCopy.indexOf(line), 
							arr[0]+(line.contains("§7+")?"§7+":"§7")
								+(statsCompound.getInteger(arr[0])+value));
				}
				im.setLore(lore);
				newItem.setItemMeta(im);
			}
			toDrop.add(newItem);
			
			EpicComponent compCompound = comp.getComponent("klejnoty");
			int size = compCompound.getInteger("amount");
			for(int i = 1; i <= size; ++i) {
				String klejnotId = compCompound.getString("slot"+i);
				toDrop.add(manager.getItemStack(klejnotId));
			}
			
			toDrop.forEach(item -> {
				Utils.dropItemStack(p, item);
			});
			vault.removeDragonCoins(KlejnotyManager.getInstance().REMOVE_COST);
			inv.clear();
			p.closeInventory();
			return;
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> removeCloseEventCreator() {
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : KlejnotyManager.getInstance().getRemoveFreeSlots()) {
				ItemStack it = inv.getItem(slot);
				if(it == null 
						|| it.getType().equals(Material.AIR))
					continue;
				Utils.dropItemStack(p, it);
			}
			inv.clear();
		};
		
		EventCreator<InventoryCloseEvent> creator = new EventCreator<>(InventoryCloseEvent.class, event);
		return creator;
	}
	
}
