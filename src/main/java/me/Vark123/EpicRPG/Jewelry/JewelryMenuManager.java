package me.Vark123.EpicRPG.Jewelry;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgJewelry;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Utils.Utils;

public class JewelryMenuManager {
	
	private static final JewelryMenuManager instance = new JewelryMenuManager();
	
	private final ItemStack baseAmulet;
	private final ItemStack baseRing;
	private final ItemStack baseGloves;
	private final ItemStack empty;
	
	@Getter
	private final int[] freeSlots;
	
	private JewelryMenuManager() {
		freeSlots = new int[] {10, 12, 14, 16};
		
		baseAmulet = new ItemStack(Material.FLINT, 1);
		baseRing = new ItemStack(Material.DIAMOND, 1);
		baseGloves = new ItemStack(Material.BUCKET, 1);
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		
		{
			ItemMeta im = baseAmulet.getItemMeta();
			im.setDisplayName("§e§lAMULET");
			baseAmulet.setItemMeta(im);
		}
		{
			ItemMeta im = baseRing.getItemMeta();
			im.setDisplayName("§6§lPIERSCIEN");
			baseRing.setItemMeta(im);
		}
		{
			ItemMeta im = baseGloves.getItemMeta();
			im.setDisplayName("§c§lREKAWICE");
			baseGloves.setItemMeta(im);
		}
		{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
	}
	
	public static final JewelryMenuManager getInstance() {
		return instance;
	}
	
	public void openMenu(Player p) {
		openMenu(p, p);
		if(!PlayerManager.getInstance().playerExists(p))
			return;
	}
	
	public void openMenu(Player viewer, Player owner) {
		if(!PlayerManager.getInstance().playerExists(owner))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(owner);
		RyseInventory.builder()
			.title("§6§lBizuteria")
			.size(18)
			.ignoredSlots(freeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.provider(getProvider(viewer, rpg))
			.build(Main.getInstance())
			.open(viewer);
	}
	
	private InventoryProvider getProvider(Player viewer, RpgPlayer owner) {
		InventoryProvider provider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(freeSlots);
				for(int i = 0; i < 18; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 1) {
						contents.set(i, baseAmulet);
						continue;
					}
					if(i == 3) {
						contents.set(i, baseRing);
						continue;
					}
					if(i == 5) {
						contents.set(i, baseRing);
						continue;
					}
					if(i == 7) {
						contents.set(i, baseGloves);
						continue;
					}
					contents.set(i, empty);
				}
				
				RpgJewelry jewelry = owner.getJewelry();
				jewelry.getAkcesoria().forEach((i, item) -> {
					if(item.getItem() == null)
						return;
					contents.set(10+i*2, item.getItem());
				});
			}

			@Override
			public void close(Player player, RyseInventory inventory) {
				
				Player p = owner.getPlayer();
				RpgJewelry jewelry = owner.getJewelry();
				jewelry.getAkcesoria().forEach((i, item) -> {
					ItemStack it = inventory.getInventory().getItem(10 + 2*i);
					if(it == null
							|| it.getType().equals(Material.AIR)) {
						item.setItem(it);
						return;
					}
					
					if(!isJewelryItem(it)) {
						p.getPlayer().sendMessage(it.getItemMeta().getDisplayName()+"§cnie jest amuletem, pierscieniem ani rekawicami!");
						Utils.dropItemStack(p, it);
						item.setItem(null);
						return;
					}
					
					if(!isCorrectJewelrySlotType(it, item)) {
						p.sendMessage(it.getItemMeta().getDisplayName()+"§czalozyles na niepoprawny slot!");
						Utils.dropItemStack(p, it);
						item.setItem(null);
						return;
					}
					
					item.setItem(it);
				});
				
				ChangeStats.change(owner);
			}
		};
		
		return provider;
	}
	
	private boolean isJewelryItem(ItemStack it) {
		NBTItem nbt = new NBTItem(it);
		return nbt.hasTag("JewerlyType");
	}
	
	private boolean isCorrectJewelrySlotType(ItemStack it, JewelryItem jewelry) {
		NBTItem nbt = new NBTItem(it);
		String type = nbt.getString("JewerlyType").toUpperCase();
		return jewelry.getType().equals(JewelryType.valueOf(type));
	}
	
}
