package me.Vark123.EpicRPG.SzponBeliara;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class SzponBeliaraManager {

	private static final SzponBeliaraManager instance = new SzponBeliaraManager();
	
	private final InventoryProvider awakeningProvider;
	private final int[] awakeningFreeSlots;

	private final InventoryProvider upgradeProvider;
	private final int[] upgradeFreeSlots;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack create;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack asleepSzpon;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack pakt;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack kamienSzlachetny;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack upgradeSzpon;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack esencjaSzpona;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack esencjaDemona;
	
	private SzponBeliaraManager() {
		awakeningFreeSlots = new int[] {11,13,15};
		upgradeFreeSlots = new int[] {10,12,14,16};
		
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		create = new ItemStack(Material.EMERALD, 1);{
			ItemMeta im = create.getItemMeta();
			im.setDisplayName("§6§lUlepsz");
			create.setItemMeta(im);
		}
		asleepSzpon = MythicBukkit.inst().getItemManager().getItemStack("Szpon_Beliara_Weak_Str");
		upgradeSzpon = asleepSzpon.clone();{
			ItemMeta im = upgradeSzpon.getItemMeta();
			im.setLore(new LinkedList<>());
			upgradeSzpon.setItemMeta(im);
		}
		esencjaSzpona = MythicBukkit.inst().getItemManager().getItemStack("Esencja_Szpona");{
			ItemMeta im = esencjaSzpona.getItemMeta();
			im.setLore(new LinkedList<>());
			esencjaSzpona.setItemMeta(im);
		}
		esencjaDemona = new ItemStack(Material.GHAST_TEAR);{
			ItemMeta im = esencjaDemona.getItemMeta();
			im.setDisplayName("§4§lEsencja Arcydemona");
			esencjaDemona.setItemMeta(im);
		}
		pakt = MythicBukkit.inst().getItemManager().getItemStack("Pakt_Z_Beliarem");{
			ItemMeta meta = pakt.getItemMeta();
			meta.setLore(new ArrayList<>());
			pakt.setItemMeta(meta);
		}
		kamienSzlachetny = MythicBukkit.inst().getItemManager().getItemStack("Opal");{
			ItemMeta meta = kamienSzlachetny.getItemMeta();
			meta.setDisplayName("§b§lKamien Szlachetny");
			meta.setLore(new ArrayList<>());
			kamienSzlachetny.setItemMeta(meta);
		}
		
		awakeningProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(awakeningFreeSlots);
				for(int i = 0; i < 27; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 2) {
						contents.set(i, asleepSzpon);
						continue;
					}
					if(i == 4) {
						contents.set(i, pakt);
						continue;
					}
					if(i == 6) {
						contents.set(i, kamienSzlachetny);
						continue;
					}
					if(i == 22) {
						contents.set(i, create);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
		
		upgradeProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(upgradeFreeSlots);
				for(int i = 0; i < 27; ++i) {
					if(tmpList.contains(i))
						continue;
					contents.set(i, empty);
				}
				
				contents.set(1, upgradeSzpon);
				contents.set(3, kamienSzlachetny);
				contents.set(5, esencjaSzpona);
				contents.set(7, esencjaDemona);

				contents.set(22, IntelligentItem.of(create, e -> {
					Player p = (Player) e.getWhoClicked();
					Inventory inv = e.getInventory();
					ItemStack _szpon = inv.getItem(10);
					ItemStack _klejnot = inv.getItem(12);
					ItemStack _esencjaSzpona = inv.getItem(14);
					ItemStack _esencjaDemona = inv.getItem(16);
					
					if(_szpon == null || _szpon.getType().equals(Material.AIR)
							|| _klejnot == null || _klejnot.getType().equals(Material.AIR)
							|| _esencjaSzpona == null || _esencjaSzpona.getType().equals(Material.AIR)
							|| _esencjaDemona == null || _esencjaDemona.getType().equals(Material.AIR)) {
						p.closeInventory();
						return;
					}

					NBTItem _szponNBT = new NBTItem(_szpon);
					NBTItem _klejnotNBT = new NBTItem(_klejnot);
					NBTItem _esencjaSzponaNBT = new NBTItem(_esencjaSzpona);
					NBTItem _esencjaDemonaNBT = new NBTItem(_esencjaDemona);
					
					if(!_esencjaSzponaNBT.hasTag("MYTHIC_TYPE")
							|| !_esencjaSzponaNBT.getString("MYTHIC_TYPE").equals("Esencja_Szpona")
							|| !_klejnotNBT.hasTag("MYTHIC_TYPE")
							|| !_esencjaDemonaNBT.hasTag("MYTHIC_TYPE")) {
						p.closeInventory();
						return;
					}
					if(!_szponNBT.hasTag("SzponBeliara")) {
						p.closeInventory();
						return;
					}

					String _szponId = _szponNBT.getString("MYTHIC_TYPE");
					String _klejnotId = null;
					String _esencjaDemonaId = null;
					switch(_szponId) {
						case "Szpon_Beliara_Str":
						case "Szpon_Beliara_Str_I":
						case "Szpon_Beliara_Str_II":
							_klejnotId = "Topaz";
							break;
						case "Szpon_Beliara_Wytrz":
						case "Szpon_Beliara_Wytrz_I":
						case "Szpon_Beliara_Wytrz_II":
							_klejnotId = "Bursztyn";
							break;
						case "Szpon_Beliara_Zr":
						case "Szpon_Beliara_Zr_I":
						case "Szpon_Beliara_Zr_II":
							_klejnotId = "Jadeit";
							break;
						case "Szpon_Beliara_Zd":
						case "Szpon_Beliara_Zd_I":
						case "Szpon_Beliara_Zd_II":
							_klejnotId = "Opal";
							break;
						case "SzponBeliaraMana":
						case "SzponBeliaraMana_I":
						case "SzponBeliaraMana_II":
							_klejnotId = "Szafir";
							break;
						case "SzponBeliaraInt":
						case "SzponBeliaraInt_I":
						case "SzponBeliaraInt_II":
							_klejnotId = "Ametyst";
							break;
					}
					
					if(_szponId.endsWith("_I"))
						_esencjaDemonaId = "Esencja_Senyaka";
					else if(_szponId.endsWith("_II"))
						_esencjaDemonaId = "Esencja_Eligora";
					else if(_szponId.endsWith("_III")) {
						p.closeInventory();
						return;
					}
					else if(_szponId.endsWith("Weak_Str")) {
						p.closeInventory();
						return;
					}
					else
						_esencjaDemonaId = "Esencja_Dramtara";
					
					if(_klejnotId == null
							|| _esencjaDemonaId == null) {
						p.closeInventory();
						return;
					}
					
					if(!_klejnotNBT.getString("MYTHIC_TYPE").equals(_klejnotId)
							|| !_esencjaDemonaNBT.getString("MYTHIC_TYPE").equals(_esencjaDemonaId)) {
						p.closeInventory();
						return;
					}
					
					String add = _szponId.endsWith("I") ? "I" : "_I";
					ItemStack newSzpon = MythicBukkit.inst().getItemManager().getItemStack(_szponId+add);
					
					inv.clear();
					Utils.dropItemStack(player, newSzpon);
					p.closeInventory();
					
				}));
			}

			@Override
			public void close(Player player, RyseInventory inventory) {
				Inventory inv = inventory.getInventory();
				for(int i = 0; i < upgradeFreeSlots.length; ++i) {
					int slot = upgradeFreeSlots[i];
					ItemStack it = inv.getItem(slot);
					if(it == null 
							|| it.getType().equals(Material.AIR))
						continue;
					Utils.dropItemStack(player, it);
				}
			}
		};
	}
	
	public static final SzponBeliaraManager getInstance() {
		return instance;
	}
	
	public void openSzponAwakeningMenu(Player p) {
		RyseInventory.builder()
			.title("§5§lUlepszanie Szponu Beliara")
			.size(27)
			.ignoredSlots(awakeningFreeSlots)
//			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(SzponBeliaraEvents.getEvents().getAwakeningClickEvent())
			.listener(SzponBeliaraEvents.getEvents().getAwakeningCloseEvent())
			.disableUpdateTask()
			.provider(awakeningProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public void openSzponUpgradeMenu(Player p) {
		RyseInventory.builder()
			.title("§5§lBeliar")
			.size(27)
			.ignoredSlots(upgradeFreeSlots)
//			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.disableUpdateTask()
			.provider(upgradeProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
}
