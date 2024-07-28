package me.Vark123.EpicRPG.Files;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.BlackrockSystem.BlackrockManager;
import me.Vark123.EpicRPG.BoosterSystem.Booster;
import me.Vark123.EpicRPG.BoosterSystem.BoosterManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgJewelry;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgReputation;
import me.Vark123.EpicRPG.Players.Components.RpgRzemiosla;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Players.Components.RpgVault;
import me.Vark123.EpicRPG.UpgradableSystem.UpgradableInhibitor;
import me.Vark123.EpicRPG.UpgradableSystem.UpgradableLevel;
import me.Vark123.EpicRPG.UpgradableSystem.UpgradableManager;
import me.Vark123.EpicRPG.UpgradableSystem.UpgradableInhibitor.InhibitorCrafting;
import me.Vark123.EpicRPG.Utils.Pair;

public class FileOperations {

	private static File users = new File(Main.getInstance().getDataFolder(), "users");
	private static File phorses = new File(Main.getInstance().getDataFolder(), "phorses");
	private static File jewelry = new File(Main.getInstance().getDataFolder(), "jewelry");
	private static File oldJewelry = new File(Main.getInstance().getDataFolder(), "old_jewelry");
	private static File backItems = new File(Main.getInstance().getDataFolder(), "backs");
	private static File exp = new File(Main.getInstance().getDataFolder(), "exp.yml");
	private static File blackrock = new File(Main.getInstance().getDataFolder(), "blackrock.yml");
	private static File boosters = new File(Main.getInstance().getDataFolder(), "boosters.yml");
	private static File upgrades = new File(Main.getInstance().getDataFolder(), "upgrades.yml");
	@Getter
	private static File config = new File(Main.getInstance().getDataFolder(), "config.yml");

