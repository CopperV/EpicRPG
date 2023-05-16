package me.Vark123.EpicRPG.Jewelry;

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

public class JewerlyMenuCloseEvent implements Listener {
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(!e.getInventory().getType().equals(InventoryType.CHEST)) return;
		String title = ChatColor.stripColor(e.getView().getTitle().toLowerCase());
		if(!title.equalsIgnoreCase("bizuteria")) return;
		Inventory inv = e.getInventory();
		Player p = (Player) e.getPlayer();
		Inventory pInv = p.getInventory();
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		rpg.getJewelry().getAkcesoria().forEach((i, item) -> {
			ItemStack it = inv.getItem(10+2*i);
			if(it == null || it.getType().equals(Material.AIR)){
				item.setItem(it);
//				rpg.getJewelry().getAkcesoria().get(i).setItem(inv.getItem(10+2*i));
				return;
			}
			
			if(!isJewerlyItem(it)) {
				p.sendMessage(it.getItemMeta().getDisplayName()+" §cnie jest amuletem, pierscieniem ani rekawicami!");
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
			
			if(!isCorrectJewerlySlotType(it, item)) {
				p.sendMessage(it.getItemMeta().getDisplayName()+" §czalozyles na niepoprawny slot!");
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
			
			item.setItem(it);
//			rpg.getJewelry().getAkcesoria().get(i).setItem(inv.getItem(10+2*i));
		});
		
		ChangeStats.change(rpg);
		inv.clear();
		
	}

	private boolean isJewerlyItem(ItemStack it) {
		NBTItem nbt = new NBTItem(it);
		return nbt.hasTag("JewerlyType");
	}
	
	private boolean isCorrectJewerlySlotType(ItemStack it, JewerlyItem jewerly) {
		NBTItem nbt = new NBTItem(it);
		String type = nbt.getString("JewerlyType").toUpperCase();
		return jewerly.getType().equals(JewerlyType.valueOf(type));
	}
	
}
