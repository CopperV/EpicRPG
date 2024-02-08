package me.Vark123.EpicRPG.Options.Menus;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import me.Vark123.EpicOptions.MenuSystem.OptionMenuManager;
import me.Vark123.EpicOptions.PlayerSystem.OPlayer;
import me.Vark123.EpicOptions.PlayerSystem.PlayerOption;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.HorseSystem.AEpicHorse;
import me.Vark123.EpicRPG.HorseSystem.HorseManager;
import me.Vark123.EpicRPG.Options.Serializables.StringSerializable;

public class HorseOptionMenuManager {

	private static final HorseOptionMenuManager inst = new HorseOptionMenuManager();

	private final ItemStack empty;
	private final ItemStack back;

	private final ItemStack remove;

	private HorseOptionMenuManager() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		back = new ItemStack(Material.BARRIER, 1);
		{
			ItemMeta im = back.getItemMeta();
			im.setDisplayName("§cPOWROT");
			back.setItemMeta(im);
		}

		remove = new ItemStack(Material.STONE_SHOVEL, 1);
		{
			ItemMeta im = remove.getItemMeta();
			im.setDisplayName("§cUSUN");
			remove.setItemMeta(im);
		}
	}

	public static final HorseOptionMenuManager get() {
		return inst;
	}

	public void openMenu(OPlayer op) {
		Player p = op.getPlayer();
		@SuppressWarnings("unchecked")
		PlayerOption<StringSerializable> option = (PlayerOption<StringSerializable>) op
				.getPlayerOptionByID("epicrpg_horse").orElseThrow();
		String strOption = option.getValue().getValue();

		File f = new File(FileOperations.getPlayerHorsesFolder(), p.getName().toLowerCase() + "horse.yml");
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		Map<String, AEpicHorse> horses = new LinkedHashMap<>();
		fYml.getStringList("horses").stream().forEach(key -> {
			AEpicHorse horse =  HorseManager.getInstance().getHorse(key);
			if(horse == null)
				return;
			horses.put(key, horse);
		});

		int size = 9 + horses.size();
		if (horses.keySet().contains(strOption))
			--size;
		int rows = (size - 1) / 9 + 1;

		RyseInventory.builder()
				.title("§7§lUSTAWIENIA - §a§lDOMYSLNY WIERZCHOWIEC")
				.rows(rows)
				.disableUpdateTask()
				.provider(new InventoryProvider() {
					@Override
					public void init(Player player, InventoryContents contents) {
						String display = horses.keySet().contains(strOption)
								? horses.get(strOption).getMenuMountItem()
										.getItemMeta().getDisplayName()
								: "§cBRAK";

						for (int i = 0; i < 9; ++i)
							contents.set(i, empty);

						contents.set(0, IntelligentItem.of(back, e -> OptionMenuManager.get().openMenu(p)));
						if (horses.keySet().contains(strOption))
							contents.set(8, IntelligentItem.of(remove, e -> {
								p.sendMessage(Main.getInstance().getPrefix() + " " + display + " §cnie jest juz Twoim domyslnym wierzchowcem!");
								option.getValue().setValue("0");
								p.closeInventory();
							}));
						
						ItemStack infoItem = new ItemStack(Material.PAPER);{
							ItemMeta im = infoItem.getItemMeta();
							im.setDisplayName("§7Przypisany wierzchowiec: §r"+display);
							infoItem.setItemMeta(im);
						}
						contents.set(4, infoItem);
						
						MutableInt index = new MutableInt(9);
						horses.forEach((key, horse) -> {
							if(strOption.equals(key))
								return;
							
							contents.set(index.getAndIncrement(), IntelligentItem.of(horse.getMenuMountItem(), e -> {
								p.sendMessage(Main.getInstance().getPrefix() + " " + horse.getMenuMountItem().getItemMeta().getDisplayName() + " §azostal ustawiony jako domyslny wierzchowiec!");
								option.getValue().setValue(key);
								p.closeInventory();
							}));
						});
					}
				})
				.build(Main.getInstance())
				.open(p);
	}

}