	public static void checkFiles() {
		if(!Main.getInstance().getDataFolder().exists()) {
			Main.getInstance().getDataFolder().mkdir();
		}
		Main.getInstance().saveResource("config.yml", false);
		Main.getInstance().getConfig().options().copyDefaults(true);
		Main.getInstance().saveConfig();
		if(!exp.exists())
			try {
				exp.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		if(!users.exists()) {
			users.mkdir();
		}
		if(!phorses.exists()) {
			phorses.mkdir();
		}
		if(!jewelry.exists()) {
			jewelry.mkdir();
		}
		if(!backItems.exists())
			backItems.mkdir();
		if(!blackrock.exists()) {
			try {
				blackrock.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!boosters.exists())
			try {
				boosters.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		if(!upgrades.exists())
			try {
				upgrades.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		if(oldJewelry.exists())
			convert();
		
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(exp);
		fYml.getKeys(false).stream().parallel().forEach(s -> {
			String mob = ChatColor.translateAlternateColorCodes('&', fYml.getString(s+".name"));
			Pair<Integer, Integer> mobExp = new Pair<>(fYml.getInt(s+".min"), fYml.getInt(s+".max"));
			EpicRPGMobManager.getInstance().addMobExp(mob, mobExp);
			
			if(fYml.contains(s+".money")) {
				EpicRPGMobManager.getInstance().addMobMoney(mob, fYml.getDouble(s+".money"));
			}
			
			if(fYml.contains(s+".cmds")) {
				List<String> cmds = fYml.getStringList(s+".cmds");
				EpicRPGMobManager.getInstance().addMobCmds(mob, cmds);
			}
			
			if(fYml.contains(s+".coins")) {
				EpicRPGMobManager.getInstance().addMobCoins(mob, fYml.getInt(s+".coins"));
			}
			
			if(fYml.contains(s+".points")) {
				fYml.getConfigurationSection(s+".points").getKeys(false).stream().forEach(k ->{
					EpicRPGMobManager.getInstance().addMobPoints(mob, k, fYml.getInt(s+".points."+k));
				});
			}
		});
		
		YamlConfiguration blackrockYml = YamlConfiguration.loadConfiguration(blackrock);
		if(blackrockYml.contains("lista")) {
			List<String> tmp = blackrockYml.getStringList("lista");
			tmp.stream().map(s -> {
				UUID uid = UUID.fromString(s);
				return Bukkit.getPlayer(uid);
			}).forEach(completed -> {
				BlackrockManager.getInstance().completeDailyBlackrock(completed);
			});
		}
		
		YamlConfiguration boosterYml = YamlConfiguration.loadConfiguration(boosters);
		boosterYml.getKeys(false).stream().forEach(name -> {
			ConfigurationSection boosterSection = boosterYml.getConfigurationSection(name);
			String display = ChatColor.translateAlternateColorCodes('&', boosterSection.getString("display"));
			boosterSection.getKeys(false).forEach(key -> {
				double modifier = boosterSection.getDouble(key+".modifier");
				long exp = boosterSection.getLong(key+".exp");
				BoosterManager.get().initBooster(name, display, modifier, exp);
			});
		});
		
		YamlConfiguration upgradesYml = YamlConfiguration.loadConfiguration(upgrades);
		if(upgradesYml.isConfigurationSection("upgrades")) {
			ConfigurationSection section = upgradesYml.getConfigurationSection("upgrades");
			section.getKeys(false).stream()
				.filter(section::isConfigurationSection)
				.map(section::getConfigurationSection)
				.forEach(upgradeSection -> {
					int level = upgradeSection.getInt("level");
					double chance = upgradeSection.getDouble("chance");
					Map<String, Integer> items = new LinkedHashMap<>();
					if(upgradeSection.isConfigurationSection("items")) {
						ConfigurationSection itemSection = upgradeSection.getConfigurationSection("items");
						itemSection.getKeys(false).stream()
							.filter(itemSection::isInt)
							.forEach(key -> items.put(key, itemSection.getInt(key)));
					}
					UpgradableManager.get().registerUpgradableLevel(new UpgradableLevel(level, chance, items));
				});
		}
		if(upgradesYml.isConfigurationSection("inhibitors")) {
			ConfigurationSection section = upgradesYml.getConfigurationSection("inhibitors");
			section.getKeys(false).stream()
				.filter(section::isConfigurationSection)
				.forEach(mmId -> {
					ConfigurationSection inhibitorSection = section.getConfigurationSection(mmId);
					double chance = inhibitorSection.getDouble("chance", 0);
					if(inhibitorSection.isConfigurationSection("crafting")) {
						ConfigurationSection craftingSection = inhibitorSection.getConfigurationSection("crafting");
						double money = craftingSection.getDouble("money");
						Map<String, Integer> items = new LinkedHashMap<>();
						if(craftingSection.isConfigurationSection("items")) {
							ConfigurationSection itemSection = craftingSection.getConfigurationSection("items");
							itemSection.getKeys(false).stream()
								.filter(itemSection::isInt)
								.forEach(item -> items.put(item, itemSection.getInt(item)));
						}
						InhibitorCrafting crafting = new InhibitorCrafting(money, items);
						UpgradableManager.get().registerInhibitor(UpgradableInhibitor.builder()
								.mmId(mmId)
								.chance(chance)
								.crafting(crafting)
								.build());
					}
				});
		}
	}
	
	@Deprecated
	public static boolean playerStatFileExists(Player p) {
		return (new File(users, p.getName().toLowerCase()+".yml").exists());
	}
	
	@Deprecated
	public static YamlConfiguration getPlayerStatYaml(Player p) {
		File f = new File(users, p.getName().toLowerCase()+".yml");
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		return fYml;
	}
	
	@Deprecated
	public static void saveStats(RpgPlayer rpg) {
		File f = new File(users, 
				rpg.getPlayer().getName().toLowerCase()+".yml");
		if(!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage("Blad tworzenia pliku stat gracza do pliku - "+rpg.getPlayer().getName());
				Bukkit.getConsoleSender().sendMessage("Blad: "+e.getMessage());
				Bukkit.getConsoleSender().sendMessage("Dane gracza: ");
				Bukkit.getConsoleSender().sendMessage(rpg.toString());
				return;
			}
		RpgStats stats = rpg.getStats();
		RpgReputation rep = rpg.getReputation();
		RpgPlayerInfo playerInfo = rpg.getInfo();
		RpgRzemiosla rzemiosla = rpg.getRzemiosla();
		RpgSkills skills = rpg.getSkills();
		RpgVault vault = rpg.getVault();
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		if(!fYml.contains("nick")) fYml.set("nick", rpg.getPlayer().getName().toLowerCase());
		if(!fYml.contains("UUID")) fYml.set("UUID", rpg.getPlayer().getUniqueId());
		fYml.set("PN", playerInfo.getPn());
		fYml.set("proffesion", playerInfo.getProffesion());
		fYml.set("level", playerInfo.getLevel());
		fYml.set("exp", playerInfo.getExp());
		fYml.set("nextLevel", playerInfo.getNextLevel());
		fYml.set("sila", stats.getSila());
		fYml.set("zrecznosc", stats.getZrecznosc());
		fYml.set("wytrzymalosc", stats.getWytrzymalosc());
		fYml.set("zdolnosci", stats.getZdolnosciMysliwskie());
		fYml.set("walka", stats.getWalka());
		fYml.set("health", stats.getHealth());
		fYml.set("mana", stats.getMana());
		fYml.set("present_mana", stats.getPresentMana());
		fYml.set("inteligencja", stats.getInteligencja());
		fYml.set("krag", stats.getKrag());
		fYml.set("potion_sila", stats.getPotionSila());
		fYml.set("potion_wytrzymalosc", stats.getPotionWytrzymalosc());
		fYml.set("potion_zrecznosc", stats.getPotionZrecznosc());
		fYml.set("potion_zdolnosci", stats.getPotionZdolnosciMysliwskie());
		fYml.set("potion_mana", stats.getPotionMana());
		fYml.set("potion_inteligencja", stats.getPotionInteligencja());
		fYml.set("potion_walka", stats.getPotionWalka());
		fYml.set("potion_health", stats.getPotionHealth());
		fYml.set("alchemia", rzemiosla.hasAlchemia());
		fYml.set("platnerstwo", rzemiosla.hasPlatnerstwo());
		fYml.set("luczarstwo", rzemiosla.hasLuczarstwo());
		fYml.set("kowalstwo", rzemiosla.hasKowalstwo());
		fYml.set("jubilerstwo", rzemiosla.hasJubilerstwo());
		fYml.set("drop", playerInfo.isDrop());
		fYml.set("tutorial", playerInfo.isTutorial());
		fYml.set("manaReg", skills.hasManaReg());
		fYml.set("unlimitArr", skills.hasUnlimitedArrows());
		fYml.set("foodless", skills.hasHungerless());
		fYml.set("slugaBeliara", skills.hasSlugaBeliara());
		fYml.set("magKrwi", skills.hasMagKrwi());
		fYml.set("ciosKrytyczny", skills.hasCiosKrytyczny());
		fYml.set("magnetyzm", skills.hasMagnetyzm());
		fYml.set("polnocnyBarbarzynca", skills.hasPolnocnyBarbarzynca());
		fYml.set("silaZywiolow", skills.hasSilaZywiolow());
		fYml.set("rozprucie", skills.hasRozprucie());
		fYml.set("stygia", vault.getStygia());
		fYml.set("coins", vault.getDragonCoins());
		fYml.set("brylki", vault.getBrylkiRudy());
		try {
			fYml.save(f);
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage("Blad zapisu stat gracza do pliku - "+rpg.getPlayer().getName());
			Bukkit.getConsoleSender().sendMessage("Blad: "+e.getMessage());
			Bukkit.getConsoleSender().sendMessage("Dane gracza: ");
			Bukkit.getConsoleSender().sendMessage(rpg.toString());
		}
	}

	public static void saveBlackrockCompleted() {
		File f = blackrock;
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		List<String> toSave = BlackrockManager.getInstance().completedPlayers
				.stream().map(p -> {
					return p.getUniqueId().toString();
				}).toList();
		fYml.set("lista", toSave);
		try {
			fYml.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveBoosters() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(boosters);
		fYml.getKeys(false).forEach(key -> fYml.set(key, null));
		
		BoosterManager.get().getBoosters().entrySet().forEach(entry -> {
			Booster booster = entry.getKey();
			String name = booster.getName();
			String display = booster.getDisplay();
			double modifier = booster.getModifier();
			long exp = entry.getValue().getTime();
			
			String uid = UUID.randomUUID().toString();
			
			fYml.set(name+".display", display);
			fYml.set(name+"."+uid+".modifier", modifier);
			fYml.set(name+"."+uid+".exp", exp - new Date().getTime());
		});
		
		try {
			fYml.save(boosters);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static File getPlayerJewelryFile(Player p) {
		return new File(jewelry, p.getUniqueId().toString().toLowerCase() + ".yml");
	}
	
	public static boolean hasPlayerJewelryFile(Player p) {
		return (new File(jewelry, p.getUniqueId().toString().toLowerCase() + ".yml").exists());
	}
	
	public static void createPlayerJewelryFile(Player p) {
		File f = getPlayerJewelryFile(p);
		if(f.exists())
			return;
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadPlayerJewelry(RpgJewelry jewelry, Player p) {
		File f = getPlayerJewelryFile(p);
		if(f == null)
			return;
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		if(!fYml.contains("slots"))
			return;
		ConfigurationSection section = fYml.getConfigurationSection("slots");
		section.getKeys(false).forEach(key -> {
			if(!StringUtils.isNumeric(key))
				return;
			int slot = Integer.parseInt(key);
			ItemStack it = section.getItemStack(key);
			jewelry.getAkcesoria().get(slot).setItem(it);
		});
	}
	
	public static void savePlayerJewerly(RpgPlayer rpg) {
		File f = getPlayerJewelryFile(rpg.getPlayer());
		if(f == null)
			return;
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		
		fYml.set("last-nick", rpg.getPlayer().getName());
		rpg.getJewelry().getAkcesoria().forEach((i, jewerlyItem) -> {
			fYml.set("slots."+i, jewerlyItem.getItem());
		});
		
		try {
			fYml.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static File getPlayerBackItemFile(Player p) {
		return new File(backItems, p.getUniqueId().toString().toLowerCase() + ".yml");
	}
	
	public static boolean hasPlayerBackItemFile(Player p) {
		return new File(backItems, p.getUniqueId().toString().toLowerCase() + ".yml").exists();
	}
	
	public static void createPlayerBackItemFile(Player p) {
		File f = getPlayerBackItemFile(p);
		if(f.exists())
			return;
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ItemStack loadPlayerBackItem(Player p) {
		File f = getPlayerBackItemFile(p);
		if(f == null)
			return null;
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		ItemStack it = fYml.getItemStack("item");
		return it;
	}
	
	public static void savePlayerBackItem(RpgPlayer rpg) {
		File f = getPlayerBackItemFile(rpg.getPlayer());
		if(f == null)
			return;
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);

		fYml.set("last-nick", rpg.getPlayer().getName());
		fYml.set("item", rpg.getBackItem());
		
		try {
			fYml.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static File getPHorseFile(Player p) {
		File f = new File(phorses, p.getName().toLowerCase() + "horse.yml");
		if(!f.exists()) {
			return null;
		}
		return f;
	}
	
	public static File getPlayerHorsesFolder() {
		return phorses;
	}
	
	private static void convert() {
		Arrays.asList(oldJewelry.listFiles()).stream()
			.filter(file -> file.isFile())
			.filter(file -> file.getName().endsWith(".yml"))
			.forEach(file -> {
				YamlConfiguration fYml = YamlConfiguration.loadConfiguration(file);
				String nick = file.getName().replace(".yml", "");
				System.out.println(nick);
				OfflinePlayer op = Bukkit.getOfflinePlayer(nick);
				if(op == null)
					return;
				UUID uid = op.getUniqueId();
				if(uid == null)
					return;
				Object slots = fYml.get("slots");

				File file2 = new File(jewelry, uid+".yml");
				if(file2.exists()) {
					File toCompare1 = new File(oldJewelry, nick.toLowerCase()+".yml");
					YamlConfiguration fYml2 = YamlConfiguration.loadConfiguration(file2);
					String nick2 = fYml2.getString("last-nick");
					File toCompare2 = new File(oldJewelry, nick2.toLowerCase()+".yml");
					if(toCompare2.exists()
							&& FileUtils.isFileNewer(toCompare1, toCompare2))
						return;
					if(!toCompare2.exists())
						return;
				} else {
					try {
						file2.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				YamlConfiguration fYml2 = YamlConfiguration.loadConfiguration(file2);
				fYml2.set("last-nick", nick);
				fYml2.set("slots", slots);
				try {
					fYml2.save(file2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		oldJewelry.renameTo(new File(Main.getInstance().getDataFolder(), "archive"));
	}
	
}
