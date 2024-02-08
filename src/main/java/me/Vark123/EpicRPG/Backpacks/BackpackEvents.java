package me.Vark123.EpicRPG.Backpacks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class BackpackEvents {
	
	private static final BackpackEvents container = new BackpackEvents();
	
	private final EventCreator<InventoryClickEvent> clickEvent;
	private final EventCreator<InventoryCloseEvent> closeEvent;
	private final EventCreator<InventoryCloseEvent> repairCloseEvent;
	private final EventCreator<InventoryClickEvent> clickUpgradeEvent;
	private final EventCreator<InventoryCloseEvent> closeUpgradeEvent;
	
	private BackpackEvents() {
		clickEvent = clickEventCreator();
		closeEvent = closeEventCreator();
		repairCloseEvent = repairCloseEventCreator();
		clickUpgradeEvent = clickUpgradeEventCreator();
		closeUpgradeEvent = closeUpgradeEventCreator();
	}
	
	public static BackpackEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> clickEventCreator(){
		
		Consumer<InventoryClickEvent> event = e -> {
			Inventory inv = e.getClickedInventory();
			if(inv == null || !inv.getType().equals(InventoryType.CHEST)) 
				return;
			int slot = e.getSlot();
			if(Utils.intArrayToList(BackpackManager.getInstance().getFreeSlots())
					.contains(slot))
				return;
			
			e.setCancelled(true);
			if(slot != 49)
				return;
			
			Player p = (Player) e.getWhoClicked();
			List<ItemStack> toReturn = new LinkedList<>();
			Map<String, ItemStack> katedraItems = new HashMap<>();
			List<String> katedraNBT = new LinkedList<>();
			for(int freeSlot : BackpackManager.getInstance().getFreeSlots()) {
				ItemStack it = inv.getItem(freeSlot);
				if(it == null
						|| it.getType().equals(Material.AIR))
					continue;
				
				NBTItem nbtIt = new NBTItem(it);
				if(!nbtIt.hasTag("soulbind")
						|| !nbtIt.getString("soulbind").equalsIgnoreCase(e.getWhoClicked().getName())) {
					toReturn.add(it);
					continue;
				}
				if(!nbtIt.hasTag("Katedra")
						|| !BackpackManager.getInstance().getBigBackpack()
							.contains(nbtIt.getString("Katedra"))
						|| katedraNBT.contains(nbtIt.getString("Katedra"))) {
					toReturn.add(it);
					continue;
				}
				
				String katedraId = nbtIt.getString("Katedra");
				katedraNBT.add(katedraId);
				katedraItems.put(katedraId, it);
			}
			
			switch(katedraNBT.size()) {
				case 12:
					katedraItems.clear();
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fb give "+p.getName()+" 3");
					break;
				case 11:
				case 10:
				case 9:
				case 8:
					int craftSizeMedium = 8;
					for(String mediumKatedra : BackpackManager.getInstance().getMediumBackpack()) {
						if(!katedraNBT.contains(mediumKatedra))
							continue;
						--craftSizeMedium;
					}
					if(craftSizeMedium == 0) {
						katedraNBT.stream().forEach(s -> {
							katedraItems.remove(s);
						});
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fb give "+p.getName()+" 2");
						break;
					}
				case 7:
				case 6:
				case 5:
				case 4:
					int craftSizeSmall = 4;
					for(String smallKatedra : BackpackManager.getInstance().getSmallBackpack()) {
						if(!katedraNBT.contains(smallKatedra))
							continue;
						--craftSizeSmall;
					}
					if(craftSizeSmall == 0) {
						katedraNBT.stream().forEach(s -> {
							katedraItems.remove(s);
						});
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fb give "+p.getName()+" 1");
					}
					break;
			}
			toReturn.stream().forEach(it -> {
				Utils.dropItemStack(p, it);
			});
			katedraItems.values().stream().forEach(it -> {
				Utils.dropItemStack(p, it);
			});
			inv.clear();
			e.getWhoClicked().closeInventory();
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> closeEventCreator(){
		
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : BackpackManager.getInstance().getFreeSlots()) {
				ItemStack it = inv.getItem(slot);
				if(it == null 
						|| it.getType().equals(Material.AIR))
					continue;
				Utils.dropItemStack(p, it);
			}
		};
		
		EventCreator<InventoryCloseEvent> creator = new EventCreator<>(InventoryCloseEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> repairCloseEventCreator(){
		
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			ItemStack backpack = inv.getItem(BackpackManager.getInstance().getRepairFreeSlots()[0]);
			
			if(backpack == null
					|| backpack.getType().equals(Material.AIR))
				return;
			
			List<ItemStack> toDrop = new LinkedList<>();
			if(BackpackUtils.isBuggedBackpack(backpack)) {
				NBTItem nbt = new NBTItem(backpack);
				int slotsAmount = nbt.getInteger("SlotsAmount");
				int type = 1;
				switch(slotsAmount) {
					case 9:
						type = 1;
						break;
					case 18:
						type = 2;
						break;
					case 27:
						type = 3;
						break;
					case 54:
						type = 4;
						break;
					case 36:
						type = 5;
						break;
				}
				for(int i = 0; i < slotsAmount; ++i) {
					ItemStack it = BackpackUtils.itemstackFromBase64(nbt.getString(i+""));
					if(it == null 
							|| it.getType().equals(Material.AIR)) {
						continue;
					}
					toDrop.add(it);
				}
				
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fb give "+p.getName()+" "+type);
			} else {
				toDrop.add(backpack);
			}
			
			toDrop.parallelStream().forEach(it -> {
				Utils.dropItemStack(p, it);
			});
		};
		
		EventCreator<InventoryCloseEvent> creator = new EventCreator<>(InventoryCloseEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryClickEvent> clickUpgradeEventCreator(){
		
		Consumer<InventoryClickEvent> event = e -> {
			int[] slots = BackpackManager.getInstance().getUpgradeFreeSlots();
			int slot = e.getSlot();
			Inventory inv = e.getClickedInventory();
			ItemStack it = inv.getItem(slot);
			if(it == null || it.getType().equals(Material.AIR))
				return;

			if(!it.equals(BackpackManager.getInstance().getCreate()))
				return;

			Player p = (Player) e.getWhoClicked();
			List<String> katedraNBT = new LinkedList<>();
			for(int i = 0; i < slots.length - 1; ++i) {
				int freeSlot = slots[i];
				ItemStack katedraIt = inv.getItem(freeSlot);
				if(katedraIt == null
						|| katedraIt.getType().equals(Material.AIR)) {
					p.closeInventory();
					return;
				}
				
				NBTItem nbtIt = new NBTItem(katedraIt);
				if(!nbtIt.hasTag("soulbind")
						|| !nbtIt.getString("soulbind").equalsIgnoreCase(p.getName())) {
					p.closeInventory();
					return;
				}
				if(!nbtIt.hasTag("Katedra")
						|| !BackpackManager.getInstance().getGiantBackpack()
							.contains(nbtIt.getString("Katedra"))
						|| katedraNBT.contains(nbtIt.getString("Katedra"))) {
					p.closeInventory();
					return;
				}

				katedraNBT.add(nbtIt.getString("Katedra"));
			}

			ItemStack oldBackpack = inv.getItem(slots[7]);
			NBTItem nbt = new NBTItem(oldBackpack);
			ReadWriteNBT compund = nbt.getCompound("PublicBukkitValues");
			if(!compund.hasTag("fancybags:backpackid") || compund.getInteger("fancybags:backpackid") != 3){
				p.closeInventory();
				return;
			}

			List<ItemStack> toDropList = new LinkedList<>();
			for(int i = 0; i < 3*9; ++i) {
				ItemStack toDrop = BackpackUtils.itemstackFromBase64(compund.getString("fancybags:"+i));
				if(toDrop == null 
						|| toDrop.getType().equals(Material.AIR)) {
					continue;
				}
				toDropList.add(toDrop);
			}
			toDropList.parallelStream().forEach(toDrop -> {
				Utils.dropItemStack(p, toDrop);
			});
			
			
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fb give "+p.getName()+" 5");
			inv.clear();
			e.getWhoClicked().closeInventory();
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> closeUpgradeEventCreator(){
		
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : BackpackManager.getInstance().getUpgradeFreeSlots()) {
				ItemStack it = inv.getItem(slot);
				if(it == null 
						|| it.getType().equals(Material.AIR))
					continue;
				Utils.dropItemStack(p, it);
			}
		};
		
		EventCreator<InventoryCloseEvent> creator = new EventCreator<>(InventoryCloseEvent.class, event);
		return creator;
	}

}
