package me.Vark123.EpicRPG.RyseInventory;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory.Builder;
import me.Vark123.EpicRPG.Main;

public class InventoryUtils {

	public static void openNormalMenu(Player p, String title, int size, int[] ignoredSlots,
			List<EventCreator<? extends Event>> listeners, Inventory inv) {
		Builder builder = RyseInventory.builder()
				.title(title)
				.size(size)
				.ignoredSlots(ignoredSlots)
				.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
				.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
				.disableUpdateTask();
		listeners.forEach(builder::listener);
		builder.provider(getProvider(inv))
			.build(Main.getInstance())
			.open(p);
		
	}
	
	public static void openConfirmationMenu(Player p, String title, int size,
			List<EventCreator<? extends Event>> listeners, Inventory inv) {
		Builder builder = RyseInventory.builder()
				.title(title)
				.size(size)
				.disableUpdateTask();
		listeners.forEach(builder::listener);
		builder.provider(getProvider(inv))
			.build(Main.getInstance())
			.open(p);
	}
	
	private static InventoryProvider getProvider(Inventory inv) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < inv.getSize(); ++i) {
					ItemStack it = inv.getItem(i);
					if(it == null || it.getType().equals(Material.AIR))
						continue;
					contents.set(i, it);
				}
			}
		};
	}

///////////////////////////////////////////////////////////////////////////////////

	public static void openNormalMenu(Player p, String title, int size, int[] ignoredSlots,
			List<EventCreator<? extends Event>> listeners, InventoryContents contents, Plugin plugin) {
		Builder builder = RyseInventory.builder()
				.title(title)
				.size(size)
				.ignoredSlots(ignoredSlots)
				.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
				.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
				.disableUpdateTask();
		listeners.forEach(builder::listener);
		builder.provider(getProvider(contents))
			.build(plugin)
			.open(p);
		
	}
	
	public static void openConfirmationMenu(Player p, String title, int size,
			List<EventCreator<? extends Event>> listeners, InventoryContents contents, Plugin plugin) {
		Builder builder = RyseInventory.builder()
				.title(title)
				.size(size)
				.disableUpdateTask();
		listeners.forEach(builder::listener);
		builder.provider(getProvider(contents))
			.build(plugin)
			.open(p);
	}
	
	private static InventoryProvider getProvider(InventoryContents cont) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				contents = cont;
			}
		};
	}
	
}
