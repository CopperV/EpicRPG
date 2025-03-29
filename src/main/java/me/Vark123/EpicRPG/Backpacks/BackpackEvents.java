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
import org.bukkit.plugin.java.JavaPlugin;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicInventory.Other.EventCreator;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class BackpackEvents {
	
	private static final BackpackEvents container = new BackpackEvents();
	
	private final EventCreator<InventoryClickEvent> clickEvent;
	private final EventCreator<InventoryCloseEvent> closeEvent;
	private final EventCreator<InventoryClickEvent> clickUpgradeEvent;
	private final EventCreator<InventoryCloseEvent> closeUpgradeEvent;
	
	private BackpackEvents() {
		clickEvent = clickEventCreator();
		closeEvent = closeEventCreator();
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
				
				EpicComponent itComp = new EpicComponent(it, MythicBukkit.inst());
				
				if(!itComp.hasKey("soulbind")
						|| !itComp.getString("soulbind").equalsIgnoreCase(e.getWhoClicked().getName())) {
					toReturn.add(it);
					continue;
				}
				if(!itComp.hasKey("katedra")
						|| !BackpackManager.getInstance().getBigBackpack()
							.contains(itComp.getString("katedra"))
						|| katedraNBT.contains(itComp.getString("katedra"))) {
					toReturn.add(it);
					continue;
				}

				String katedraId = itComp.getString("katedra");
				katedraNBT.add(katedraId);
				katedraItems.put(katedraId, it);
			}

			switch(katedraNBT.size()) {
				case 12:
					katedraItems.clear();
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "backpack give 3 "+p.getName());
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
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "backpack give 2 "+p.getName());
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
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "backpack give 1 "+p.getName());
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
			inv.clear();
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
				
				EpicComponent itComp = new EpicComponent(katedraIt, MythicBukkit.inst());
				if(!itComp.hasKey("soulbind")
						|| !itComp.getString("soulbind").equalsIgnoreCase(p.getName())) {
					p.closeInventory();
					return;
				}
				if(!itComp.hasKey("katedra")
						|| !BackpackManager.getInstance().getGiantBackpack()
							.contains(itComp.getString("katedra"))
						|| katedraNBT.contains(itComp.getString("katedra"))) {
					p.closeInventory();
					return;
				}

				katedraNBT.add(itComp.getString("katedra"));
			}

			ItemStack oldBackpack = inv.getItem(slots[7]);
			EpicComponent comp = new EpicComponent(oldBackpack, (JavaPlugin) Bukkit.getPluginManager().getPlugin("BackpackPlus"));
			if(!comp.hasKey("backpacktier") || comp.getInteger("backpacktier") != 3){
				p.closeInventory();
				return;
			}
			
			
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "backpack give 5 "+p.getName());
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
			inv.clear();
		};
		
		EventCreator<InventoryCloseEvent> creator = new EventCreator<>(InventoryCloseEvent.class, event);
		return creator;
	}

}
