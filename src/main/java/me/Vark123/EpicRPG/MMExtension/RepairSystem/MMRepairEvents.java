package me.Vark123.EpicRPG.MMExtension.RepairSystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import lombok.Getter;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicInventory.Other.EventCreator;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class MMRepairEvents {

	private static final MMRepairEvents container = new MMRepairEvents();

	private final EventCreator<InventoryClickEvent> clickEvent;
	private final EventCreator<InventoryCloseEvent> closeEvent;
	
	private MMRepairEvents() {
		clickEvent = clickEventCreator();
		closeEvent = closeEventCreator();
	}
	
	public static final MMRepairEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> clickEventCreator(){
		Consumer<InventoryClickEvent> event = e -> {
			int slot = e.getSlot();
			if(slot != 49)
				return;
			
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getWhoClicked();
			List<ItemStack> toDrop = new LinkedList<>();
			ItemExecutor manag = MythicBukkit.inst().getItemManager();
			List<Integer> slotsToCheck = Utils.intArrayToList(MMRepairManager.getInstance().getFreeSlots());
			
			slotsToCheck.stream().filter(i -> {
				ItemStack it = inv.getItem(i);
				if(it == null 
						|| it.getType().equals(Material.AIR))
					return false;
				if(!Utils.isMythicMobItem(it)){
					toDrop.add(it);
					return false;
				}
				return true;
			}).forEach(i -> {
				ItemStack it = inv.getItem(i);
				EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
				String mmId = Utils.getMythicMobItemType(it);
				ItemStack it2 = manag.getItemStack(mmId);
				if(it2 == null 
						|| it2.getType().equals(Material.AIR)){
					toDrop.add(it);
					return;
				}
				
				it2.setAmount(it.getAmount());
				EpicComponent comp2 = new EpicComponent(it2);
				comp.getKeys().stream().filter(key -> {
					return (key.contains("random") && !key.equals("random"));
				}).findAny().ifPresent(s -> {
					Random rand = new Random();
					comp2.setInteger("random"+rand.nextInt(), rand.nextInt());
					comp2.applyTo(it2);
//					it2 = NBT.itemStackFromNBT(comp2);
//					NBT.modify(it2, comp2 -> {
//						comp2.setInteger("Random"+rand.nextInt(), rand.nextInt());
//					});
				});
				
				toDrop.add(it2);
			});
			
			toDrop.forEach(it -> {
				Utils.dropItemStack(p, it);
			});
			inv.clear();
			p.closeInventory();
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> closeEventCreator(){
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : MMRepairManager.getInstance().getFreeSlots()) {
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
