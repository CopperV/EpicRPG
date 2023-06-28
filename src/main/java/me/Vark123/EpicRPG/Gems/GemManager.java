package me.Vark123.EpicRPG.Gems;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
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
public class GemManager {
	
	private static final GemManager instance = new GemManager();
	
	private final InventoryProvider powerfulProvider;
	private final int[] powerfulFreeSlots;
	private final List<String> powerfulGemKatedra;

	private final InventoryProvider annihilusProvider;
	private final int[] annihilusFreeSlots;
	private final List<String> annihilusGemKatedra;
	
	@Getter(value = AccessLevel.NONE)
	private final ItemStack empty;
	@Getter(value = AccessLevel.NONE)
	private final ItemStack create;
	
	private GemManager() {
		powerfulFreeSlots = new int[] {10,11,19,20,15};
		annihilusFreeSlots = new int[] {10,11,19,20,15,16};
		
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
		
		powerfulGemKatedra = Arrays.asList("arena4","arena9","arena11","arena7");
		annihilusGemKatedra = Arrays.asList("arena8","arena19","arena17","arena20");
		
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
	}
	
	public static GemManager getInstance() {
		return instance;
	}
	
	public void openPowerfulGemCreator(Player p) {
		RyseInventory.builder()
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
		RyseInventory.builder()
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
	
	public ItemStack getAnnihilus(ItemStack it1, ItemStack it2) {
		ItemExecutor manag = MythicBukkit.inst().getItemManager();
		ItemStack it = manag.getItemStack("Gem_Annihilus");
		
		Map<String, Integer> staty = new ConcurrentHashMap<>();
		double hp = 0;
		double speed = 0;
		double knock = 0;
		
		List<String> lore = new LinkedList<>();
		if(it1.hasItemMeta() && it1.getItemMeta().hasLore())
			lore.addAll(it1.getItemMeta().getLore());
		if(it2.hasItemMeta() && it2.getItemMeta().hasLore())
			lore.addAll(it2.getItemMeta().getLore());
		
		lore.parallelStream().filter(s -> {
			return s.contains(": ");
		}).forEach(s -> {
			String[] tmpArr = s.split(": §7");
			int value = Integer.parseInt(tmpArr[1]);
			int present = staty.getOrDefault(tmpArr[0], 0);
			staty.put(tmpArr[0], present+value);
		});
		
		NBTItem nbtit1 = new NBTItem(it1);
		NBTCompoundList listTmp = nbtit1.getCompoundList("AttributeModifiers");
		for(int i = 0; i < listTmp.size(); ++i) {
			NBTListCompound lc = listTmp.get(i);
			switch(lc.getString("Name").toLowerCase().replace("generic.", "")) {
				case "max_health":
					hp += lc.getDouble("Amount");
					break;
				case "knockback_resistance":
					knock += lc.getDouble("Amount");
					break;
				case "movement_speed":
					speed += lc.getDouble("Amount");
					break;
			}
		}
		
		NBTItem nbtit2 = new NBTItem(it2);
		listTmp = nbtit2.getCompoundList("AttributeModifiers");
		for(int i = 0; i < listTmp.size(); ++i) {
			NBTListCompound lc = listTmp.get(i);
			switch(lc.getString("Name").toLowerCase().replace("generic.", "")) {
				case "max_health":
					hp += lc.getDouble("Amount");
					break;
				case "knockback_resistance":
					knock += lc.getDouble("Amount");
					break;
				case "movement_speed":
					speed += lc.getDouble("Amount");
					break;
			}
		}
		
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
		NBTItem nbtit = new NBTItem(it);
		NBTCompoundList attribute = nbtit.getCompoundList("AttributeModifiers");
		
		if(hp != 0) {
			UUID uuid = UUID.randomUUID();
			NBTListCompound hpTag = attribute.addCompound();
			hpTag.setString("AttributeName", "generic.max_health");
			hpTag.setString("Name", "generic.max_health");
			hpTag.setDouble("Amount", hp);
			hpTag.setInteger("Operation", 0);
			hpTag.setLong("UUIDLeast", uuid.getLeastSignificantBits());
			hpTag.setLong("UUIDMost", uuid.getMostSignificantBits());
			hpTag.setString("Slot", "offhand");			
		}
		
		if(knock != 0) {
			UUID uuid = UUID.randomUUID();
			NBTListCompound knockTag = attribute.addCompound();
			knockTag.setString("AttributeName", "generic.knockback_resistance");
			knockTag.setString("Name", "generic.knockback_resistance");
			knockTag.setDouble("Amount", knock);
			knockTag.setInteger("Operation", 0);
			knockTag.setLong("UUIDLeast", uuid.getLeastSignificantBits());
			knockTag.setLong("UUIDMost", uuid.getMostSignificantBits());
			knockTag.setString("Slot", "offhand");
		}
		
		if(speed != 0) {
			UUID uuid = UUID.randomUUID();
			NBTListCompound speedTag = attribute.addCompound();
			speedTag.setString("AttributeName", "generic.movement_speed");
			speedTag.setString("Name", "generic.movement_speed");
			speedTag.setDouble("Amount", speed);
			speedTag.setInteger("Operation", 1);
			speedTag.setLong("UUIDLeast", uuid.getLeastSignificantBits());
			speedTag.setLong("UUIDMost", uuid.getMostSignificantBits());
			speedTag.setString("Slot", "offhand");
		}

		nbtit.setInteger("annihilus", 1);
		nbtit.applyNBT(it);
		
		return it;
	}

}
