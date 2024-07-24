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

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import lombok.Getter;
import me.Vark123.EpicRPG.FishSystem.FishSellManager;
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
				NBTItem nbt = new NBTItem(it);
				if(!nbt.hasTag("MYTHIC_TYPE")){
					toDrop.add(it);
					return false;
				}
				return true;
			}).forEach(i -> {
				ItemStack it = inv.getItem(i);
				NBTItem nbt = new NBTItem(it);
				String mmId = nbt.getString("MYTHIC_TYPE");
				ItemStack it2 = manag.getItemStack(mmId);
				if(it2 == null 
						|| it2.getType().equals(Material.AIR)){
					toDrop.add(it);
					return;
				}
				
				it2.setAmount(it.getAmount());
				NBTItem nbt2 = new NBTItem(it2);
				nbt.getKeys().stream().filter(key -> {
					return (key.contains("Random") && !key.equals("Random"));
				}).findAny().ifPresent(s -> {
					Random rand = new Random();
					nbt2.setInteger("Random"+rand.nextInt(), rand.nextInt());
					nbt2.applyNBT(it2);
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
			for(int slot : FishSellManager.getInstance().getFreeSlots()) {
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
