package me.Vark123.EpicRPG.Players;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Jewelry.JewelryMenuManager;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Utils.Utils;

public final class AdditionalMenuManager {

	private static final AdditionalMenuManager instance = new AdditionalMenuManager();

	private final ItemStack empty;
	
	private final ItemStack backItem;
	private final ItemStack jewelryItem;
	
	private final InventoryProvider baseProvider;
	
	private AdditionalMenuManager() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		
		jewelryItem = new ItemStack(Material.RAW_GOLD);{
			ItemMeta im = jewelryItem.getItemMeta();
			im.setDisplayName("§6§lBIZUTERIA");
			jewelryItem.setItemMeta(im);
		}
		backItem = new ItemStack(Material.STONE_SWORD);{
			ItemMeta im = backItem.getItemMeta();
			im.setDisplayName("§7§lBRON NA PLECY");
			backItem.setItemMeta(im);
		}
		
		baseProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i)
					contents.set(i, empty);
				
				contents.set(8, IntelligentItem.of(backItem, e -> {
					openBackItemMenu(player);
				}));
				contents.set(4, IntelligentItem.of(jewelryItem, e -> {
					JewelryMenuManager.getInstance().openMenu(player);
				}));
			}
		};
	}
	
	public static final AdditionalMenuManager get() {
		return instance;
	}
	
	public void openBaseMenu(Player p) {
		RyseInventory.builder()
			.title("§7Ekwipunek §7"+p.getName())
			.rows(1)
			.disableUpdateTask()
			.provider(baseProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public void openBackItemMenu(Player p) {
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RyseInventory.builder()
			.title("§7§lBRON NA PLECY")
			.rows(1)
			.disableUpdateTask()
			.ignoredSlots(4)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 9; ++i) {
						if(i == 4)
							continue;
						contents.set(i, empty);
					}
					
					ItemStack it = rpg.getBackItem();
					if(it == null)
						return;
					contents.set(4, it);
				}
				@Override
				public void close(Player player, RyseInventory inventory) {
					ItemStack it = inventory.getInventory().getItem(4);
					if(it == null) {
						rpg.setBackItem(it);
						ChangeStats.change(rpg);
						return;
					}
					
					NBTItem nbt = new NBTItem(it);
					if(!nbt.hasTag("MYTHIC_TYPE") || nbt.hasTag("Klejnot")
							|| (nbt.hasTag("RPGType") && nbt.getString("RPGType").equals("gem"))
							|| nbt.hasTag("JewerlyType")) {
						p.sendMessage(it.getItemMeta().getDisplayName()+"§cnie moze zostac zalozony na plecy!");
						rpg.setBackItem(null);
						ChangeStats.change(rpg);
						Utils.dropItemStack(p, it);
						return;
					}
					
					rpg.setBackItem(it);
					ChangeStats.change(rpg);
				}
			})
			.build(Main.getInstance())
			.open(p);
	}
	
}
