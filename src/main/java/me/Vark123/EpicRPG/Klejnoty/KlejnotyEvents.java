package me.Vark123.EpicRPG.Klejnoty;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import lombok.Getter;
import me.Vark123.EpicInventory.Other.EventCreator;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;
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
			
			ReadWriteNBT itemNBT = NBT.itemStackToNBT(item);
			ReadWriteNBT klejnotNBT = NBT.itemStackToNBT(klejnot);
			if(!itemNBT.hasTag("FreeSlots")
					|| !klejnotNBT.hasTag("Klejnot")) {
				p.closeInventory();
				return;
			}

			int freeSlots;
			if(itemNBT.getInteger("FreeSlots") < 1) {
				if(Integer.parseInt(itemNBT.getString("FreeSlots")) < 1) {
					p.closeInventory();
					return;
				}
				freeSlots = Integer.parseInt(itemNBT.getString("FreeSlots"));
			}else {
				freeSlots = itemNBT.getInteger("FreeSlots");
			}
			
			String klejnotName = Utils.getMythicMobItemType(klejnot);
			if(itemNBT.hasTag("Klejnoty")) {
				ReadWriteNBT nbt = itemNBT.getCompound("Klejnoty");
				int size = nbt.getInteger("amount");
				for(int i = 1; i <= size; ++i) {
					if(nbt.getString("slot"+i).equalsIgnoreCase(klejnotName)){
						p.closeInventory();
						return;
					}
				}
				size += 1;
				nbt.setInteger("amount", size);
				nbt.setString("slot"+size, klejnotName);
			}else {
				ReadWriteNBT comp = itemNBT.getOrCreateCompound("Klejnoty");
				comp.setInteger("amount", 1);
				comp.setString("slot1", klejnotName);
			}
			
			if(freeSlots == 1) {
				itemNBT.setString("FreeSlots", "0");
			}else {
				itemNBT.setInteger("FreeSlots", (freeSlots-1));
			}

			Map<String, Integer> staty = new ConcurrentHashMap<>();
			Map<String, Double> hpSlots = new ConcurrentHashMap<>();
			Map<String, Double> speedSlots = new ConcurrentHashMap<>();
			Map<String, Double> knockSlots = new ConcurrentHashMap<>();
			
			klejnot.getItemMeta().getLore().stream().filter(line -> {
				return line.contains(": §7") && line.contains("§4- §8");
			}).forEach(line -> {
				String[] tmpArr = line.split(": §7");
				int value = Integer.parseInt(tmpArr[1]);
				int present = staty.getOrDefault(tmpArr[0], 0);
				staty.put(tmpArr[0], present+value);
			});
			
			List<String> lookingSlots = new LinkedList<>();
			String type = item.getType().name().toUpperCase();
			if(type.contains("HELMET") || type.contains("SKULL") || type.contains("HEAD"))
				lookingSlots.add("head");
			else if(type.contains("CHESTPLATE") || type.contains("ELYTRA"))
				lookingSlots.add("chest");
			else if(type.contains("LEGGINGS"))
				lookingSlots.add("legs");
			else if(type.contains("BOOTS"))
				lookingSlots.add("feet");
			lookingSlots.add("mainhand");
			lookingSlots.add("offhand");
			
			ReadWriteNBTCompoundList klejnotStats = klejnotNBT.getCompoundList("AttributeModifiers");
			for(int i = 0; i < klejnotStats.size(); ++i) {
				ReadWriteNBT lc = klejnotStats.get(i);
				if(!lc.hasTag("Slot")) {
					lookingSlots.stream().forEach(s -> {
						switch(lc.getString("Name").toLowerCase().replace("generic.", "")) {
							case "max_health":
								hpSlots.put(s, lc.getDouble("Amount"));
								break;
							case "knockback_resistance":
								knockSlots.put(s, lc.getDouble("Amount"));
								break;
							case "movement_speed":
								speedSlots.put(s, lc.getDouble("Amount"));
								break;
						}
					});
				}
				
				String strSlot = lc.getString("Slot");
				if(!lookingSlots.contains(strSlot.toLowerCase())) {	
					continue;
				}
				switch(lc.getString("Name").toLowerCase().replace("generic.", "")) {
					case "max_health":
						hpSlots.put(strSlot, lc.getDouble("Amount"));
						break;
					case "knockback_resistance":
						knockSlots.put(strSlot, lc.getDouble("Amount"));
						break;
					case "movement_speed":
						speedSlots.put(strSlot, lc.getDouble("Amount"));
						break;
				}
			}

			ReadWriteNBTCompoundList itemStats = itemNBT.getCompoundList("AttributeModifiers");
			for(int i = 0; i < itemStats.size(); ++i) {
				ReadWriteNBT lc = itemStats.get(i);
				String strSlot = lc.getString("Slot");
				double tmp;
				switch(lc.getString("Name").toLowerCase().replace("generic.", "")) {
					case "max_health":
						if(!hpSlots.containsKey(strSlot)) 
							break;
						tmp = lc.getDouble("Amount");
						lc.setDouble("Amount", tmp+hpSlots.get(strSlot));
						hpSlots.remove(strSlot);
						break;
					case "knockback_resistance":
						if(!knockSlots.containsKey(strSlot)) 
							break;
						tmp = lc.getDouble("Amount");
						lc.setDouble("Amount", tmp+knockSlots.get(strSlot));
						knockSlots.remove(strSlot);
						break;
					case "movement_speed":
						if(!speedSlots.containsKey(strSlot)) 
							break;
						tmp = lc.getDouble("Amount");
						lc.setDouble("Amount", tmp+speedSlots.get(strSlot));
						speedSlots.remove(strSlot);
						break;
				}
			}
			
			hpSlots.forEach((strSlot, value) -> {
				UUID uuid = UUID.randomUUID();
				ReadWriteNBT lc = itemStats.addCompound();
				lc.setString("AttributeName", "generic.max_health");
				lc.setString("Name", "generic.max_health");
				lc.setDouble("Amount", value);
				lc.setInteger("Operation", 0);
				lc.setLong("UUIDLeast", uuid.getLeastSignificantBits());
				lc.setLong("UUIDMost", uuid.getMostSignificantBits());
				lc.setString("Slot", strSlot);
			});
			knockSlots.forEach((strSlot, value) -> {
				UUID uuid = UUID.randomUUID();
				ReadWriteNBT lc = itemStats.addCompound();
				lc.setString("AttributeName", "generic.knockback_resistance");
				lc.setString("Name", "generic.knockback_resistance");
				lc.setDouble("Amount", value);
				lc.setInteger("Operation", 0);
				lc.setLong("UUIDLeast", uuid.getLeastSignificantBits());
				lc.setLong("UUIDMost", uuid.getMostSignificantBits());
				lc.setString("Slot", strSlot);
			});
			speedSlots.forEach((strSlot, value) -> {
				UUID uuid = UUID.randomUUID();
				ReadWriteNBT lc = itemStats.addCompound();
				lc.setString("AttributeName", "generic.movement_speed");
				lc.setString("Name", "generic.movement_speed");
				lc.setDouble("Amount", value);
				lc.setInteger("Operation", 1);
				lc.setLong("UUIDLeast", uuid.getLeastSignificantBits());
				lc.setLong("UUIDMost", uuid.getMostSignificantBits());
				lc.setString("Slot", strSlot);
			});

			item = NBT.itemStackFromNBT(itemNBT);
