package me.Vark123.EpicRPG.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.RpgReputation;
import me.Vark123.EpicRPG.Players.RpgRzemiosla;
import me.Vark123.EpicRPG.Players.RpgSkills;
import me.Vark123.EpicRPG.Players.RpgStats;
import me.Vark123.EpicRPG.Players.RpgVault;
import me.Vark123.EpicRPG.Utils.Pair;

public class FileOperations {

	private static File users = new File(Main.getInstance().getDataFolder(), "users");
	private static File phorses = new File(Main.getInstance().getDataFolder(), "phorses");
	private static File jewelry = new File(Main.getInstance().getDataFolder(), "jewelry");
	private static File exp = new File(Main.getInstance().getDataFolder(), "exp.yml");
	private static File blackrock = new File(Main.getInstance().getDataFolder(), "blackrock.yml");

	public static void checkFiles() {
		if(!Main.getInstance().getDataFolder().exists()) {
			Main.getInstance().getDataFolder().mkdir();
		}
		if(!new File(Main.getInstance().getDataFolder(), "config.yml").exists()) {
			Main.getInstance().saveDefaultConfig();
		}
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
		if(!blackrock.exists()) {
			try {
				blackrock.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
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
				fYml.getConfigurationSection(s+".points").getKeys(false).parallelStream().forEach(k ->{
					EpicRPGMobManager.getInstance().addMobPoints(mob, k, fYml.getInt(s+".points."+k));
				});
			}
		});
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
		fYml.set("zdolnosci", stats.getZdolnosci());
		fYml.set("walka", stats.getWalka());
		fYml.set("health", stats.getHealth());
		fYml.set("mana", stats.getMana());
		fYml.set("present_mana", stats.getPresentMana());
		fYml.set("inteligencja", stats.getInteligencja());
		fYml.set("krag", stats.getKrag());
		fYml.set("potion_sila", stats.getPotionSila());
		fYml.set("potion_wytrzymalosc", stats.getPotionWytrzymalosc());
		fYml.set("potion_zrecznosc", stats.getPotionZrecznosc());
		fYml.set("potion_zdolnosci", stats.getPotionZdolnosci());
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
	
	private static File getPlayerJewelryFile(Player p) {
		return new File(jewelry, p.getName().toLowerCase() + ".yml");
	}
	
	public static boolean hasPlayerJewelryFile(Player p) {
		return (new File(jewelry, p.getName().toLowerCase() + ".yml").exists());
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
	
	public static void loadPlayerJewelry(RpgPlayer rpg, Player p) {
		File f = getPlayerJewelryFile(p);
		if(f == null)
			return;
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		if(!fYml.contains("slots"))
			return;
		if(fYml.contains("slots.0")) {
			rpg.getJewelry().getAkcesoria().get(0).setItem(fYml.getItemStack("slots.0"));
		}
		if(fYml.contains("slots.1")) {
			rpg.getJewelry().getAkcesoria().get(1).setItem(fYml.getItemStack("slots.1"));
		}
		if(fYml.contains("slots.2")) {
			rpg.getJewelry().getAkcesoria().get(2).setItem(fYml.getItemStack("slots.2"));
		}
		if(fYml.contains("slots.3")) {
			rpg.getJewelry().getAkcesoria().get(3).setItem(fYml.getItemStack("slots.3"));
		}
	}
	
	public static void savePlayerJewerly(RpgPlayer rpg) {
		File f = getPlayerJewelryFile(rpg.getPlayer());
		if(f == null)
			return;
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		
		rpg.getJewelry().getAkcesoria().forEach((i, jewerlyItem) -> {
			fYml.set("slots."+i, jewerlyItem.getItem());
		});
		
		try {
			fYml.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
