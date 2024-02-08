package me.Vark123.EpicRPG.Gems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class GemEvents {
	
	private static final GemEvents container = new GemEvents();
	
	private final EventCreator<InventoryClickEvent> powerfulClickEvent;
	private final EventCreator<InventoryCloseEvent> powerfulCloseEvent;

	private final EventCreator<InventoryClickEvent> annihilusClickEvent;
	private final EventCreator<InventoryCloseEvent> annihilusCloseEvent;

	private final EventCreator<InventoryClickEvent> annihilusUpgradeClickEvent;
	private final EventCreator<InventoryCloseEvent> annihilusUpgradeCloseEvent;
	
	private GemEvents() {
		powerfulClickEvent = powerfulClickEventCreator();
		powerfulCloseEvent = powerfulCloseEventCreator();

		annihilusClickEvent = annihilusClickEventCreator();
		annihilusCloseEvent = annihilusCloseEventCreator();

		annihilusUpgradeClickEvent = annihilusUpgradeClickEventCreator();
		annihilusUpgradeCloseEvent = annihilusUpgradeCloseEventCreator();
	}
	
	public static GemEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> powerfulClickEventCreator(){
		
		Consumer<InventoryClickEvent> event = e -> {
			Inventory inv = e.getClickedInventory();
			if(inv == null || !inv.getType().equals(InventoryType.CHEST)) 
				return;

			int slot = e.getSlot();
			final int[] slots = GemManager.getInstance().getPowerfulFreeSlots();
			if(Utils.intArrayToList(slots)
					.contains(slot))
				return;
			
			e.setCancelled(true);
			
			if(slot != 31)
				return;
			
			Player p = (Player) e.getWhoClicked();
			ItemStack gem = inv.getItem(slots[4]);
			if(gem == null
					|| gem.getType().equals(Material.AIR)){
				p.closeInventory();
				return;
			}
			
			NBTItem gemNBT = new NBTItem(gem);
			if(!gemNBT.hasTag("RPGType")
					|| !gemNBT.getString("RPGType").equalsIgnoreCase("gem")
					|| !gemNBT.hasTag("MYTHIC_TYPE")) {
				p.closeInventory();
				return;
			}
			
			String mmId = gemNBT.getString("MYTHIC_TYPE");
			int gemLevel = Character.getNumericValue(mmId.charAt(mmId.length()-1));
			if(gemLevel != 3) {
				p.closeInventory();
				return;
			}
			++gemLevel;

			List<String> katedraNBT = new ArrayList<>();
			for(int i = 0; i < 4; ++i) {
				int checkSlot = slots[i];
				ItemStack it = inv.getItem(checkSlot);
				if(it == null
						|| it.getType().equals(Material.AIR)) {
					p.closeInventory();
					return;
				}
				
				NBTItem nbtIt = new NBTItem(it);
				if(!nbtIt.hasTag("soulbind")
						|| !nbtIt.getString("soulbind").equalsIgnoreCase(e.getWhoClicked().getName())) {
					p.closeInventory();
					return;
				}
				if(!nbtIt.hasTag("Katedra")
						|| !GemManager.getInstance().getPowerfulGemKatedra()
							.contains(nbtIt.getString("Katedra"))
						|| katedraNBT.contains(nbtIt.getString("Katedra"))) {
					p.closeInventory();
					return;
				}
				
				katedraNBT.add(nbtIt.getString("Katedra"));
			}
			
			StringBuilder sb = new StringBuilder(mmId);
			sb.setCharAt(mmId.length()-1, Character.forDigit(gemLevel, 10));
			mmId = sb.toString();
			
			ItemStack newGem = MythicBukkit.inst().getItemManager().getItemStack(mmId);
			if(newGem == null) {
				p.closeInventory();
				return;
			}
			
			inv.clear();
			p.closeInventory();
			Utils.dropItemStack(p, newGem);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> powerfulCloseEventCreator(){
		
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : GemManager.getInstance().getPowerfulFreeSlots()) {
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
	
	private EventCreator<InventoryClickEvent> annihilusClickEventCreator(){
		
		Consumer<InventoryClickEvent> event = e -> {
			Inventory inv = e.getClickedInventory();
			if(inv == null || !inv.getType().equals(InventoryType.CHEST)) 
				return;

			int slot = e.getSlot();
			final int[] slots = GemManager.getInstance().getAnnihilusFreeSlots();
			if(Utils.intArrayToList(slots)
					.contains(slot))
				return;
			
			e.setCancelled(true);
			
			if(slot != 31)
				return;
			
			Player p = (Player) e.getWhoClicked();
			ItemStack gem1 = inv.getItem(slots[4]);
			ItemStack gem2 = inv.getItem(slots[5]);
			if(gem1 == null || gem1.getType().equals(Material.AIR)
					|| gem2 == null || gem2.getType().equals(Material.AIR)) {
				p.closeInventory();
				return;
			}
			
			NBTItem nbtGem1 = new NBTItem(gem1);
			NBTItem nbtGem2 = new NBTItem(gem2);
			if(!nbtGem1.hasTag("RPGType")
					|| !nbtGem1.getString("RPGType").equalsIgnoreCase("gem")
					|| (nbtGem1.hasTag("annihilus") && nbtGem1.getInteger("annihilus") == 1)
					|| !nbtGem2.hasTag("RPGType")
					|| !nbtGem2.getString("RPGType").equalsIgnoreCase("gem")
					|| (nbtGem2.hasTag("annihilus") && nbtGem2.getInteger("annihilus") == 1)) {
				p.closeInventory();
				return;
			}
			
			List<String> katedraNBT = new ArrayList<>();
			for(int i = 0; i < 4; ++i) {
				int checkSlot = slots[i];
				ItemStack it = inv.getItem(checkSlot);
				if(it == null
						|| it.getType().equals(Material.AIR)) {
					p.closeInventory();
					return;
				}
				
				NBTItem nbtIt = new NBTItem(it);
				if(!nbtIt.hasTag("soulbind")
						|| !nbtIt.getString("soulbind").equalsIgnoreCase(e.getWhoClicked().getName())) {
					p.closeInventory();
					return;
				}
				if(!nbtIt.hasTag("Katedra")
						|| !GemManager.getInstance().getAnnihilusGemKatedra()
							.contains(nbtIt.getString("Katedra"))
						|| katedraNBT.contains(nbtIt.getString("Katedra"))) {
					p.closeInventory();
					return;
				}
				
				katedraNBT.add(nbtIt.getString("Katedra"));
			}
			
			inv.clear();
			p.closeInventory();
			ItemStack annihilus = GemManager.getInstance().getAnnihilus(gem1, gem2);
			Utils.dropItemStack(p, annihilus);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> annihilusCloseEventCreator(){
		
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : GemManager.getInstance().getAnnihilusFreeSlots()) {
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
	private EventCreator<InventoryClickEvent> annihilusUpgradeClickEventCreator(){
		
		Consumer<InventoryClickEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			if(e.getClickedInventory() == null || !e.getClickedInventory().equals(inv))
				return;
			
			int slot = e.getSlot();
			ItemStack it = inv.getItem(slot);
			if(it == null || it.getType().equals(Material.AIR))
				return;
			
			if(!it.equals(GemManager.getInstance().getCreate()))
				return;

			final int[] slots = GemManager.getInstance().getAnnihilusUpgradeFreeSlots();
			
			Player p = (Player) e.getWhoClicked();
			ItemStack annihilus = inv.getItem(slots[7]);
			ItemStack gem = inv.getItem(slots[8]);
			if(annihilus == null || annihilus.getType().equals(Material.AIR)
					|| gem == null || gem.getType().equals(Material.AIR)) {
				p.closeInventory();
				return;
			}
			
			NBTItem nbtAnnihilus = new NBTItem(annihilus);
			NBTItem nbtGem = new NBTItem(gem);
			if(!nbtAnnihilus.hasTag("RPGType")
					|| !nbtAnnihilus.getString("RPGType").equalsIgnoreCase("gem")
					|| !(nbtAnnihilus.hasTag("annihilus") && nbtAnnihilus.getInteger("annihilus") == 1)
					|| !nbtGem.hasTag("RPGType")
					|| !nbtGem.getString("RPGType").equalsIgnoreCase("gem")
					|| (nbtGem.hasTag("annihilus") && nbtGem.getInteger("annihilus") == 1)) {
				p.closeInventory();
				return;
			}
			
			List<String> katedraNBT = new ArrayList<>();
			for(int i = 0; i < slots.length - 2; ++i) {
				int checkSlot = slots[i];
				ItemStack trophy = inv.getItem(checkSlot);
				if(trophy == null
						|| trophy.getType().equals(Material.AIR)) {
					p.closeInventory();
					return;
				}
				
				NBTItem nbtTrophy = new NBTItem(trophy);
				if(!nbtTrophy.hasTag("soulbind")
						|| !nbtTrophy.getString("soulbind").equalsIgnoreCase(e.getWhoClicked().getName())) {
					p.closeInventory();
					return;
				}
				if(!nbtTrophy.hasTag("Katedra")
						|| !GemManager.getInstance().getAnnihilusUpgradeGemKatedra()
							.contains(nbtTrophy.getString("Katedra"))
						|| katedraNBT.contains(nbtTrophy.getString("Katedra"))) {
					p.closeInventory();
					return;
				}
				
				katedraNBT.add(nbtTrophy.getString("Katedra"));
			}
			
			inv.clear();
			p.closeInventory();
			ItemStack annihilusItem = GemManager.getInstance().getAnnihilus(annihilus, gem);
			Utils.dropItemStack(p, annihilusItem);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> annihilusUpgradeCloseEventCreator(){
		
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : GemManager.getInstance().getAnnihilusUpgradeFreeSlots()) {
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
