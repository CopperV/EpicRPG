package me.Vark123.EpicRPG.Klejnoty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicInventory.Content.InventoryContents;
import me.Vark123.EpicInventory.Content.InventoryProvider;
import me.Vark123.EpicInventory.Enums.Action;
import me.Vark123.EpicInventory.Enums.DisabledEvents;
import me.Vark123.EpicInventory.Enums.DisabledInventoryClick;
import me.Vark123.EpicInventory.Pagination.EpicInventory;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;
import me.Vark123.EpicRPG.Utils.MergableAttribute;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class KlejnotyManager {

	private static final KlejnotyManager instance = new KlejnotyManager();

	@Getter(value = AccessLevel.NONE)
	public final int REMOVE_COST;
	
	private final InventoryProvider creatorProvider;
	private final InventoryProvider removeProvider;
	private final int[] insertFreeSlots;
	private final int[] removeFreeSlots;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack create;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack remove;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack item;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack item2;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack klejnot;
	
	private KlejnotyManager() {
		REMOVE_COST = 100_000;
		
		insertFreeSlots = new int[] {11,15};
		removeFreeSlots = new int[] {13};
		
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		create = new ItemStack(Material.EMERALD, 1);{
			ItemMeta im = create.getItemMeta();
			im.setDisplayName("§6§lWloz");
			create.setItemMeta(im);
		}
		remove = new ItemStack(Material.EMERALD, 1);{
			ItemMeta im = remove.getItemMeta();
			im.setDisplayName("§6§lWyjmij");
			List<String> lore = new LinkedList<>();
			lore.add("§aKoszt: §c§o100 000 Smoczych Monet");
			im.setLore(lore);
			remove.setItemMeta(im);
		}
		item = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);{
			ItemMeta im = item.getItemMeta();
			im.setDisplayName("§2§lPrzedmiot z wolnym slotem");
			item.setItemMeta(im);
		}
		item2 = new ItemStack(Material.NETHERITE_SWORD, 1);{
			ItemMeta im = item2.getItemMeta();
			im.setDisplayName("§2§lPrzedmiot z klejnotem");
			item2.setItemMeta(im);
		}
		klejnot = new ItemStack(Material.DIAMOND, 1);{
			ItemMeta im = klejnot.getItemMeta();
			im.setDisplayName("§b§lKlejnot");
			klejnot.setItemMeta(im);
		}
		
		creatorProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(insertFreeSlots);
				for(int i = 0; i < 27; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 22) {
						contents.set(i, create);
						continue;
					}
					if(i == 2) {
						contents.set(i, item);
						continue;
					}
					if(i == 6) {
						contents.set(i, klejnot);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
		removeProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(removeFreeSlots);
				for(int i = 0; i < 27; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 22) {
						contents.set(i, remove);
						continue;
					}
					if(i == 4) {
						contents.set(i, item2);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
		
	}
	
	public static final KlejnotyManager getInstance() {
		return instance;
	}
	
	public void openInsertMenu(Player p) {
		EpicInventory.builder()
			.title("§6§lWkladanie Klejnotow")
			.size(27)
			.ignoredSlots(insertFreeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.listener(KlejnotyEvents.getEvents().getInsertClickEvent())
			.listener(KlejnotyEvents.getEvents().getInsertCloseEvent())
			.provider(creatorProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public void openRemoveMenu(Player p) {
		EpicInventory.builder()
			.title("§6§lWyjmowanie Klejnotow")
			.size(27)
			.ignoredSlots(removeFreeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.listener(KlejnotyEvents.getEvents().getRemoveClickEvent())
			.listener(KlejnotyEvents.getEvents().getRemoveCloseEvent())
			.provider(removeProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public boolean canPutKlejnotIntoItem(Player player, ItemStack item, ItemStack klejnot) {
		if(item == null || item.getType().equals(Material.AIR)
				|| !item.hasItemMeta() || !item.getItemMeta().hasLore()
				|| klejnot == null || klejnot.getType().equals(Material.AIR)) {
			return false;
		}
		if(!Utils.canUseItem(item, player) ||
				!Utils.canUseItem(klejnot, player)) {
			return false;
		}
		if(item.getAmount() > 1
				|| klejnot.getAmount() > 1) {
			return false;
		}
		
		EpicComponent itemComp = new EpicComponent(item, MythicBukkit.inst());
		EpicComponent klejnotNBT = new EpicComponent(klejnot, MythicBukkit.inst());
		if(!itemComp.hasKey("free_slots")
				|| !klejnotNBT.hasKey("klejnot")) {
			return false;
		}
		
		int freeSlots = itemComp.getInteger("free_slots");
		if(freeSlots < 1)
			return false;
		
		if(!itemComp.hasKey("klejnoty"))
			return true;
		
		String klejnotName = Utils.getMythicMobItemType(klejnot);
		
		EpicComponent comp = itemComp.getComponent("klejnoty");
		int size = comp.getInteger("amount");
		for(int i = 1; i <= size; ++i) {
			EpicComponent slotComp = comp.getComponent("slot"+i);
			if(slotComp.getString("id").equalsIgnoreCase(klejnotName))
				return false;
		}
		
		return true;
	}
	public ItemStack putKlejnotIntoItem(Player player, ItemStack item, ItemStack klejnot) {
		ItemStack newItem = item.clone();
		String klejnotId = Utils.getMythicMobItemType(klejnot);
		
		EpicComponent itemComp = new EpicComponent(newItem, MythicBukkit.inst());
		
		EpicComponent itemKlejnotComp = itemComp.getComponent("klejnoty");
		int size = itemKlejnotComp.hasKey("amount") ?
				itemKlejnotComp.getInteger("amount") :
				0;
		
		++size;
		EpicComponent slotComp = itemKlejnotComp.getComponent("slot"+size);
		
		Player soulbindedKlejnotPlayer = Utils.getSoulbindedPlayer(klejnot);
		slotComp.setString("id", klejnotId);
		if(soulbindedKlejnotPlayer != null) {
			slotComp.setUUID("soulbind", soulbindedKlejnotPlayer.getUniqueId());
		}
		
		itemKlejnotComp.setComponent("slot"+size, slotComp);
		itemKlejnotComp.setInteger("amount", size);
		
		itemComp.setComponent("klejnoty", itemKlejnotComp);
		
		int freeSlots = itemComp.getInteger("free_slots") - 1;
		itemComp.setInteger("free_slots", freeSlots);
		
		itemComp.applyTo(newItem);

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
		String type = newItem.getType().name().toUpperCase();
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
		
		ItemMeta meta = newItem.getItemMeta();
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
		
		newItem.setItemMeta(meta);
		
		List<String> lore = newItem.getItemMeta().getLore();
		
		for(int i = 0; i < lore.size(); ++i) {
			String line = lore.get(i);
			if(!line.contains("Wolnych slotow"))
				continue;
			line = "§8Wolnych slotow: §7"+freeSlots;
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
		
		lore.add("§b§l✦ "+klejnot.getItemMeta().getDisplayName()+
				(soulbindedKlejnotPlayer == null ?
				"" :
				" §7~§e§o"+soulbindedKlejnotPlayer.getName()));
		newItem.getItemMeta().setLore(lore);
		
		ItemMeta im = newItem.getItemMeta();
		im.setLore(lore);
		newItem.setItemMeta(im);
		
		return newItem;
	}
	
	public boolean canRemoveKlejnotyFromItem(Player player, ItemStack item) {
		if(item == null ||
				item.getType().equals(Material.AIR) ||
				item.getAmount() > 1)
			return false;
		
		if(!Utils.canUseItem(item, player))
			return false;
		
		EpicComponent itemComp = new EpicComponent(item, MythicBukkit.inst());
		if(!Utils.isMythicMobItem(item) ||
				!itemComp.hasKey("free_slots") ||
				!itemComp.hasKey("klejnoty"))
			return false;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgVault vault = rpg.getVault();
		if(!vault.hasEnoughDragonCoins(REMOVE_COST))
			return false;
		
		return true;
	}
	
	public List<ItemStack> getItemsFromKlejnotyRemoving(Player player, ItemStack item) {
		List<ItemStack> toReturn = new LinkedList<>();
		
		EpicComponent itemComp = new EpicComponent(item, MythicBukkit.inst());
		ItemExecutor manager = MythicBukkit.inst().getItemManager();
		
		ItemStack newItem = manager.getItemStack(Utils.getMythicMobItemType(item));
		if(Utils.isItemSoulbinded(item))
			Utils.setItemSoulbinded(newItem, Utils.getSoulbindedPlayer(item));
		if(itemComp.hasKey("epic-upgrades")) {
			EpicComponent newItemComp = new EpicComponent(newItem, MythicBukkit.inst());
			EpicComponent upgradesCompound = itemComp.getComponent("epic-upgrades");
			EpicComponent newUpgradesCompound = newItemComp.getComponent("epic-upgrades");
			newUpgradesCompound.setComponent("epic-upgrades", upgradesCompound);
			newItemComp.applyTo(newItem);
			
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
		toReturn.add(newItem);
		
		EpicComponent itemKlejnotComp = itemComp.getComponent("klejnoty");
		int size = itemKlejnotComp.getInteger("amount");
		for(int i = 1; i <= size; ++i) {
			EpicComponent slotComp = itemKlejnotComp.getComponent("slot"+i);
			ItemStack klejnot = manager.getItemStack(slotComp.getString("id"));
			if(slotComp.hasKey("soulbind"))
				Utils.setItemSoulbinded(klejnot, Bukkit.getPlayer(slotComp.getUUID("soulbind")));
			toReturn.add(klejnot);
		}
		
		return toReturn;
	}
	
}
