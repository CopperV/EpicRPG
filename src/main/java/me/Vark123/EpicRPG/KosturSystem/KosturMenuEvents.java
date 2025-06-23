package me.Vark123.EpicRPG.KosturSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicInventory.Other.EventCreator;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class KosturMenuEvents {

	private static final KosturMenuEvents container = new KosturMenuEvents();

	private final EventCreator<InventoryClickEvent> kosturClickEvent;
	private final EventCreator<InventoryCloseEvent> kosturCloseEvent;
	private final EventCreator<InventoryClickEvent> kosturModifyClickEvent;
	private final EventCreator<InventoryClickEvent> kosturCreateClickEvent;
	private final EventCreator<InventoryCloseEvent> kosturCreateCloseEvent;
	
	private KosturMenuEvents() {
		kosturClickEvent = kosturClickEventCreator();
		kosturCloseEvent = kosturCloseEventCreator();
		kosturModifyClickEvent = kosturModifyClickEventCreator();
		kosturCreateClickEvent = kosturCreateClickEventCreator();
		kosturCreateCloseEvent = kosturCreateCloseEventCreator();
	}
	
	public static final KosturMenuEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> kosturClickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			int slot = e.getSlot();
			if(slot != 22)
				return;
			
			Player p = (Player) e.getWhoClicked();
			Inventory inv = e.getView().getTopInventory();
			ItemStack kostur = inv.getItem(KosturMenuManager.getInstance().getKosturFreeSlots()[0]);
			if(kostur == null || kostur.getType().equals(Material.AIR) || !Utils.canUseItem(kostur, p)) {
				p.closeInventory();
				return;
			}

			EpicComponent comp = new EpicComponent(kostur, MythicBukkit.inst());
			if(!comp.hasKey("rozdzka")) {
				p.closeInventory();
				return;
			}

			inv.clear();
			KosturMenuManager.getInstance().openMenu(p, kostur);
			return;
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> kosturCloseEventCreator() {
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : KosturMenuManager.getInstance().getKosturFreeSlots()) {
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
	
	private EventCreator<InventoryClickEvent> kosturModifyClickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			int slot = e.getSlot();
			if(slot != 22)
				return;
			
			e.getWhoClicked().closeInventory();
			return;
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryClickEvent> kosturCreateClickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			int slot = e.getSlot();
			if(slot != 31)
				return;
			
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getWhoClicked();
			
			final int[] freeSlots = KosturMenuManager.getInstance().getCreateFreeSlots();
			for(int i = 0; i < 6; ++i) {
				int checkSlot = freeSlots[i];
				ItemStack it = inv.getItem(checkSlot);
				if(it == null
						|| it.getType().equals(Material.AIR)) {
					p.closeInventory();
					return;
				}
				
				EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
				if(!Utils.isItemSoulbindedToPlayer(it, p)) {
					p.closeInventory();
					return;
				}
				
				int kosturPart = i/2 + 1;
				if(!comp.hasKey("rozdzka_part")
						|| !comp.getString("rozdzka_part").equalsIgnoreCase(kosturPart+"")) {
					p.closeInventory();
					return;
				}
			}
			
			List<String> klejnoty = new ArrayList<>();
			for(int i = 6; i < 12; ++i) {
				int checkSlot = freeSlots[i];
				ItemStack it = inv.getItem(checkSlot);
				if(it == null
						|| it.getType().equals(Material.AIR)) {
					p.closeInventory();
					return;
				}
				
				EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
				if(!comp.hasKey("cave")
						|| comp.getString("cave").equalsIgnoreCase("crimson")) {
					p.closeInventory();
					return;
				}
				
				String klejnot = comp.getString("cave");
				if(klejnoty.contains(klejnot)) {
					p.closeInventory();
					return;
				}
				
				klejnoty.add(klejnot);
			}
			
			inv.clear();
			p.closeInventory();
			ItemStack kostur = MythicBukkit.inst().getItemManager().getItemStack("Runiczny_Kostur");
			Utils.dropItemStack(p, kostur);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> kosturCreateCloseEventCreator() {
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : KosturMenuManager.getInstance().getCreateFreeSlots()) {
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
