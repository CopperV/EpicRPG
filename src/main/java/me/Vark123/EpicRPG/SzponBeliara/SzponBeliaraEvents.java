package me.Vark123.EpicRPG.SzponBeliara;

import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class SzponBeliaraEvents {
	
	private static final SzponBeliaraEvents container = new SzponBeliaraEvents();
	
	private final EventCreator<InventoryClickEvent> awakeningClickEvent;
	private final EventCreator<InventoryCloseEvent> awakeningCloseEvent;
	
	private SzponBeliaraEvents() {
		awakeningClickEvent = awakeningClickEventCreator();
		awakeningCloseEvent = awakeningCloseEventCreator();
	}
	
	public static SzponBeliaraEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> awakeningClickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			int[] freeSlots = SzponBeliaraManager.getInstance().getAwakeningFreeSlots();
			int slot = e.getSlot();
			if(slot != 22)
				return;
			
			Player p = (Player) e.getWhoClicked();
			Inventory inv = e.getClickedInventory();
			ItemStack szpon = inv.getItem(freeSlots[0]);
			ItemStack pakt = inv.getItem(freeSlots[1]);
			ItemStack kamien = inv.getItem(freeSlots[2]);
			if(szpon == null || szpon.getType().equals(Material.AIR)
					|| pakt == null || pakt.getType().equals(Material.AIR)
					|| kamien == null || kamien.getType().equals(Material.AIR)) {
				p.closeInventory();
				return;
			}

			NBTItem nbtSzpon = new NBTItem(szpon);
			NBTItem nbtPakt = new NBTItem(pakt);
			NBTItem nbtKamien = new NBTItem(kamien);
			if(!(nbtSzpon.hasTag("SzponBeliara") && !nbtSzpon.hasTag("FreeSlots"))
					|| !nbtPakt.hasTag("SzponUpgrade")
					|| !nbtKamien.hasTag("cave")) {
				p.closeInventory();
				return;
			}

			ItemStack toDrop = null;
			switch(nbtKamien.getString("cave").toLowerCase()) {
				case "topaz":
					toDrop = MythicBukkit.inst().getItemManager().getItemStack("Szpon_Beliara_Str");
					break;
				case "bursztyn":
					toDrop = MythicBukkit.inst().getItemManager().getItemStack("Szpon_Beliara_Wytrz");
					break;
				case "jadeit":
					toDrop = MythicBukkit.inst().getItemManager().getItemStack("Szpon_Beliara_Zr");
					break;
				case "opal":
					toDrop = MythicBukkit.inst().getItemManager().getItemStack("Szpon_Beliara_Zd");
					break;
				case "szafir":
					toDrop = MythicBukkit.inst().getItemManager().getItemStack("SzponBeliaraMana");
					break;
				case "ametyst":
					toDrop = MythicBukkit.inst().getItemManager().getItemStack("SzponBeliaraInt");
					break;
			}
			if(toDrop == null) {
				p.closeInventory();
				return;
			}
			
			Utils.dropItemStack(p, toDrop);
			inv.clear();
			p.closeInventory();
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> awakeningCloseEventCreator() {
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : SzponBeliaraManager.getInstance().getAwakeningFreeSlots()) {
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
