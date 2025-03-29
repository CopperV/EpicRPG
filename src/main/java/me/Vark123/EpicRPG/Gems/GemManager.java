package me.Vark123.EpicRPG.Gems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.ItemExecutor;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicInventory.Content.InventoryContents;
import me.Vark123.EpicInventory.Content.InventoryProvider;
import me.Vark123.EpicInventory.Enums.Action;
import me.Vark123.EpicInventory.Enums.DisabledEvents;
import me.Vark123.EpicInventory.Enums.DisabledInventoryClick;
import me.Vark123.EpicInventory.Pagination.EpicInventory;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.MergableAttribute;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class GemManager {
	
	private static final GemManager instance = new GemManager();
	
	private final InventoryProvider powerfulProvider;
	private final int[] powerfulFreeSlots;
	private final List<String> powerfulGemKatedra;

	private final InventoryProvider annihilusProvider;
	private final int[] annihilusFreeSlots;
	private final List<String> annihilusGemKatedra;

	private final InventoryProvider annihilusUpgradeProvider;
	private final int[] annihilusUpgradeFreeSlots;
	private final List<String> annihilusUpgradeGemKatedra;
	private final ItemStack witcherTrophies;
	private final ItemStack oldAnnihilus;
	private final ItemStack newGem;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	private final ItemStack create;
	
	private GemManager() {
		powerfulFreeSlots = new int[] {10,11,19,20,15};
		annihilusFreeSlots = new int[] {10,11,19,20,15,16};
		annihilusUpgradeFreeSlots = new int[] {10,11,12,13,14,15,16,29,33};
		
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
		witcherTrophies = new ItemStack(Material.LEATHER, 1);{
			ItemMeta im = witcherTrophies.getItemMeta();
			im.setDisplayName("§2§lTROFEA WIEDZMINSKIE");
			witcherTrophies.setItemMeta(im);
		}
		ItemExecutor manag = MythicBukkit.inst().getItemManager();
		oldAnnihilus = manag.getItemStack("Gem_Annihilus");
		newGem = manag.getItemStack("Gem_KnockBack1");{
			ItemMeta im = newGem.getItemMeta();
			im.setDisplayName("§7DOWOLNY GEM");
			newGem.setItemMeta(im);
		}
		
		powerfulGemKatedra = Arrays.asList("arena4","arena9","arena11","arena7");
		annihilusGemKatedra = Arrays.asList("arena8","arena19","arena17","arena20");
		annihilusUpgradeGemKatedra = Arrays.asList("arena2_2","arena2_6","arena2_3","arena2_8",
				"arena2_1","arena2_7","arena2_13");
		
		powerfulProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(powerfulFreeSlots);
				for(int i = 0; i < 36; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 31) {
						contents.set(i, create);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
		annihilusProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(annihilusFreeSlots);
				for(int i = 0; i < 36; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 31) {
						contents.set(i, create);
						continue;
					}
					contents.set(i, empty);
				}
			}
		};
		annihilusUpgradeProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<Integer> tmpList = Utils.intArrayToList(annihilusUpgradeFreeSlots);
				for(int i = 0; i < 45; ++i) {
					if(tmpList.contains(i))
						continue;
					if(i == 29)
						continue;
					contents.set(i, empty);
				}
				contents.set(4, witcherTrophies);
				contents.set(20, oldAnnihilus);
				contents.set(24, newGem);
				contents.set(40, create);
			}
		};
	}
	
	public static GemManager getInstance() {
		return instance;
	}
	
	public void openPowerfulGemCreator(Player p) {
		EpicInventory.builder()
			.title("§3§lTworzenie Poteznych Gemow")
			.size(36)
			.ignoredSlots(powerfulFreeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(GemEvents.getEvents().getPowerfulClickEvent())
			.listener(GemEvents.getEvents().getPowerfulCloseEvent())
			.disableUpdateTask()
			.provider(powerfulProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public void openAnnihilusGemCreator(Player p) {
		EpicInventory.builder()
			.title("§3§lLaczenie Gemow")
			.size(36)
			.ignoredSlots(annihilusFreeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(GemEvents.getEvents().getAnnihilusClickEvent())
			.listener(GemEvents.getEvents().getAnnihilusCloseEvent())
			.disableUpdateTask()
			.provider(annihilusProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public void openAnnihilusGemUpgrade(Player p) {
		EpicInventory.builder()
			.title("§6§lUlepszenie Annihilusa")
			.size(45)
			.ignoredSlots(annihilusUpgradeFreeSlots)
			.enableAction(Action.MOVE_TO_OTHER_INVENTORY)
			.ignoreClickEvent(DisabledInventoryClick.BOTTOM)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(GemEvents.getEvents().getAnnihilusUpgradeClickEvent())
			.listener(GemEvents.getEvents().getAnnihilusUpgradeCloseEvent())
			.disableUpdateTask()
			.provider(annihilusUpgradeProvider)
			.build(Main.getInstance())
			.open(p);
	}
	
	public ItemStack getAnnihilus(ItemStack it1, ItemStack it2) {
		ItemExecutor manag = MythicBukkit.inst().getItemManager();
		ItemStack it = manag.getItemStack("Gem_Annihilus");
		if(Utils.isMythicMobItem(it1)
				&& Utils.getMythicMobItemType(it1).equals("Gem_Annihilus"))
			it = manag.getItemStack("Gem_Annihilus_I");
		
		Map<String, Integer> staty = new ConcurrentHashMap<>();
//		double hp = 0;
//		double speed = 0;
//		double knock = 0;
//		Map<Attribute, List<AttributeModifier>> attributes = new LinkedHashMap<>();
		List<MergableAttribute> attributes = new ArrayList<>();
		
		List<String> lore = new LinkedList<>();
		if(it1.hasItemMeta() && it1.getItemMeta().hasLore())
			lore.addAll(it1.getItemMeta().getLore());
		if(it2.hasItemMeta() && it2.getItemMeta().hasLore())
			lore.addAll(it2.getItemMeta().getLore());
		
		lore.stream().filter(s -> {
			return s.contains(": ");
		}).forEach(s -> {
			String[] tmpArr = s.split(": §7");
			if(tmpArr.length < 2)
				return;
			int value = Integer.parseInt(tmpArr[1]);
			int present = staty.getOrDefault(tmpArr[0], 0);
			staty.put(tmpArr[0], present+value);
		});

		if(it1.getItemMeta().getAttributeModifiers() != null)
			it1.getItemMeta().getAttributeModifiers().entries()
				.stream()
				.filter(attribute -> attribute.getValue().getSlotGroup().equals(EquipmentSlotGroup.OFFHAND))
				.forEach(entry -> {
					MergableAttribute ma = new MergableAttribute(entry.getKey(), entry.getValue());
					attributes.stream()
						.filter(ma::isSimiliar)
						.findFirst()
						.ifPresentOrElse(
								attr -> attr.merge(ma),
								() -> attributes.add(ma));
				});
		
		if(it2.getItemMeta().getAttributeModifiers() != null)
			it2.getItemMeta().getAttributeModifiers().entries()
				.stream()
				.filter(attribute -> attribute.getValue().getSlotGroup().equals(EquipmentSlotGroup.OFFHAND))
				.forEach(entry -> {
					MergableAttribute ma = new MergableAttribute(entry.getKey(), entry.getValue());
					attributes.stream()
						.filter(ma::isSimiliar)
						.findFirst()
						.ifPresentOrElse(
								attr -> attr.merge(ma),
								() -> attributes.add(ma));
				});
		
		if(!staty.isEmpty()) {
			List<String> newLore = new LinkedList<>();
			newLore.add(" ");
			staty.forEach((s, v) -> {
				if(v == 0)
					return;
				if(v > 0)
					newLore.add(s+": §7+"+v);
				else
					newLore.add(s+": §7"+v);
			});
			ItemMeta im = it.getItemMeta();
			im.setLore(newLore);
			it.setItemMeta(im);
		}
		
		ItemMeta im = it.getItemMeta();
		attributes.forEach(ma -> {
			im.addAttributeModifier(ma.getAttribute(), ma.getAttributeModifier());
		});
		it.setItemMeta(im);

		int level = 0;
		
		EpicComponent itComp = new EpicComponent(it, MythicBukkit.inst());
		if(itComp.hasKey("annihilus"))
			level = itComp.getInteger("annihilus");
		itComp.setInteger("annihilus", level + 1);
		itComp.applyTo(it);
		
		return it;
	}

}