//			itemNBT.applyNBT(item);
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
					if(itemNBT.hasTag("epic-upgrades")) {
						ReadWriteNBT statsCompound = itemNBT.getCompound("epic-upgrades").getCompound("stats");
						if(statsCompound.hasTag(stat+": "))
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
			
			ReadWriteNBT nbt = NBT.itemStackToNBT(it);
			if(!Utils.isMythicMobItem(it)
					|| !nbt.hasTag("FreeSlots")
					|| !nbt.hasTag("Klejnoty")) {
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
			if(nbt.hasTag("epic-upgrades")) {
				ReadWriteNBT newItemNbt = NBT.itemStackToNBT(newItem);
				ReadWriteNBT upgradesCompound = nbt.getCompound("epic-upgrades");
				ReadWriteNBT newUpgradesCompound = newItemNbt.getOrCreateCompound("epic-upgrades");
				newUpgradesCompound.mergeCompound(upgradesCompound);
//				newItemNbt.applyNBT(newItem);
				newItem = NBT.itemStackFromNBT(newItemNbt);
				
				ReadWriteNBT statsCompound = upgradesCompound.getOrCreateCompound("stats");
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
					if(!statsCompound.hasTag(key))
						continue;
					
					lore.set(loreCopy.indexOf(line), 
							arr[0]+(line.contains("§7+")?"§7+":"§7")
								+(statsCompound.getInteger(arr[0])+value));
				}
				im.setLore(lore);
				newItem.setItemMeta(im);
			}
			toDrop.add(newItem);
			
			ReadWriteNBT nbtCompound = nbt.getCompound("Klejnoty");
			int size = nbtCompound.getInteger("amount");
			for(int i = 1; i <= size; ++i) {
				String klejnotId = nbtCompound.getString("slot"+i);
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
