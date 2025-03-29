package me.Vark123.EpicRPG.FishSystem;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.mutable.MutableInt;
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
import me.Vark123.EpicRPG.Core.RudaSystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class FishSellEvents {

	private static final FishSellEvents container = new FishSellEvents();

	private final EventCreator<InventoryClickEvent> clickEvent;
	private final EventCreator<InventoryCloseEvent> closeEvent;
	
	private FishSellEvents() {
		clickEvent = clickEventCreator();
		closeEvent = closeEventCreator();
	}
	
	public static final FishSellEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> clickEventCreator(){
		Consumer<InventoryClickEvent> event = e -> {
			int slot = e.getSlot();
			if(slot != 49)
				return;
			
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getWhoClicked();
			List<Integer> tmpList = Utils.intArrayToList(FishSellManager.getInstance().getFreeSlots());
			List<ItemStack> toDrop = new LinkedList<>();
			
			MutableInt price = new MutableInt();
			
			tmpList.stream().filter(i -> {
				ItemStack it = inv.getItem(i);
				if(it == null || it.getType().equals(Material.AIR))
					return false;

				EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
				if(!comp.hasKey("rpgtype") 
						|| !comp.getString("rpgtype").equalsIgnoreCase("fish_to_sell")) {
					toDrop.add(it);
					return false;
				}
				if(!comp.hasKey("price")) {
					toDrop.add(it);
					return false;
				}
				return true;
			}).forEach(i -> {
				ItemStack it = inv.getItem(i);
				EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
				int toAdd = Integer.parseInt(comp.getString("price"))*it.getAmount();
				price.add(toAdd);
			});
			
			int value = price.getValue();
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			RudaSystem.getInstance().addRuda(rpg, value, "fish");
			
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
			for(int slot : FishSellManager.getInstance().getFreeSlots()) {
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
