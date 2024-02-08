package me.Vark123.EpicRPG.KosturSystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class KosturMenuEvents {

	private static final KosturMenuEvents container = new KosturMenuEvents();

	private final EventCreator<InventoryClickEvent> kosturClickEvent;
	private final EventCreator<InventoryCloseEvent> kosturCloseEvent;
	private final EventCreator<InventoryClickEvent> kosturModifyClickEvent;
	private final EventCreator<InventoryCloseEvent> kosturModifyCloseEvent;
	private final EventCreator<InventoryClickEvent> kosturCreateClickEvent;
	private final EventCreator<InventoryCloseEvent> kosturCreateCloseEvent;
	
	private KosturMenuEvents() {
		kosturClickEvent = kosturClickEventCreator();
		kosturCloseEvent = kosturCloseEventCreator();
		kosturModifyClickEvent = kosturModifyClickEventCreator();
		kosturModifyCloseEvent = kosturModifyCloseEventCreator();
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
			if(kostur == null || kostur.getType().equals(Material.AIR)) {
				p.closeInventory();
				return;
			}

			NBTItem nbt = new NBTItem(kostur);
			if(!nbt.hasTag("Rozdzka")) {
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
	
	private EventCreator<InventoryCloseEvent> kosturModifyCloseEventCreator() {
		Consumer<InventoryCloseEvent> event = e -> {
			ItemExecutor manag = MythicBukkit.inst().getItemManager();
			ItemStack kostur = manag.getItemStack("Runiczny_Kostur");
			NBTItem kosturNBT = new NBTItem(kostur);
			ItemMeta im = kostur.getItemMeta();
			List<String> lore = im.getLore();
			List<ItemStack> toReturn = new LinkedList<>();
			NBTItem nbt;
			
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			
			String[] strSlots = KosturMenuManager.getInstance().getRuneSlots();
			for(int i = 0; i < strSlots.length; ++i) {
				ItemStack rune = inv.getItem(10+2*i);
				if(rune == null 
						|| !rune.getType().toString().contains("DISC")) {
					kosturNBT.setString(strSlots[i], "-");
					toReturn.add(rune);
					continue;
				}
				
				nbt = new NBTItem(rune);
				if(!nbt.hasTag("MYTHIC_TYPE")) {
					kosturNBT.setString(strSlots[i], "-");
					toReturn.add(rune);
					continue;
				}
				
				String mmId = nbt.getString("MYTHIC_TYPE");
				String name = rune.getItemMeta().getDisplayName();
				lore.set(5+i, "§d☬ §7"+strSlots[i]+": "+name);
				kosturNBT.setString(strSlots[i], mmId);
			}
			
			kosturNBT.applyNBT(kostur);
			im = kostur.getItemMeta();
			im.setLore(lore);
			kostur.setItemMeta(im);
			
			Utils.dropItemStack(p, kostur);
			toReturn.forEach(it -> {
				Utils.dropItemStack(p, it);
			});
		};
		
		EventCreator<InventoryCloseEvent> creator = new EventCreator<>(InventoryCloseEvent.class, event);
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
				
				NBTItem nbt = new NBTItem(it);
				if(!nbt.hasTag("soulbind")
						|| !nbt.getString("soulbind").equalsIgnoreCase(p.getName())) {
					p.closeInventory();
					return;
				}
				
				int kosturPart = i/2 + 1;
				if(!nbt.hasTag("RozdzkaPart")
						|| !nbt.getString("RozdzkaPart").equalsIgnoreCase(kosturPart+"")) {
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
				
				NBTItem nbt = new NBTItem(it);
				if(!nbt.hasTag("cave")
						|| nbt.getString("cave").equalsIgnoreCase("crimson")) {
					p.closeInventory();
					return;
				}
				
				String klejnot = nbt.getString("cave");
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
		};
		
		EventCreator<InventoryCloseEvent> creator = new EventCreator<>(InventoryCloseEvent.class, event);
		return creator;
	}
	
}
