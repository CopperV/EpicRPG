package me.Vark123.EpicRPG.Backpacks;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

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
	private final InventoryProvider repairProvider;
	private final InventoryProvider upgradeProvider;
	
	private final int[] freeSlots;
	private final int[] repairFreeSlots;
	private final int[] upgradeFreeSlots;

	private final List<String> giantBackpack;
	private final List<String> bigBackpack;
	private final List<String> mediumBackpack;
	private final List<String> smallBackpack;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	private final ItemStack create;
	private final ItemStack bigBackpackItem;
	
	private BackpackManager() {
		
		freeSlots = new int[] {12,13,14,21,22,23,30,31,32,39,40,41};
		repairFreeSlots = new int[] {4};
		upgradeFreeSlots = new int[] {10,11,12,13,14,15,16,29};

		giantBackpack = Arrays.asList("arena2_5","arena2_12","arena2_9","arena2_10",
				"arena2_4","arena2_11","arena2_13");
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
		bigBackpackItem = new ItemStack(Material.PLAYER_HEAD, 1);{
			SkullMeta im = (SkullMeta) bigBackpackItem.getItemMeta();
			im.setDisplayName("§3§lDUZY PLECAK");
			
			final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U1YWJkYjczNzQ1NTNkMDU2NWNiY2IzMjk1YWVkNTE1YTg5N2ViY2U5ZTBiYzYwZjFjMWY4YWU1NGM3NDlkZiJ9fX0="));
            Field profileField = null;
            try {
                profileField = im.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(im, profile);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
			bigBackpackItem.setItemMeta(im);
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
		
		repairProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i) {
					if(i == 4)
						continue;
					contents.set(i, empty);
				}
			}
		};
		
		upgradeProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 36; ++i) {
					List<Integer> tmpList = Utils.intArrayToList(upgradeFreeSlots);
					if(tmpList.contains(i))
						continue;
					contents.set(i, empty);
				}
				contents.set(28, bigBackpackItem);
				contents.set(33, create);
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
	
	public void openBackpackRepairMenu(Player p) {
		RyseInventory.builder()
			.title("§3§lNaprawa plecaka")
			.size(9)
			.ignoredSlots(repairFreeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.listener(BackpackEvents.getEvents().getRepairCloseEvent())
			.provider(repairProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public void openBackpackUpgradeMenu(Player p) {
		RyseInventory.builder()
			.title("§b§lUlepszenie Plecaka")
			.size(36)
			.ignoredSlots(upgradeFreeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.listener(BackpackEvents.getEvents().getClickUpgradeEvent())
			.listener(BackpackEvents.getEvents().getCloseUpgradeEvent())
			.provider(upgradeProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
}
