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
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class BackpackEvents {
	
	private static final BackpackEvents container = new BackpackEvents();
	
	private final EventCreator<InventoryClickEvent> clickEvent;
	private final EventCreator<InventoryCloseEvent> closeEvent;
	
	private BackpackEvents() {
		clickEvent = clickEventCreator();
		closeEvent = closeEventCreator();
	}
	
	public static BackpackEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> clickEventCreator(){
		
		Consumer<InventoryClickEvent> event = e -> {
			if(e.isCancelled())
				return;

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

}
