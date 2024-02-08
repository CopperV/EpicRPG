package me.Vark123.EpicRPG.HorseSystem;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.HorseSystem.Horses.BaseHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.DeathRider;
import me.Vark123.EpicRPG.HorseSystem.Horses.DiamondHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.ESkeletonHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.EZombieHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.GoldenHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.GoldenSteed;
import me.Vark123.EpicRPG.HorseSystem.Horses.HauntedHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.IronHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.KidHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.MasterHorse;

@Getter
public class HorseManager {

	private static final HorseManager instance = new HorseManager();
	
	private final Map<Player, Date> horseCd = new HashMap<>();
	
	@Getter(value = AccessLevel.NONE)
	public final int HORSE_SUMMON_CD = 20;
	
	private HorseManager() {
		
	}
	
	public static final HorseManager getInstance() {
		return instance;
	}
	
	public AEpicHorse getHorse(String id) {
		switch(id.toLowerCase()) {
			case "1":
				return new BaseHorse();
			case "2":
				return new IronHorse();
			case "3":
				return new GoldenHorse();
			case "4":
				return new DiamondHorse();
			case "5":
				return new EZombieHorse();
			case "6":
				return new ESkeletonHorse();
			case "7":
				return new MasterHorse();
			case "8":
				return new HauntedHorse();
			case "9":
				return new KidHorse();
			case "10":
				return new DeathRider();
			case "11":
				return new GoldenSteed();
			default:
				return null;
		}
	}
	
	public void openMenu(Player p) {
		File f = new File(FileOperations.getPlayerHorsesFolder(), p.getName().toLowerCase()+"horse.yml");
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		List<? extends AEpicHorse> horses = fYml.getStringList("horses").stream()
				.map(this::getHorse).toList();
		int size = 1 + (horses.size()-1)/9;
		size *= 9;
		RyseInventory.builder()
			.title("§7§lWybor wierzchowca")
			.size(size)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(HorseMenuEvents.getEvents().getClickEvent())
			.disableUpdateTask()
			.provider(getProvider(horses))
			.build(Main.getInstance())
			.open(p);
	}
	
	private InventoryProvider getProvider(List<? extends AEpicHorse> horses) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				int slot = 0;
				for(AEpicHorse horse : horses) {
					contents.set(slot, horse.getMenuMountItem());
					++slot;
				}
			}
		};
	}
	
}
