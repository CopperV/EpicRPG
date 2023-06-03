package me.Vark123.EpicRPG.Stats;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;

@Getter
public class ResetStatsMenuManager {
	
	private static final ResetStatsMenuManager instance = new ResetStatsMenuManager();
	
	private final InventoryProvider provider;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack yes;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack no;
	
	private ResetStatsMenuManager() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		yes = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);{
			ItemMeta im = yes.getItemMeta();
			im.setDisplayName("§2§l§oPotwierdz");
			yes.setItemMeta(im);
		}
		no = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);{
			ItemMeta im = no.getItemMeta();
			im.setDisplayName("§c§l§oOdrzuc");
			no.setItemMeta(im);
		}
		
		provider = new InventoryProvider() {

			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i) {
					if(i == 2) {
						contents.set(i, yes);
						continue;
					}
					if(i == 6) {
						contents.set(i, no);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
	}
	
	public static final ResetStatsMenuManager getInstance() {
		return instance;
	}
	
	public void open(Player p) {
		RyseInventory.builder()
			.title("§7§l§oResetowanie statystyk")
			.size(9)
			.listener(ResetStatsMenuEvents.getEvents().getClickEvent())
			.disableUpdateTask()
			.provider(provider)
			.build(Main.getInstance())
			.open(p);
	}

}
