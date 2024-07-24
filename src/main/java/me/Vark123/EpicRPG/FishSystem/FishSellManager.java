package me.Vark123.EpicRPG.FishSystem;

import java.util.List;

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
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class FishSellManager {

	private static final FishSellManager instance = new FishSellManager();
	
	private final InventoryProvider provider;
	private final int[] freeSlots;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack sell;
	
	private FishSellManager() {
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
			im.setDisplayName("§a§oSprzedaj");
			sell.setItemMeta(im);
		}
		
		provider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(freeSlots);
				for(int i = 0; i < 54; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 49) {
						contents.set(i, sell);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
	}
	
	public static final FishSellManager getInstance() {
		return instance;
	}
	
	public void open(Player p) {
		RyseInventory.builder()
			.title("§b§lKupiec ryb")
			.size(54)
			.ignoredSlots(freeSlots)
//			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(FishSellEvents.getEvents().getClickEvent())
			.listener(FishSellEvents.getEvents().getCloseEvent())
			.disableUpdateTask()
			.provider(provider)
			.build(Main.getInstance())
			.open(p);
	}
	
}
