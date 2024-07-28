package me.Vark123.EpicRPG.UpgradableSystem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.UpgradableSystem.UpgradableInhibitor.InhibitorCrafting;

@Getter
public final class InhibitorMenuManager {

	private static final int MAX_RECIPES_TO_VIEW_PER_PAGE = 45;

	private static final InhibitorMenuManager inst = new InhibitorMenuManager();

	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	private final ItemStack back;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack next;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack previous;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack pageItem;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack moneyItem;

	private final ItemStack redCreate;
	private final ItemStack greenCreate;
	
	private final List<Integer> recipeSlots = Arrays.asList(9,10,11,12,13,14,15,16,17);
	private final List<Integer> craftingSlots = Arrays.asList(27,28,29,30,31,32,33,34,35);
	
	private InhibitorMenuManager() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		back = new ItemStack(Material.BARRIER);{
			ItemMeta im = back.getItemMeta();
			im.setDisplayName("§c§lPowrot");
			back.setItemMeta(im);
		}
		next = new ItemStack(Material.ARROW);{
			ItemMeta im = next.getItemMeta();
			im.setDisplayName("§fNastepna");
			next.setItemMeta(im);
		}
		previous = new ItemStack(Material.ARROW);{
			ItemMeta im = previous.getItemMeta();
			im.setDisplayName("§fPoprzednia");
			previous.setItemMeta(im);
		}
		pageItem = new ItemStack(Material.PAPER);{
			ItemMeta im = pageItem.getItemMeta();
			im.setDisplayName("§f§lStrona 1");
			pageItem.setItemMeta(im);
		}
		moneyItem = new ItemStack(Material.GOLD_INGOT);
		redCreate = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);{
			ItemMeta im = redCreate.getItemMeta();
			im.setDisplayName("§6§lStworz");
			redCreate.setItemMeta(im);
		}
		greenCreate = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);{
			ItemMeta im = greenCreate.getItemMeta();
			im.setDisplayName("§6§lStworz");
			greenCreate.setItemMeta(im);
		}
	}
	
	public static final InhibitorMenuManager get() {
		return inst;
	}
	
	public void openRecipesCraftMenu(Player p, int page) {
		List<UpgradableInhibitor> inhibitors = UpgradableManager.get()
				.getInhibitors().values()
				.stream()
//				.sorted((inhibitor1, inhibitor2) -> inhibitor1.getMmId().compareTo(inhibitor2.getMmId()))
				.collect(Collectors.toList());
		int offset = MAX_RECIPES_TO_VIEW_PER_PAGE*page;
		int end = (MAX_RECIPES_TO_VIEW_PER_PAGE*(page+1)) > inhibitors.size() ? inhibitors.size() : (MAX_RECIPES_TO_VIEW_PER_PAGE*(page+1));
		int pages = (inhibitors.size() - 1) / MAX_RECIPES_TO_VIEW_PER_PAGE + 1;
		
		RyseInventory.builder()
			.title("§6§lDestylacja Inhibitorow")
			.size(MAX_RECIPES_TO_VIEW_PER_PAGE+9)
			.disableUpdateTask()
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 9; ++i)
						contents.set(MAX_RECIPES_TO_VIEW_PER_PAGE+i, empty);

					{
						ItemStack localPage = pageItem.clone();
						ItemMeta im = localPage.getItemMeta();
						im.setDisplayName("§f§lStrona "+(page+1));
						localPage.setItemMeta(im);
						contents.set(MAX_RECIPES_TO_VIEW_PER_PAGE+4, localPage);
					}
					
					if(page > 0)
						contents.set(MAX_RECIPES_TO_VIEW_PER_PAGE+2, IntelligentItem.of(previous, e -> {
							openRecipesCraftMenu(player, page-1);
						}));
					if(page < (pages-1))
						contents.set(MAX_RECIPES_TO_VIEW_PER_PAGE+6, IntelligentItem.of(next, e -> {
							openRecipesCraftMenu(player, page+1);
						}));

					
					for(int i = 0; i < (end - offset); ++i) {
						UpgradableInhibitor inhibitor = inhibitors.get(i+offset);
						ItemStack it = inhibitor.getItem();
						contents.set(i, IntelligentItem.of(it, e -> {
							openRecipeCraftMenu(p, inhibitor, page);
						}));
					}
				}
			})
			.build(Main.getInstance())
			.open(p);
	}

	public void openRecipeCraftMenu(Player p, UpgradableInhibitor inhibitor, int page) {
		int size = 54;
		
		InhibitorInventoryHolder holder = new InhibitorInventoryHolder(inhibitor, page);
		Inventory inv = Bukkit.createInventory(holder, size, "§6§lDestylacja Inhibitorow");
		
		for(int i = 0; i < size; ++i) {
			if(recipeSlots.contains(i) || craftingSlots.contains(i))
				continue;
			inv.setItem(i, empty);
		}
		
		inv.setItem(4, inhibitor.getItem());
		inv.setItem(8, back);
		inv.setItem(49, redCreate);
		
		ItemStack moneyInfo = moneyItem.clone();{
			ItemMeta im = moneyInfo.getItemMeta();
			im.setDisplayName("§e§o"+String.format("%.2f", inhibitor.getCrafting().getMoneyCost())+" $");
			moneyInfo.setItemMeta(im);
		}
		inv.setItem(50, moneyInfo);
		
		InhibitorCrafting crafting = inhibitor.getCrafting();
		crafting.getMmIdCosts().keySet().forEach(crafting::getItem);
		
		MutableInt index = new MutableInt(0);
		crafting.getMmItemCosts().values().forEach(it -> {
			if(index.getValue() >= recipeSlots.size())
				return;
			inv.setItem(recipeSlots.get(index.getAndIncrement()), it);
		});
		
		p.openInventory(inv);
	}
	
}
