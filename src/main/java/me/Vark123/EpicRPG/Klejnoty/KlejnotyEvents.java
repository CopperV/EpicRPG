package me.Vark123.EpicRPG.Klejnoty;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
			if(!KlejnotyManager.getInstance().canPutKlejnotIntoItem(p, item, klejnot)) {
				p.closeInventory();
				return;
			}
			
			item = KlejnotyManager.getInstance().putKlejnotIntoItem(p, item, klejnot);
			
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
			
			if(!KlejnotyManager.getInstance().canRemoveKlejnotyFromItem(p, it)) {
				p.closeInventory();
				return;
			}
			
			List<ItemStack> toDrop = KlejnotyManager.getInstance().getItemsFromKlejnotyRemoving(p, it);
			
			toDrop.forEach(item -> {
				Utils.dropItemStack(p, item);
			});
			

			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			RpgVault vault = rpg.getVault();
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
