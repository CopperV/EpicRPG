package me.Vark123.EpicRPG.Jewelry.Listeners;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.Jewelry.BaseJewelryMenu;
import me.Vark123.EpicRPG.Jewelry.JewelryMenuManager;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgJewelry;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Utils.Utils;

public class JewelryMenuInteractListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.isCancelled())
			return;
		
		Inventory inv = e.getView().getTopInventory();
		if (inv.getHolder() == null || !(inv.getHolder() instanceof BaseJewelryMenu))
			return;
		if (e.getClickedInventory() == null || !e.getClickedInventory().equals(inv))
			return;
		
		List<Integer> tmpList = Utils.intArrayToList(JewelryMenuManager.getInstance().getFreeSlots());
		int slot = e.getSlot();
		if(!tmpList.contains(slot)) {
			e.setCancelled(true);
			return;
		}
		
		ItemStack it = inv.getItem(slot);
		if(it == null || it.getType().equals(Material.AIR))
			return;
		
		Player p = (Player) e.getWhoClicked();
		if (!(p.getGameMode().equals(GameMode.SURVIVAL) || p.getGameMode().equals(GameMode.ADVENTURE)))
			return;
		Utils.resetSetInfo(it);
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Inventory inv = e.getView().getTopInventory();
		if (inv.getHolder() == null || !(inv.getHolder() instanceof BaseJewelryMenu))
			return;
		Player p = ((BaseJewelryMenu) inv.getHolder()).getOwner();
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgJewelry jewelry = rpg.getJewelry();
		jewelry.getAkcesoria().forEach((i, item) -> {
			ItemStack it = inv.getItem(10 + 2*i);
			if(it == null
					|| it.getType().equals(Material.AIR)) {
				item.setItem(it);
				return;
			}

			Utils.resetSetInfo(it);
			if(!JewelryMenuManager.getInstance().isJewelryItem(it)) {
				p.getPlayer().sendMessage(it.getItemMeta().getDisplayName()+"§cnie jest amuletem, pierscieniem ani rekawicami!");
				Utils.dropItemStack(p, it);
				item.setItem(null);
				return;
			}
			
			if(!JewelryMenuManager.getInstance().isCorrectJewelrySlotType(it, item)) {
				p.sendMessage(it.getItemMeta().getDisplayName()+"§czalozyles na niepoprawny slot!");
				Utils.dropItemStack(p, it);
				item.setItem(null);
				return;
			}
			
			item.setItem(it);
		});
		
		ChangeStats.change(rpg);
	}

}
