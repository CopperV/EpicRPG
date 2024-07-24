package me.Vark123.EpicRPG.KosturSystem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class KosturMenuManager {

	private static final KosturMenuManager instance = new KosturMenuManager();
	
	private final InventoryProvider kosturProvider;
	private final InventoryProvider createProvider;
	private final int[] kosturFreeSlots;
	private final int[] runesFreeSlots;
	private final int[] createFreeSlots;
	private final String[] runeSlots;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack create;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack modify;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack kostur;

	@Getter(value = AccessLevel.NONE)
	private final ItemStack pppIt;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack pplIt;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack plpIt;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack pllIt;

	@Getter(value = AccessLevel.NONE)
	private final ItemStack kosturPart1;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack kosturPart2;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack kosturPart3;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack kamienie;

	@Getter(value = AccessLevel.NONE)
	private final ItemExecutor manag;
	
	private KosturMenuManager() {
		manag = MythicBukkit.inst().getItemManager();
				
		kosturFreeSlots = new int[] {13};
		runesFreeSlots = new int[] {10, 12, 14, 16};
		createFreeSlots = new int[] {10, 19, 11, 20, 12, 21, 
				14, 15, 16, 23, 24, 25};
		runeSlots = new String[] {"PPP","PPL","PLP","PLL"};
		
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
		modify = new ItemStack(Material.EMERALD, 1);{
			ItemMeta im = modify.getItemMeta();
			im.setDisplayName("§6§lModyfikuj");
			modify.setItemMeta(im);
		}
		kostur = new ItemStack(Material.BLAZE_ROD, 1);{
			ItemMeta im = kostur.getItemMeta();
			im.setDisplayName("§3§lRuniczny kostur");
			kostur.setItemMeta(im);
		}
		
		pppIt = new ItemStack(Material.MUSIC_DISC_MELLOHI);{
			ItemMeta im = pppIt.getItemMeta();
			im.setDisplayName("§2§lKombinacja PPP");
			im.setLore(Arrays.asList("§aUmozliwia rzucanie efektu runy","§auzywajac kombinacji myszy", "§aprawy-prawy-prawy"));
			pppIt.setItemMeta(im);
		}
		pplIt = new ItemStack(Material.MUSIC_DISC_CHIRP);{
			ItemMeta im = pplIt.getItemMeta();
			im.setDisplayName("§2§lKombinacja PPL");
			im.setLore(Arrays.asList("§aUmozliwia rzucanie efektu runy","§auzywajac kombinacji myszy", "§aprawy-prawy-lewy"));
			pplIt.setItemMeta(im);
		}
		plpIt = new ItemStack(Material.MUSIC_DISC_WARD);{
			ItemMeta im = plpIt.getItemMeta();
			im.setDisplayName("§2§lKombinacja PLP");
			im.setLore(Arrays.asList("§aUmozliwia rzucanie efektu runy","§auzywajac kombinacji myszy", "§aprawy-lewy-prawy"));
			plpIt.setItemMeta(im);
		}
		pllIt = new ItemStack(Material.MUSIC_DISC_11);{
			ItemMeta im = pllIt.getItemMeta();
			im.setDisplayName("§2§lKombinacja PLL");
			im.setLore(Arrays.asList("§aUmozliwia rzucanie efektu runy","§auzywajac kombinacji myszy", "§aprawy-lewy-lewy"));
			pllIt.setItemMeta(im);
		}

		kamienie = MythicBukkit.inst().getItemManager().getItemStack("Opal");{
			ItemMeta im = kamienie.getItemMeta();
			im.setDisplayName("§b§lKamienie Szlachetne");
			im.setLore(new LinkedList<>());
			kamienie.setItemMeta(im);
		}
		
		kosturPart1 = MythicBukkit.inst().getItemManager().getItemStack("Runiczny_Kostur_1");
		kosturPart2 = MythicBukkit.inst().getItemManager().getItemStack("Runiczny_Kostur_2");
		kosturPart3 = MythicBukkit.inst().getItemManager().getItemStack("Runiczny_Kostur_3");
		
		kosturProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(kosturFreeSlots);
				for(int i = 0; i < 27; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 22) {
						contents.set(i, modify);
						continue;
					}
					if(i == 4) {
						contents.set(i, kostur);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
		
		createProvider = new InventoryProvider() {

			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(createFreeSlots);
				for(int i = 0; i < 36; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 31) {
						contents.set(i, create);
						continue;
					}
					if(i == 1) {
						contents.set(i, kosturPart1);
						continue;
					}
					if(i == 2) {
						contents.set(i, kosturPart2);
						continue;
					}
					if(i == 3) {
						contents.set(i, kosturPart3);
						continue;
					}
					if(i == 6) {
						contents.set(i, kamienie);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
	}
	
	public static final KosturMenuManager getInstance() {
		return instance;
	}
	
	public void openMenu(Player p) {
		RyseInventory.builder()
			.title("§3§lRuniczny kostur")
			.size(27)
			.ignoredSlots(kosturFreeSlots)
//			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.listener(KosturMenuEvents.getEvents().getKosturClickEvent())
			.listener(KosturMenuEvents.getEvents().getKosturCloseEvent())
			.provider(kosturProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public void openMenu(Player p, ItemStack kostur) {
		RyseInventory.builder()
			.title("§3§lRuniczny kostur")
			.size(27)
			.ignoredSlots(runesFreeSlots)
//			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.listener(KosturMenuEvents.getEvents().getKosturModifyClickEvent())
			.listener(KosturMenuEvents.getEvents().getKosturModifyCloseEvent())
			.provider(getRuneProvider(kostur))
			.build(Main.getInstance())
			.open(p);
	}
	
	public void openCreateMenu(Player p) {
		RyseInventory.builder()
			.title("§3§lTworzenie kostura")
			.size(36)
			.ignoredSlots(createFreeSlots)
//			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.listener(KosturMenuEvents.getEvents().getKosturCreateClickEvent())
			.listener(KosturMenuEvents.getEvents().getKosturCreateCloseEvent())
			.provider(createProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	private InventoryProvider getRuneProvider(ItemStack kostur) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(runesFreeSlots);
				for(int i = 0; i < 27; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 22) {
						contents.set(i, create);
						continue;
					}
					if(i == 1) {
						contents.set(i, pppIt);
						continue;
					}
					if(i == 3) {
						contents.set(i, pplIt);
						continue;
					}
					if(i == 5) {
						contents.set(i, plpIt);
						continue;
					}
					if(i == 7) {
						contents.set(i, pllIt);
						continue;
					}
					contents.set(i, empty);
				}
				
				NBTItem nbt = new NBTItem(kostur);
				if(!nbt.getString("PPP").equals("-")) {
					ItemStack it = manag.getItemStack(nbt.getString("PPP"));
					contents.set(10, it);
				}
				if(!nbt.getString("PPL").equals("-")) {
					ItemStack it = manag.getItemStack(nbt.getString("PPL"));
					contents.set(12, it);
				}
				if(!nbt.getString("PLP").equals("-")) {
					ItemStack it = manag.getItemStack(nbt.getString("PLP"));
					contents.set(14, it);
				}
				if(!nbt.getString("PLL").equals("-")) {
					ItemStack it = manag.getItemStack(nbt.getString("PLL"));
					contents.set(16, it);
				}
			}
		};
	}
	
}
