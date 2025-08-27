package me.Vark123.EpicRPG.RuneSystem.SummonSystem;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicInventory.Content.IntelligentItem;
import me.Vark123.EpicInventory.Content.InventoryContents;
import me.Vark123.EpicInventory.Content.InventoryProvider;
import me.Vark123.EpicInventory.Enums.Action;
import me.Vark123.EpicInventory.Enums.DisabledEvents;
import me.Vark123.EpicInventory.Enums.DisabledInventoryClick;
import me.Vark123.EpicInventory.Pagination.EpicInventory;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;

public final class SummonControllerMenuManager {

	private static final SummonControllerMenuManager inst = new SummonControllerMenuManager();

	private final int[] controllerFreeSlots;
	private final String[] controllerSlots;
	
	private final ItemStack empty;
	private final ItemStack emptyCommand;
	private final ItemStack modify;
	private final ItemStack controllerItem;
	
	private final ItemStack pppItem;
	private final ItemStack pplItem;
	private final ItemStack plpItem;
	private final ItemStack pllItem;

	private final ItemExecutor manag;
	private final String controllerStringName;
	
	private SummonControllerMenuManager() {
		manag = MythicBukkit.inst().getItemManager();
		controllerStringName = manag.getItemStack("Sigil_Dominacji").getItemMeta().getDisplayName();

		controllerFreeSlots = new int[] {13};
		
		controllerSlots = new String[] {"ppp","ppl","plp","pll"};
		
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		emptyCommand = new ItemStack(Material.BARRIER, 1);{
			ItemMeta im = emptyCommand.getItemMeta();
			im.setDisplayName("§c§lBRAK");
			emptyCommand.setItemMeta(im);
		}
		modify = new ItemStack(Material.EMERALD, 1);{
			ItemMeta im = modify.getItemMeta();
			im.setDisplayName("§6§lModyfikuj");
			modify.setItemMeta(im);
		}
		controllerItem = new ItemStack(Material.RECOVERY_COMPASS);{
			ItemMeta im = controllerItem.getItemMeta();
			im.setDisplayName(controllerStringName);
			controllerItem.setItemMeta(im);
		}
		
		pppItem = new ItemStack(Material.GOAT_HORN);{
			ItemMeta im = pppItem.getItemMeta();
			im.setDisplayName("§2§lKombinacja PPP");
			im.setLore(Arrays.asList("§aUmozliwia wydanie rozkazu przyzwancom","§auzywajac kombinacji myszy", "§aprawy-prawy-prawy"));
			pppItem.setItemMeta(im);
		}
		pplItem = new ItemStack(Material.GOAT_HORN);{
			ItemMeta im = pplItem.getItemMeta();
			im.setDisplayName("§2§lKombinacja PPL");
			im.setLore(Arrays.asList("§aUmozliwia wydanie rozkazu przyzwancom","§auzywajac kombinacji myszy", "§aprawy-prawy-lewy"));
			pplItem.setItemMeta(im);
		}
		plpItem = new ItemStack(Material.GOAT_HORN);{
			ItemMeta im = plpItem.getItemMeta();
			im.setDisplayName("§2§lKombinacja PLP");
			im.setLore(Arrays.asList("§aUmozliwia wydanie rozkazu przyzwancom","§auzywajac kombinacji myszy", "§aprawy-lewy-prawy"));
			plpItem.setItemMeta(im);
		}
		pllItem = new ItemStack(Material.GOAT_HORN);{
			ItemMeta im = pllItem.getItemMeta();
			im.setDisplayName("§2§lKombinacja PLL");
			im.setLore(Arrays.asList("§aUmozliwia wydanie rozkazu przyzwancom","§auzywajac kombinacji myszy", "§aprawy-lewy-lewy"));
			pllItem.setItemMeta(im);
		}
	}
	
	public static final SummonControllerMenuManager getInst() {
		return inst;
	}
	
