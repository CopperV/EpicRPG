package me.Vark123.EpicRPG.Klejnoty;

import java.util.LinkedList;
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
public class KlejnotyManager {

	private static final KlejnotyManager instance = new KlejnotyManager();

	@Getter(value = AccessLevel.NONE)
	public final int REMOVE_COST;
	
	private final InventoryProvider creatorProvider;
	private final InventoryProvider removeProvider;
	private final int[] insertFreeSlots;
	private final int[] removeFreeSlots;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack create;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack remove;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack item;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack item2;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack klejnot;
	
	private KlejnotyManager() {
		REMOVE_COST = 100_000;
		
		insertFreeSlots = new int[] {11,15};
		removeFreeSlots = new int[] {13};
		
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		create = new ItemStack(Material.EMERALD, 1);{
			ItemMeta im = create.getItemMeta();
			im.setDisplayName("§6§lWloz");
			create.setItemMeta(im);
		}
		remove = new ItemStack(Material.EMERALD, 1);{
			ItemMeta im = remove.getItemMeta();
			im.setDisplayName("§6§lWyjmij");
			List<String> lore = new LinkedList<>();
			lore.add("§aKoszt: §c§o100 000 Smoczych Monet");
			im.setLore(lore);
			remove.setItemMeta(im);
		}
		item = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);{
			ItemMeta im = item.getItemMeta();
			im.setDisplayName("§2§lPrzedmiot z wolnym slotem");
			item.setItemMeta(im);
		}
		item2 = new ItemStack(Material.NETHERITE_SWORD, 1);{
			ItemMeta im = item2.getItemMeta();
			im.setDisplayName("§2§lPrzedmiot z klejnotem");
			item2.setItemMeta(im);
		}
		klejnot = new ItemStack(Material.DIAMOND, 1);{
			ItemMeta im = klejnot.getItemMeta();
			im.setDisplayName("§b§lKlejnot");
			klejnot.setItemMeta(im);
		}
		
		creatorProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(insertFreeSlots);
				for(int i = 0; i < 27; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 22) {
						contents.set(i, create);
						continue;
					}
					if(i == 2) {
						contents.set(i, item);
						continue;
					}
					if(i == 6) {
						contents.set(i, klejnot);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
		removeProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(removeFreeSlots);
				for(int i = 0; i < 27; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 22) {
						contents.set(i, remove);
						continue;
					}
					if(i == 4) {
						contents.set(i, item2);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
		
	}
	
	public static final KlejnotyManager getInstance() {
		return instance;
	}
	
	public void openInsertMenu(Player p) {
		RyseInventory.builder()
			.title("§6§lWkladanie Klejnotow")
			.size(27)
			.ignoredSlots(insertFreeSlots)
//			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.listener(KlejnotyEvents.getEvents().getInsertClickEvent())
			.listener(KlejnotyEvents.getEvents().getInsertCloseEvent())
			.provider(creatorProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public void openRemoveMenu(Player p) {
		RyseInventory.builder()
			.title("§6§lWyjmowanie Klejnotow")
			.size(27)
			.ignoredSlots(removeFreeSlots)
//			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.listener(KlejnotyEvents.getEvents().getRemoveClickEvent())
			.listener(KlejnotyEvents.getEvents().getRemoveCloseEvent())
			.provider(removeProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
}
