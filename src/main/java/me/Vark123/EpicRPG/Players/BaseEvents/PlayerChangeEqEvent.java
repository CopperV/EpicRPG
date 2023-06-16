package me.Vark123.EpicRPG.Players.BaseEvents;

import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Stats.CheckStats;
import net.minecraft.world.item.ItemArmor;

public class PlayerChangeEqEvent implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.isCancelled())
			return;
		
		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getClickedInventory();
		if(inv == null
				|| !inv.getType().equals(InventoryType.PLAYER))
			return;
		if(!(p.getGameMode().equals(GameMode.SURVIVAL) 
				|| p.getGameMode().equals(GameMode.ADVENTURE)))
			return;
		
		ItemStack toCheck = null;
		if(e.getSlotType().equals(SlotType.ARMOR)) {
			if(e.getClick().equals(ClickType.NUMBER_KEY)) {
				toCheck = inv.getItem(e.getHotbarButton());
			} else {
				toCheck = e.getCursor();
			}
		}
		else if(e.isShiftClick()) {
			if(e.getCurrentItem() == null)
				return;
			String name = e.getCurrentItem().getType().name();
			if(name.contains("BOOTS")) {
				if(p.getInventory().getBoots() != null)
					return;
				toCheck = e.getCurrentItem();
			}
			if(name.contains("LEGGINGS")) {
				if(p.getInventory().getLeggings() != null)
					return;
				toCheck = e.getCurrentItem();
			}
			if(name.contains("CHESTPLATE")) {
				if(p.getInventory().getChestplate() != null)
					return;
				toCheck = e.getCurrentItem();
			}
			if(name.contains("ELYTRA")) {
				if(p.getInventory().getChestplate() != null)
					return;
				toCheck = e.getCurrentItem();
			}
			if(name.contains("HELMET")) {
				if(p.getInventory().getHelmet() != null)
					return;
				toCheck = e.getCurrentItem();
			}
		}
		if(toCheck == null)
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(!CheckStats.check(rpg, toCheck)) {
			p.sendMessage("§cNie mozesz tego na siebie zalozyc!");
			e.setCancelled(true);
			e.setResult(Result.DENY);
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				ChangeStats.change(rpg, p.getInventory().getItemInMainHand());
			}
		}.runTaskLater(Main.getInstance(), 1);
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;
		
		Player p = e.getPlayer();
		ItemStack toCheck = e.getItem();
		if(toCheck == null
				|| toCheck.getType().equals(Material.AIR))
			return;
		if(!(CraftItemStack.asNMSCopy(toCheck).c() instanceof ItemArmor)
				&& !toCheck.getType().equals(Material.ELYTRA))
			return;
		
		String name = toCheck.getItemMeta().getDisplayName();
		if(name.contains("BOOTS")) {
			if (p.getInventory().getBoots() != null)
				return;
		}
		else if(name.contains("LEGGINGS")) {
			if(p.getInventory().getLeggings() != null)
				return;
		}
		else if(name.contains("CHESTPLATE")) {
			if(p.getInventory().getChestplate() != null)
				return;
		}
		else if(name.contains("ELYTRA")) {
			if(p.getInventory().getChestplate() != null)
				return;
		}
		else if(name.contains("HELMET")) {
			if(p.getInventory().getHelmet() != null)
				return;
		}
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(!CheckStats.check(rpg, toCheck)) {
			p.sendMessage("§cNie mozesz tego na siebie zalozyc!");
			e.setCancelled(true);
			e.setUseInteractedBlock(Result.DENY);
			e.setUseItemInHand(Result.DENY);
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				ChangeStats.change(rpg, p.getInventory().getItemInMainHand());
			}
		}.runTaskLater(Main.getInstance(), 1);
		
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if(e.isCancelled())
			return;
		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		if(inv == null
				|| !inv.getType().equals(InventoryType.CRAFTING))
			return;
		
		ItemStack toCheck = e.getOldCursor();
		if(toCheck == null
				|| toCheck.getType().equals(Material.AIR))
			return;

		String type = e.getOldCursor().getType().name().toUpperCase();
		if(!(type.contains("BOOTS") 
				|| type.contains("LEGGINGS") 
				|| type.contains("CHESTPLATE") 
				|| type.contains("HELMET") 
				|| type.contains("ELYTRA")))
			return;
		
		Set<Integer> slot = e.getRawSlots();
		if(!(slot.contains(5) || slot.contains(6) || slot.contains(7) || slot.contains(8)))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(!CheckStats.check(rpg, toCheck)) {
			p.sendMessage("§cNie mozesz tego na siebie zalozyc!");
			e.setCancelled(true);
			e.setResult(Result.DENY);
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				ChangeStats.change(rpg, p.getInventory().getItemInMainHand());
			}
		}.runTaskLater(Main.getInstance(), 1);
	}

}
