package me.Vark123.EpicRPG.RubySystem;

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
public class RubyMenuManager {

	private static final RubyMenuManager instance = new RubyMenuManager();
	
	private final InventoryProvider warpedProvider;
	private final InventoryProvider kyrianProvider;
	
	private final int[] freeSlots;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack create;

	@Getter(value = AccessLevel.NONE)
	private final ItemStack warped1;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack warped2;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack warped3;

	@Getter(value = AccessLevel.NONE)
	private final ItemStack kyrian1;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack kyrian2;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack kyrian3;
	
	private RubyMenuManager() {
		freeSlots = new int[] {11,20,13,22,15,24};
		
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
		
		warped1 = MythicBukkit.inst().getItemManager().getItemStack("HPRubyCrafting_1");
		warped2 = MythicBukkit.inst().getItemManager().getItemStack("HPRubyCrafting_2");
		warped3 = MythicBukkit.inst().getItemManager().getItemStack("HPRubyCrafting_3");
		
		kyrian1 = MythicBukkit.inst().getItemManager().getItemStack("ManaRubyCrafting_1");
		kyrian2 = MythicBukkit.inst().getItemManager().getItemStack("ManaRubyCrafting_2");
		kyrian3 = MythicBukkit.inst().getItemManager().getItemStack("ManaRubyCrafting_3");
		
		warpedProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(freeSlots);
				for(int i = 0; i < 36; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 31) {
						contents.set(i, create);
						continue;
					}
					if(i == 2) {
						contents.set(i, warped1);
						continue;
					}
					if(i == 4) {
						contents.set(i, warped2);
						continue;
					}
					if(i == 6) {
						contents.set(i, warped3);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
		
		kyrianProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(freeSlots);
				for(int i = 0; i < 36; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 31) {
						contents.set(i, create);
						continue;
					}
					if(i == 2) {
						contents.set(i, kyrian1);
						continue;
					}
					if(i == 4) {
						contents.set(i, kyrian2);
						continue;
					}
					if(i == 6) {
						contents.set(i, kyrian3);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
	}
	
	public static final RubyMenuManager getInstance() {
		return instance;
	}
	
	public void openWarpedMenu(Player p) {
		RyseInventory.builder()
			.title("§c§lSpaczone rubiny")
			.size(36)
			.ignoredSlots(freeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(RubyMenuEvents.getEvents().getWarpedClickEvent())
			.listener(RubyMenuEvents.getEvents().getCloseEvent())
			.disableUpdateTask()
			.provider(warpedProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public void openKyrianMenu(Player p) {
		RyseInventory.builder()
			.title("§c§lSpaczone rubiny")
			.size(36)
			.ignoredSlots(freeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(RubyMenuEvents.getEvents().getKyrianClickEvent())
			.listener(RubyMenuEvents.getEvents().getCloseEvent())
			.disableUpdateTask()
			.provider(kyrianProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
}
