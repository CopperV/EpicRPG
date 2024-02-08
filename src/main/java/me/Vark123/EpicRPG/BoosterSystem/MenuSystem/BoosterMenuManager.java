package me.Vark123.EpicRPG.BoosterSystem.MenuSystem;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.TimeSetting;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.BoosterSystem.Booster;
import me.Vark123.EpicRPG.BoosterSystem.BoosterManager;

public class BoosterMenuManager {

	private static final BoosterMenuManager instance = new BoosterMenuManager();

	private final ItemStack empty;

	private final ItemStack expBoost;
	private final ItemStack moneyBoost;
	private final ItemStack stygiaBoost;
	private final ItemStack coinsBoost;
	private final ItemStack rudaBoost;
	
	private final RyseInventory inv;
	
	private final String pattern;
	private final SimpleDateFormat simpleDateFormat;

	private BoosterMenuManager() {
		pattern = "dd.MM.yyyy HH:mm:ss";
		simpleDateFormat = new SimpleDateFormat(pattern);
		
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}

		expBoost = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
		moneyBoost = new ItemStack(Material.GOLD_NUGGET, 1);
		stygiaBoost = new ItemStack(Material.GHAST_TEAR, 1);
		coinsBoost = new ItemStack(Material.REDSTONE, 1);
		rudaBoost = new ItemStack(Material.LAPIS_LAZULI, 1);

		inv = RyseInventory.builder()
				.title(Main.getInstance().getPrefix() + " §6§lMODYFIKATORY")
				.size(27)
				.period(5, TimeSetting.SECONDS)
				.provider(new InventoryProvider() {
					@Override
					public void init(Player player, InventoryContents contents) {
						for (int i = 0; i < 27; ++i)
							contents.set(i, empty);
						updateBoosters(contents);
					}
					@Override
					public void update(Player player, InventoryContents contents) {
						updateBoosters(contents);
					}
				}).build(Main.getInstance());
	}

	public static final BoosterMenuManager get() {
		return instance;
	}

	public void openMenu(Player p) {
		inv.open(p);
	}

	private void updateBoosters(InventoryContents contents) {
		Entry<Booster, Date> booster = BoosterManager.get().getTopBooster("xp");
		if (booster != null) {
			ItemMeta im = expBoost.getItemMeta();
			im.setDisplayName("§eXP §o+"+String.format("%02d", (int)(booster.getKey().getModifier()*100))+"%");
			im.setLore(Arrays.asList(" ","§7Aktywny do §a§o"+simpleDateFormat.format(booster.getValue())));
			expBoost.setItemMeta(im);
		} else {
			ItemMeta im = expBoost.getItemMeta();
			im.setDisplayName("§7XP §8[§cNIEAKTYWNY§8]");
			im.setLore(new LinkedList<>());
			expBoost.setItemMeta(im);
		}
		booster = BoosterManager.get().getTopBooster("money");
		if (booster != null) {
			ItemMeta im = moneyBoost.getItemMeta();
			im.setDisplayName("§6Kasa §o+"+String.format("%02d", (int)(booster.getKey().getModifier()*100))+"%");
			im.setLore(Arrays.asList(" ","§7Aktywny do §a§o"+simpleDateFormat.format(booster.getValue())));
			moneyBoost.setItemMeta(im);
		} else {
			ItemMeta im = moneyBoost.getItemMeta();
			im.setDisplayName("§7Kasa §8[§cNIEAKTYWNY§8]");
			im.setLore(new LinkedList<>());
			moneyBoost.setItemMeta(im);
		}
		booster = BoosterManager.get().getTopBooster("stygia");
		if (booster != null) {
			ItemMeta im = stygiaBoost.getItemMeta();
			im.setDisplayName("§3Stygia §o+"+String.format("%02d", (int)(booster.getKey().getModifier()*100))+"%");
			im.setLore(Arrays.asList(" ","§7Aktywny do §a§o"+simpleDateFormat.format(booster.getValue())));
			stygiaBoost.setItemMeta(im);
		} else {
			ItemMeta im = stygiaBoost.getItemMeta();
			im.setDisplayName("§7Stygia §8[§cNIEAKTYWNY§8]");
			im.setLore(new LinkedList<>());
			stygiaBoost.setItemMeta(im);
		}
		booster = BoosterManager.get().getTopBooster("coins");
		if (booster != null) {
			ItemMeta im = coinsBoost.getItemMeta();
			im.setDisplayName("§4Smocze Monety §o+"+String.format("%02d", (int)(booster.getKey().getModifier()*100))+"%");
			im.setLore(Arrays.asList(" ","§7Aktywny do §a§o"+simpleDateFormat.format(booster.getValue())));
			coinsBoost.setItemMeta(im);
		} else {
			ItemMeta im = coinsBoost.getItemMeta();
			im.setDisplayName("§7Smocze Monety §8[§cNIEAKTYWNY§8]");
			im.setLore(new LinkedList<>());
			coinsBoost.setItemMeta(im);
		}
		booster = BoosterManager.get().getTopBooster("ruda");
		if (booster != null) {
			ItemMeta im = rudaBoost.getItemMeta();
			im.setDisplayName("§9Brylki Rudy §o+"+String.format("%02d", (int)(booster.getKey().getModifier()*100))+"%");
			im.setLore(Arrays.asList(" ","§7Aktywny do §a§o"+simpleDateFormat.format(booster.getValue())));
			rudaBoost.setItemMeta(im);
		} else {
			ItemMeta im = rudaBoost.getItemMeta();
			im.setDisplayName("§7Brylki Rudy §8[§cNIEAKTYWNY§8]");
			im.setLore(new LinkedList<>());
			rudaBoost.setItemMeta(im);
		}
		
		contents.set(9, expBoost);
		contents.set(11, moneyBoost);
		contents.set(13, stygiaBoost);
		contents.set(15, coinsBoost);
		contents.set(17, rudaBoost);
	}

}
