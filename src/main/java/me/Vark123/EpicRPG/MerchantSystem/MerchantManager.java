package me.Vark123.EpicRPG.MerchantSystem;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;

@Getter
public class MerchantManager {
	
	private static final MerchantManager instance = new MerchantManager();
	
	private final InventoryProvider provider;
	private final int[] freeSlots;
	
	private final ItemStack sell;
	private final ItemStack empty;
	private final ItemStack accept;
	private final ItemStack discard;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack sellPriceTemplate;
	
	private MerchantManager() {
		freeSlots = new int[45];
		for(int i = 0; i < freeSlots.length; ++i)
			freeSlots[i] = i;
		
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		sell = new ItemStack(Material.EMERALD, 1);{
			ItemMeta im = sell.getItemMeta();
			im.setDisplayName("§6§lSprzedaj");
			sell.setItemMeta(im);
		}
		accept = new ItemStack(Material.GREEN_TERRACOTTA, 1);{
			ItemMeta im = accept.getItemMeta();
			im.setDisplayName("§2§lPotwierdz");
			accept.setItemMeta(im);
		}
		discard = new ItemStack(Material.RED_TERRACOTTA, 1);{
			ItemMeta im = discard.getItemMeta();
			im.setDisplayName("§2§lOdrzuc");
			discard.setItemMeta(im);
		}
		sellPriceTemplate = new ItemStack(Material.GOLD_INGOT, 1);{
			ItemMeta im = sellPriceTemplate.getItemMeta();
			im.setDisplayName("§a§lWartosc sprzedazy: §e§o<price>$");
			im.setLore(Arrays.asList("§7Czy na pewno chcesz sprzedac","§7to wszystko za §e§o<price>$§7?"));
			sellPriceTemplate.setItemMeta(im);
		}
		
		provider = new InventoryProvider() {

			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i) {
					if(i == 4) {
						contents.set(i+45, sell);
						continue;
					}
					contents.set(i+45, empty);
				}
			}
		};
	}
	
	public static final MerchantManager getInstance() {
		return instance;
	}
	
	public void openSellMenu(Player p) {
		RyseInventory.builder()
			.title("§6§l§oKupiec")
			.size(54)
			.ignoredSlots(freeSlots)
//			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(MerchantEvents.getEvents().getClickEvent())
			.listener(MerchantEvents.getEvents().getCloseEvent())
			.disableUpdateTask()
			.provider(provider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public ItemStack getSellPriceTemplate() {
		return sellPriceTemplate.clone();
	}

}
