package me.Vark123.EpicRPG.Backpacks;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class BackpackManager {

	private static final BackpackManager instance = new BackpackManager();
	
	private final InventoryProvider creatorProvider;
	private final int[] freeSlots;
	private final List<String> bigBackpack;
	private final List<String> mediumBackpack;
	private final List<String> smallBackpack;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack create;
	
	private BackpackManager() {
		
		freeSlots = new int[] {12,13,14,21,22,23,30,31,32,39,40,41};
		bigBackpack = Arrays.asList("arena16","arena18","arena10","arena15"
				,"arena5","arena13","arena3","arena12"
				,"arena2","arena1","arena14","arena6");
		mediumBackpack = Arrays.asList("arena2","arena1","arena14","arena6"
				,"arena5","arena13","arena3","arena12");
		smallBackpack = Arrays.asList("arena2","arena1","arena14","arena6");
		
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		create = new ItemStack(Material.EMERALD, 1);{
			ItemMeta im = create.getItemMeta();
			im.setDisplayName("§6§lStworz");
			create.setItemMeta(im);
		}
		
		creatorProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(freeSlots);
				for(int i = 0; i < 54; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 49) {
						contents.set(i, create);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
	}
	
	public static BackpackManager getInstance() {
		return instance;
	}
	
	public void openBackpackCreatorMenu(Player p) {
		RyseInventory.builder()
			.title("§3§lTworzenie Plecakow")
			.size(54)
			.ignoredSlots(freeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.listener(BackpackEvents.getEvents().getClickEvent())
			.listener(BackpackEvents.getEvents().getCloseEvent())
			.provider(creatorProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
}
