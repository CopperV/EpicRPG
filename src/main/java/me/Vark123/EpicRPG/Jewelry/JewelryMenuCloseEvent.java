package me.Vark123.EpicRPG.Jewelry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import net.md_5.bungee.api.ChatColor;

@Deprecated
public class JewelryMenuCloseEvent implements Listener {
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(!e.getInventory().getType().equals(InventoryType.CHEST)) return;
		String title = ChatColor.stripColor(e.getView().getTitle().toLowerCase());
		if(!title.equalsIgnoreCase("bizuteria")) return;
		Inventory inv = e.getInventory();
		Player p = (Player) e.getPlayer();
		Inventory pInv = p.getInventory();
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);

		Bukkit.broadcastMessage("BukkitClose "+rpg.getPlayer().getName());
		rpg.getJewelry().getAkcesoria().forEach((i, item) -> {
			ItemStack it = inv.getItem(10+2*i);
			Bukkit.broadcastMessage("Test1a");
			if(it == null || it.getType().equals(Material.AIR)){
				item.setItem(it);
//				rpg.getJewelry().getAkcesoria().get(i).setItem(inv.getItem(10+2*i));
				return;
			}

			Bukkit.broadcastMessage("Test1b");
			if(!isJewerlyItem(it)) {
				p.sendMessage(it.getItemMeta().getDisplayName()+"§cnie jest amuletem, pierscieniem ani rekawicami!");
				int firstEmpty = pInv.firstEmpty();
				if(firstEmpty < 0 || firstEmpty > 35) {
					p.getWorld().dropItem(p.getLocation(), it);
				} else {
					pInv.setItem(firstEmpty, it);
				}
				inv.setItem(10+2*i, null);
				item.setItem(null);
//				rpg.getJewelry().getAkcesoria().get(i).setItem(null);
				return;
			}

			Bukkit.broadcastMessage("Test1c");
			if(!isCorrectJewerlySlotType(it, item)) {
				p.sendMessage(it.getItemMeta().getDisplayName()+"§czalozyles na niepoprawny slot!");
				int firstEmpty = pInv.firstEmpty();
				if(firstEmpty < 0 || firstEmpty > 35) {
					p.getWorld().dropItem(p.getLocation(), it);
				} else {
					pInv.setItem(firstEmpty, it);
				}
				inv.setItem(10+2*i, null);
				item.setItem(null);
//				rpg.getJewelry().getAkcesoria().get(i).setItem(null);
				return;
			}

			Bukkit.broadcastMessage("Test1d");
			item.setItem(it);
			
			Bukkit.broadcastMessage("Test1e "+item.getItem().getItemMeta().getDisplayName());
			Bukkit.broadcastMessage("Test1f "+rpg.getJewelry().getAkcesoria().get(i).getItem().getItemMeta().getDisplayName());
//			rpg.getJewelry().getAkcesoria().get(i).setItem(inv.getItem(10+2*i));
		});
		
		ChangeStats.change(rpg);
		inv.clear();
		
		rpg.getJewelry().getAkcesoria().forEach((i, item) -> {
			if(item.getItem() == null)
				Bukkit.broadcastMessage(i+" null");
			else
				Bukkit.broadcastMessage(i+" "+item.getItem().getItemMeta().getDisplayName());
		});
		
	}

	private boolean isJewerlyItem(ItemStack it) {
		NBTItem nbt = new NBTItem(it);
		return nbt.hasTag("JewerlyType");
	}
	
	private boolean isCorrectJewerlySlotType(ItemStack it, JewelryItem jewerly) {
		NBTItem nbt = new NBTItem(it);
		String type = nbt.getString("JewerlyType").toUpperCase();
		return jewerly.getType().equals(JewelryType.valueOf(type));
	}
	
}
