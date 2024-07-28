package me.Vark123.EpicRPG.UpgradableSystem.Listeners;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.UpgradableSystem.InhibitorInventoryHolder;
import me.Vark123.EpicRPG.UpgradableSystem.InhibitorMenuManager;

public class InhibitorInventoryClickListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.isCancelled())
			return;
		Inventory inv = e.getView().getTopInventory();
		if (inv == null)
			return;
		InventoryHolder _holder = inv.getHolder();
		if (_holder == null || !(_holder instanceof InhibitorInventoryHolder))
			return;
		
		InhibitorInventoryHolder holder = (InhibitorInventoryHolder) _holder;
		Player p = (Player) e.getWhoClicked();
		int checkSlot;
		Inventory checkInv = e.getClickedInventory();
		
		if(checkInv == null)
			return;
		if(checkInv.equals(e.getView().getBottomInventory())) {
			if (e.getClick().equals(ClickType.SHIFT_LEFT) 
					|| e.getClick().equals(ClickType.SHIFT_RIGHT)) {
				checkSlot = e.getInventory().firstEmpty();
			} else if (e.getClick().equals(ClickType.DOUBLE_CLICK)) {
				checkSlot = -1;
			} else {
				return;
			}
		} else if (checkInv.equals(e.getView().getTopInventory())) {
			checkSlot = e.getSlot();
		} else {
			return;
		}

		
		if(!InhibitorMenuManager.get().getCraftingSlots().contains(checkSlot)) {
			e.setCancelled(true);
			if(checkSlot == 49
					&& inv.getItem(49).equals(InhibitorMenuManager.get().getGreenCreate())) {
				holder.getInhibitor().getCrafting().craft(p, holder.getInhibitor(), checkInv);
				return;
			} else if (checkSlot == 8) {
				InhibitorMenuManager.get().openRecipesCraftMenu(p, holder.getPage());
				return;
			}
			return;
		}
		
		new BukkitRunnable() {
			@Override
			public void run() {
				Collection<ItemStack> items = InhibitorMenuManager.get().getCraftingSlots()
						.stream()
						.map(inv::getItem)
						.filter(item -> item != null && !item.getType().equals(Material.AIR))
						.collect(Collectors.toList());
				inv.setItem(49, holder.getInhibitor().getCrafting().matchRecipe(p, items) ? 
						InhibitorMenuManager.get().getGreenCreate() : InhibitorMenuManager.get().getRedCreate());
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 1);
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (e.isCancelled())
			return;
		Inventory inv = e.getView().getTopInventory();
		if (inv == null)
			return;
		InventoryHolder _holder = inv.getHolder();
		if (_holder == null || !(_holder instanceof InhibitorInventoryHolder))
			return;
		
		InhibitorInventoryHolder holder = (InhibitorInventoryHolder) _holder;

		MutableBoolean cancel = new MutableBoolean();
		e.getRawSlots().stream()
			.filter(i -> !InhibitorMenuManager.get().getCraftingSlots().contains(i))
			.findFirst()
			.ifPresent(__ -> cancel.setTrue());
		if(cancel.isTrue()) {
			e.setCancelled(true);
			return;
		}

		Player p = (Player) e.getWhoClicked();
		new BukkitRunnable() {
			@Override
			public void run() {
				Collection<ItemStack> items = InhibitorMenuManager.get().getCraftingSlots()
						.stream()
						.map(inv::getItem)
						.filter(item -> item != null && !item.getType().equals(Material.AIR))
						.collect(Collectors.toList());
				inv.setItem(49, holder.getInhibitor().getCrafting().matchRecipe(p, items) ? 
						InhibitorMenuManager.get().getGreenCreate() : InhibitorMenuManager.get().getRedCreate());
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 1);
	}

}
