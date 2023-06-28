package me.Vark123.EpicRPG.RubySystem;

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
public class RubyMenuEvents {
	
	private static final RubyMenuEvents container = new RubyMenuEvents();
	
	private final EventCreator<InventoryClickEvent> warpedClickEvent;
	private final EventCreator<InventoryClickEvent> kyrianClickEvent;
	private final EventCreator<InventoryCloseEvent> closeEvent;
	
	private RubyMenuEvents() {
		warpedClickEvent = warpedClickEventCreator();
		kyrianClickEvent = kyrianClickEventCreator();
		closeEvent = closeEventCreator();
	}
	
	public static final RubyMenuEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> warpedClickEventCreator(){
		Consumer<InventoryClickEvent> event = e -> {
			if(e.isCancelled())
				return;
			
			int slot = e.getSlot();
			if(slot != 31)
				return;
			
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getWhoClicked();
			
			int correct = 0;
			final int[] freeSlots = RubyManager.getInstance().getFreeSlots();
			for(int i = 0; i < freeSlots.length; ++i) {
				int checkSlot = freeSlots[i];
				ItemStack it = inv.getItem(checkSlot);
				if(it == null
						|| it.getType().equals(Material.AIR))
					break;
				
				NBTItem nbt = new NBTItem(it);
				if(!nbt.hasTag("soulbind")
						|| !nbt.getString("soulbind").equalsIgnoreCase(e.getWhoClicked().getName()))
					break;
				
				int rubyCraftLevel = i/2 + 1;
				
				if(!nbt.hasTag("RPGType")
						|| !nbt.getString("RPGType").equalsIgnoreCase("hpcrafting_ruby"+rubyCraftLevel))
					break;
				++correct;
			}
			
			if(correct == 6) {
				inv.clear();
				ItemStack it = MythicBukkit.inst().getItemManager().getItemStack("HPRuby_3");
				Utils.dropItemStack(p, it);
			} else if(correct >= 4) {
				for(int i = 0; i < 4; ++i) {
					int tmpSlot = freeSlots[i];
					inv.setItem(tmpSlot, new ItemStack(Material.AIR));
				}
				ItemStack it = MythicBukkit.inst().getItemManager().getItemStack("HPRuby_2");
				Utils.dropItemStack(p, it);
			} else if(correct >= 2) {
				for(int i = 0; i < 2; ++i) {
					int tmpSlot = freeSlots[i];
					inv.setItem(tmpSlot, new ItemStack(Material.AIR));
				}
				ItemStack it = MythicBukkit.inst().getItemManager().getItemStack("HPRuby_1");
				Utils.dropItemStack(p, it);
			}
			
			p.closeInventory();
			return;
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryClickEvent> kyrianClickEventCreator(){
		Consumer<InventoryClickEvent> event = e -> {
			if(e.isCancelled())
				return;
			
			int slot = e.getSlot();
			if(slot != 31)
				return;
			
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getWhoClicked();
			
			int correct = 0;
			final int[] freeSlots = RubyManager.getInstance().getFreeSlots();
			for(int i = 0; i < freeSlots.length; ++i) {
				int checkSlot = freeSlots[i];
				ItemStack it = inv.getItem(checkSlot);
				if(it == null
						|| it.getType().equals(Material.AIR))
					break;
				
				NBTItem nbt = new NBTItem(it);
				if(!nbt.hasTag("soulbind")
						|| !nbt.getString("soulbind").equalsIgnoreCase(e.getWhoClicked().getName()))
					break;
				
				int rubyCraftLevel = i/2 + 1;
				
				if(!nbt.hasTag("RPGType")
						|| !nbt.getString("RPGType").equalsIgnoreCase("manacrafting_ruby"+rubyCraftLevel))
					break;
				++correct;
			}
			
			if(correct == 6) {
				inv.clear();
				ItemStack it = MythicBukkit.inst().getItemManager().getItemStack("ManaRuby_3");
				Utils.dropItemStack(p, it);
			} else if(correct >= 4) {
				for(int i = 0; i < 4; ++i) {
					int tmpSlot = freeSlots[i];
					inv.setItem(tmpSlot, new ItemStack(Material.AIR));
				}
				ItemStack it = MythicBukkit.inst().getItemManager().getItemStack("ManaRuby_2");
				Utils.dropItemStack(p, it);
			} else if(correct >= 2) {
				for(int i = 0; i < 2; ++i) {
					int tmpSlot = freeSlots[i];
					inv.setItem(tmpSlot, new ItemStack(Material.AIR));
				}
				ItemStack it = MythicBukkit.inst().getItemManager().getItemStack("ManaRuby_1");
				Utils.dropItemStack(p, it);
			}
			
			p.closeInventory();
			return;
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> closeEventCreator(){
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : RubyManager.getInstance().getFreeSlots()) {
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
