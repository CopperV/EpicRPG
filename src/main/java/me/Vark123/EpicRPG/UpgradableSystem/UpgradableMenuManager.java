package me.Vark123.EpicRPG.UpgradableSystem;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public final class UpgradableMenuManager {

	private static final UpgradableMenuManager inst = new UpgradableMenuManager();
	
	private final InventoryProvider upgradableProvider;
	private final int[] upgradableFreeSlots;
	private final Collection<Integer> nonDisabledSlots;
	
	private final ItemStack disabled;
	private final ItemStack empty;
	private final ItemStack gray;
	private final ItemStack green;
	private final ItemStack red;
	private final ItemStack upgrade;
	private final ItemStack chance;
	private final ItemStack inhibitor;
	
	private UpgradableMenuManager() {
		upgradableFreeSlots = new int[] {10, 32,33,34, 37};
		nonDisabledSlots = Arrays.asList(10,11, 14,15,16, 28, 32,33,34,35, 37,38, 49,50);
		
		disabled = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = disabled.getItemMeta();
			im.setDisplayName(" ");
			disabled.setItemMeta(im);
		}
		empty = new ItemStack(Material.BARRIER, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}

		gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);{
			ItemMeta im = gray.getItemMeta();
			im.setDisplayName(" ");
			gray.setItemMeta(im);
		}
		green = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);{
			ItemMeta im = green.getItemMeta();
			im.setDisplayName(" ");
			green.setItemMeta(im);
		}
		red = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);{
			ItemMeta im = red.getItemMeta();
			im.setDisplayName(" ");
			red.setItemMeta(im);
		}

		upgrade = new ItemStack(Material.DAMAGED_ANVIL, 1);{
			ItemMeta im = upgrade.getItemMeta();
			im.setDisplayName("§7§lULEPSZ");
			upgrade.setItemMeta(im);
		}
		chance = new ItemStack(Material.GOLD_NUGGET, 1);{
			ItemMeta im = chance.getItemMeta();
			im.setDisplayName("§e§lSzansa  §7»  §f§l?");
			chance.setItemMeta(im);
		}
		
		inhibitor = MythicBukkit.inst().getItemManager().getItemStack("Inhibitor_Platynowy");{
			ItemMeta im = inhibitor.getItemMeta();
			im.setDisplayName("§d§lInhibitor");
			inhibitor.setItemMeta(im);
		}
		
		upgradableProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9*6; ++i) {
					if(nonDisabledSlots.contains(i))
						continue;
					contents.set(i, disabled);
				}
				
				contents.set(11, gray);
				contents.set(35, gray);
				contents.set(38, gray);

				contents.set(14, empty);
				contents.set(15, empty);
				contents.set(16, empty);

				contents.set(28, inhibitor);
				
				contents.set(49, IntelligentItem.of(upgrade, e -> {
					Player p = (Player) e.getWhoClicked();
					Inventory inv = e.getView().getTopInventory();
					
					ItemStack upgradableItem = inv.getItem(10);
					if(upgradableItem == null 
							|| upgradableItem.getType().equals(Material.AIR)
							|| !UpgradableManager.get().isItemUpgradable(upgradableItem)) {
						p.closeInventory();
						return;
					}
					
					NBTItem upgradableItemNbt = new NBTItem(upgradableItem);
					ReadWriteNBT upgradableNbt = upgradableItemNbt.getOrCreateCompound("epic-upgrades");
					int level = 0;
					if(upgradableNbt.hasTag("level"))
						level = upgradableNbt.getInteger("level");
					
					UpgradableManager.get().getUpgradableLevel(level+1).ifPresentOrElse(upgradableLevel -> {
						Map<ItemStack, Integer> items = new LinkedHashMap<>();
						if(inv.getItem(32) != null && !inv.getItem(32).getType().equals(Material.AIR))
							items.put(inv.getItem(32), 32);
						if(inv.getItem(33) != null && !inv.getItem(33).getType().equals(Material.AIR))
							items.put(inv.getItem(33), 33);
						if(inv.getItem(34) != null && !inv.getItem(34).getType().equals(Material.AIR))
							items.put(inv.getItem(34), 34);
						if(!upgradableLevel.matchRecipe(items.keySet())) {
							p.closeInventory();
							return;
						}
						
						double chance = upgradableLevel.getChance();
						
						boolean inhibitor = false;
						ItemStack inhibitorItem = inv.getItem(37);
						if(inhibitorItem != null && !inhibitorItem.getType().equals(Material.AIR)
								&& UpgradableManager.get().canUseInhibitor(chance, inhibitorItem)) {
							chance += UpgradableManager.get().getInhibitor(inhibitorItem)
									.map(_inhibitor -> _inhibitor.getChance())
									.orElse(0.);
							inhibitor = true;
						}
						

						Collection<String> check = new LinkedList<>();
						Map<String,Integer> recipe = upgradableLevel.getMmIdCosts();
						items.forEach((item, slot) -> {
							NBTItem nbt = new NBTItem(item);
							if(!nbt.hasTag("MYTHIC_TYPE"))
								return;
							String mmId = nbt.getString("MYTHIC_TYPE");
							if(!recipe.containsKey(mmId))
								return;
							if(item.getAmount() < recipe.get(mmId))
								return;
							if(check.contains(mmId))
								return;
							
							if(item.getAmount() > recipe.get(mmId))
								item.setAmount(item.getAmount() - recipe.get(mmId));
							else
								inv.setItem(slot, null);
							check.add(mmId);
						});
						
						if(inhibitor) {
							if(inhibitorItem.getAmount() < 2) {
								inv.setItem(37, null);
							} else {
								inhibitorItem.setAmount(inhibitorItem.getAmount() - 1);
							}
						}
						
						UpgradableManager.get().upgradeItem(p, upgradableItem, chance, upgradableLevel.getLevel());
						
						for(int slot : upgradableFreeSlots) {
							Utils.dropItemStack(p, inv.getItem(slot));
							inv.setItem(slot, null);
						}
						p.closeInventory();
					}, () -> {
						p.closeInventory();
					});
				}));
				contents.set(50, chance.clone());
				
			}
			@Override
			public void update(Player player, InventoryContents contents) {
				Inventory inv = contents.pagination().inventory().getInventory();
				
				ItemStack upgradableItem = inv.getItem(10);
				if(upgradableItem == null || upgradableItem.getType().equals(Material.AIR)) {
					inv.setItem(11, gray);
					inv.setItem(35, gray);
					inv.setItem(38, gray);

					inv.setItem(14, empty);
					inv.setItem(15, empty);
					inv.setItem(16, empty);

					inv.setItem(50, chance.clone());
					return;
				}
				if(!UpgradableManager.get().isItemUpgradable(upgradableItem)) {
					inv.setItem(11, red);
					inv.setItem(35, gray);
					inv.setItem(38, gray);

					inv.setItem(14, empty);
					inv.setItem(15, empty);
					inv.setItem(16, empty);

					inv.setItem(50, chance.clone());
					return;
				}
				
				NBTItem upgradableItemNbt = new NBTItem(upgradableItem);
				int level = 0;
				if(upgradableItemNbt.hasTag("epic-upgrades")
						&& upgradableItemNbt.getCompound("epic-upgrades").hasTag("level"))
					level = upgradableItemNbt.getCompound("epic-upgrades").getInteger("level");
				
				UpgradableManager.get().getUpgradableLevel(level+1).ifPresentOrElse(upgradableLevel -> {
					inv.setItem(11, green);
					
					double chance = upgradableLevel.getChance();
					
					ItemStack inhibitorItem = inv.getItem(37);
					if(inhibitorItem == null || inhibitorItem.getType().equals(Material.AIR)) {
						inv.setItem(38, red);
					} else {
						if(UpgradableManager.get().canUseInhibitor(chance, inhibitorItem)) {
							inv.setItem(38, green);
							chance += UpgradableManager.get().getInhibitor(inhibitorItem)
									.map(_inhibitor -> _inhibitor.getChance())
									.orElse(0.);
						}
					}
					
					List<ItemStack> recipe = upgradableLevel.getMmIdCosts().keySet().stream()
						.map(upgradableLevel::getItem)
						.filter(item -> item != null
								&& !item.getType().equals(Material.AIR))
						.collect(Collectors.toList());
					{
						int j = 14;
						for(int i = 0; i < recipe.size() && j < 17; ++i, ++j) {
							inv.setItem(j, recipe.get(i));
						}
						for(;j < 17; ++j) {
							inv.setItem(j, empty);
						}
					}
					
					List<ItemStack> items = new LinkedList<>();
					if(inv.getItem(32) != null && !inv.getItem(32).getType().equals(Material.AIR))
						items.add(inv.getItem(32));
					if(inv.getItem(33) != null && !inv.getItem(33).getType().equals(Material.AIR))
						items.add(inv.getItem(33));
					if(inv.getItem(34) != null && !inv.getItem(34).getType().equals(Material.AIR))
						items.add(inv.getItem(34));
					if(upgradableLevel.matchRecipe(items)) {
						inv.setItem(35, green);
					} else {
						inv.setItem(35, red);
					}
					
					ItemStack chanceItem = inv.getItem(50);{
						ItemMeta im = chanceItem.getItemMeta();
						im.setDisplayName("§e§lSzansa  §7»  §f§l"+((int) (chance*100))+"%");
						chanceItem.setItemMeta(im);
					}
					inv.setItem(50, chanceItem);
					
				}, () -> {
					inv.setItem(11, red);
					inv.setItem(35, gray);
					inv.setItem(38, gray);

					inv.setItem(14, empty);
					inv.setItem(15, empty);
					inv.setItem(16, empty);

					inv.setItem(50, chance.clone());
				});
				
			}
			@Override
			public void close(Player player, RyseInventory inventory) {
				Inventory inv = inventory.getInventory();
				for(int i = 0; i < upgradableFreeSlots.length; ++i) {
					int slot = upgradableFreeSlots[i];
					ItemStack it = inv.getItem(slot);
					if(it == null 
							|| it.getType().equals(Material.AIR))
						continue;
					Utils.dropItemStack(player, it);
				}
			}
			
		};
	}
	
	public static final UpgradableMenuManager get() {
		return inst;
	}
	
	public void openUpgradableMenu(Player p) {
		RyseInventory.builder()
			.title("§7§lWielki Piec")
			.rows(6)
			.ignoredSlots(upgradableFreeSlots)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.provider(upgradableProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
}
