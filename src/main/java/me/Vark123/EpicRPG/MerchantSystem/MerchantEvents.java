package me.Vark123.EpicRPG.MerchantSystem;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;

@Getter
public class MerchantEvents {

	private static final MerchantEvents container = new MerchantEvents();
	
	private final EventCreator<InventoryClickEvent> clickEvent;
	private final EventCreator<InventoryCloseEvent> closeEvent;
	
	private MerchantEvents() {
		clickEvent = clickEventCreator();
		closeEvent = closeEventCreator();
	}
	
	public static final MerchantEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> clickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			if(e.isCancelled())
				return;
			
			int slot = e.getSlot();
			Inventory inv = e.getClickedInventory();
			if(slot == 49) {
				if(!inv.getItem(slot).equals(MerchantManager.getInstance().getSell()))
					return;
				
				ItemStack sellPrice = MerchantManager.getInstance().getSellPriceTemplate();
				int totalValue = 0;
				final int[] freeSlots = MerchantManager.getInstance().getFreeSlots();
				for(int i = 0; i < freeSlots.length; ++i) {
					ItemStack it = inv.getItem(i);
					if(it == null 
							|| it.getType().equals(Material.AIR))
						continue;
					
					NBTItem nbt = new NBTItem(it);
					if(nbt.hasTag("rpg_cost")) {
						totalValue += it.getAmount()*Integer.valueOf(nbt.getString("rpg_cost"));
					} else {
						if(!it.hasItemMeta() 
								|| !it.getItemMeta().hasLore() 
								|| it.getItemMeta().getLore().size() < 2)
							continue;
						if(!it.getItemMeta().getLore().get(1).contains("Typ przedmiotu"))
							continue;
						String typ = ChatColor.stripColor(it.getItemMeta().getLore().get(1)).replace("Typ przedmiotu: ", "").toLowerCase();
						switch(typ) {
							case "trofeum":
								totalValue += (2*it.getAmount());
								continue;
							case "zwykly":
								totalValue += (5*it.getAmount());
								continue;
							case "rzadki":
								totalValue += (20*it.getAmount());
								continue;
							case "epicki":
								totalValue += (100*it.getAmount());
								continue;
							case "legendarny":
								totalValue += (3000*it.getAmount());
								continue;
						}
					}
				}
				
				inv.setItem(46, MerchantManager.getInstance().getAccept());
				inv.setItem(52, MerchantManager.getInstance().getDiscard());
				
				ItemMeta im = sellPrice.getItemMeta();
				
				String display = im.getDisplayName().replace("<price>", totalValue+"");
				List<String> lore = im.getLore();
				String line = lore.get(1).replace("<price>", totalValue+"");
				lore.set(1, line);
				
				im.setDisplayName(display);
				im.setLore(lore);
				
				sellPrice.setItemMeta(im);
				
				NBTItem sellNBT = new NBTItem(sellPrice);
				sellNBT.setInteger("sell_info", totalValue);
				sellNBT.applyNBT(sellPrice);
				inv.setItem(49, sellPrice);
				return;
			}
			
			if(slot == 52) {
				if(!inv.getItem(slot).equals(MerchantManager.getInstance().getDiscard()))
					return;
				
				inv.setItem(46, MerchantManager.getInstance().getEmpty());
				inv.setItem(52, MerchantManager.getInstance().getEmpty());
				inv.setItem(49, MerchantManager.getInstance().getSell());
				return;
			}
			
			if(slot == 46) {
				if(!inv.getItem(slot).equals(MerchantManager.getInstance().getAccept()))
					return;
				int value = new NBTItem(e.getInventory().getItem(49)).getInteger("sell_info");
				e.getInventory().clear();
				EconomyResponse r = Main.eco.depositPlayer((Player)e.getWhoClicked(), value);
				if(r.transactionSuccess()) e.getWhoClicked().sendMessage(Main.getInstance().getPrefix()+" §aOtrzymales za sprzedanie itemow u §6§l§oKupca §e"+value+"$");
				e.getWhoClicked().closeInventory();
				return;
			}
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryCloseEvent> closeEventCreator() {
		Consumer<InventoryCloseEvent> event = e -> {
			Inventory inv = e.getView().getTopInventory();
			Player p = (Player) e.getPlayer();
			for(int slot : MerchantManager.getInstance().getFreeSlots()) {
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
