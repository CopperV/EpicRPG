package me.Vark123.EpicRPG.SzponBeliara;

import java.util.ArrayList;
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
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class SzponBeliaraManager {

	private static final SzponBeliaraManager instance = new SzponBeliaraManager();
	
	private final InventoryProvider awakeningProvider;
	private final int[] awakeningFreeSlots;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack create;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack asleepSzpon;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack pakt;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack kamienSzlachetny;
	
	private SzponBeliaraManager() {
		awakeningFreeSlots = new int[] {11,13,15};
		
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		create = new ItemStack(Material.EMERALD, 1);{
			ItemMeta im = create.getItemMeta();
			im.setDisplayName("§6§lUlepsz");
			create.setItemMeta(im);
		}
		asleepSzpon = MythicBukkit.inst().getItemManager().getItemStack("Szpon_Beliara_Weak_Str");
		pakt = MythicBukkit.inst().getItemManager().getItemStack("Pakt_Z_Beliarem");{
			ItemMeta meta = pakt.getItemMeta();
			meta.setLore(new ArrayList<>());
			pakt.setItemMeta(meta);
		}
		kamienSzlachetny = MythicBukkit.inst().getItemManager().getItemStack("Opal");{
			ItemMeta meta = kamienSzlachetny.getItemMeta();
			meta.setDisplayName("§b§lKamien Szlachetny");
			meta.setLore(new ArrayList<>());
			kamienSzlachetny.setItemMeta(meta);
		}
		
		awakeningProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(awakeningFreeSlots);
				for(int i = 0; i < 27; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 2) {
						contents.set(i, asleepSzpon);
						continue;
					}
					if(i == 4) {
						contents.set(i, pakt);
						continue;
					}
					if(i == 6) {
						contents.set(i, kamienSzlachetny);
						continue;
					}
					if(i == 22) {
						contents.set(i, create);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
	}
	
	public static final SzponBeliaraManager getInstance() {
		return instance;
	}
	
	public void openSzponAwakeningMenu(Player p) {
		RyseInventory.builder()
			.title("§5§lUlepszanie Szponu Beliara")
			.size(27)
			.ignoredSlots(awakeningFreeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(SzponBeliaraEvents.getEvents().getAwakeningClickEvent())
			.listener(SzponBeliaraEvents.getEvents().getAwakeningCloseEvent())
			.disableUpdateTask()
			.provider(awakeningProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
}