	public void openModifyMenu(Player player) {
		EpicInventory.builder()
			.title(controllerStringName)
			.rows(3)
			.ignoredSlots(controllerFreeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					List<Integer> tmpList = Utils.intArrayToList(controllerFreeSlots);
					for(int i = 0; i < 27; ++i) {
						if(tmpList.contains(i))
							continue;
						contents.set(i, empty);
					}
					
					contents.set(4, controllerItem);
					contents.set(22, IntelligentItem.of(modify, event -> {
						Inventory inv = event.getClickedInventory();
						ItemStack item = inv.getItem(13);
						if(item == null || item.getType().equals(Material.AIR)
								|| !Utils.canUseItem(item, player))
							return;

						EpicComponent comp = new EpicComponent(item, MythicBukkit.inst());
						if(!comp.hasKey("summon_controller"))
							return;
						
						inv.clear();
						openCommandMenu(player, item);
					}));
				}

				@Override
				public void close(Player player, EpicInventory inventory) {
					for(int slot : controllerFreeSlots) {
						ItemStack it = inventory.getInventory().getItem(slot);
						if(it == null || it.getType().equals(Material.AIR))
							return;
						
						Utils.dropItemStack(player, it);
					}
				}
			})
			.build(Main.getInstance())
			.open(player);
	}
	
	private void openCommandMenu(Player player, ItemStack item) {
		MutableObject<ItemStack> itemContainer = new MutableObject<>(item);
		
		EpicInventory.builder()
			.title(controllerStringName)
			.rows(3)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 27; ++i) {
						contents.set(i, empty);
					}
					
					contents.set(1, pppItem);
					contents.set(3, pplItem);
					contents.set(5, plpItem);
					contents.set(7, pllItem);
					
					contents.set(22, IntelligentItem.of(modify, event -> {
						event.getWhoClicked().closeInventory();
					}));
					
					EpicComponent comp = new EpicComponent(item, MythicBukkit.inst());
					for(int i = 0; i < controllerSlots.length; ++i) {
						String comboKey = controllerSlots[i];
						ItemStack cmdItem = emptyCommand.clone();

						if(comp.hasKey(controllerSlots[i])) {
							String id = comp.getString(controllerSlots[i]);
							Optional<ASummonCommand> oCmd = SummonControllerManager.getInst().getCommand(id);
							if(oCmd.isPresent()) {
								cmdItem = oCmd.get().getCommandItem();
							}
						}
						
						int slot = 10 + i*2;
						int loreIndex = 5 + i;
						contents.set(slot, IntelligentItem.of(cmdItem, event -> {
							ItemStack itemCopy = itemContainer.getValue();
							itemContainer.setValue(null);
							
							openCommandListMenu(player, itemCopy, loreIndex, comboKey);
						}));
					}
				}
	
				@Override
				public void close(Player player, EpicInventory inventory) {
					ItemStack it = itemContainer.getValue();
					if(it == null || it.getType().equals(Material.AIR))
						return;
					
					Utils.dropItemStack(player, it);
				}
			})
			.build(Main.getInstance())
			.open(player);
	}
	
	private void openCommandListMenu(Player player, ItemStack item, int loreIndex, String comboSlot) {
		MutableObject<ItemStack> itemContainer = new MutableObject<>(item);
		int rows = (int) Utils.limitValue(1, 6,(SummonControllerManager.getInst().getCommands().size() + 8) / 9);
		
		EpicInventory.builder()
			.title(controllerStringName)
			.rows(rows)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					int slots = rows * 9;
					int slot = 0;
					List<ASummonCommand> commands = SummonControllerManager.getInst()
							.getCommands()
							.values()
							.stream()
							.collect(Collectors.toList());
					while(slot < slots && slot < commands.size()) {
						var command = commands.get(slot);
						ItemStack it = command.getCommandItem();
						
						contents.set(slot, IntelligentItem.of(it, event -> {
							EpicComponent comp = new EpicComponent(item, MythicBukkit.inst());
							comp.setString(comboSlot, command.getCommandId());
							comp.applyTo(item);
							
							ItemMeta im = item.getItemMeta();
							List<String> lore = im.getLore();
							lore.set(loreIndex, ChatColor.translateAlternateColorCodes('&', "&b♦ &7"+comboSlot.toUpperCase()+": &f"+command.getDisplay()));
							im.setLore(lore);
							item.setItemMeta(im);
							
							openCommandMenu(player, item);
						}));
						
						++slot;
					}
				}
	
				@Override
				public void close(Player player, EpicInventory inventory) {
					ItemStack it = itemContainer.getValue();
					if(it == null || it.getType().equals(Material.AIR))
						return;
					
					Utils.dropItemStack(player, it);
				}
			})
			.build(Main.getInstance())
			.open(player);
	}
	
}
