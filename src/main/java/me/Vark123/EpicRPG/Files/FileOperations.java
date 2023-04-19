package me.Vark123.EpicRPG.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Pair;

public class FileOperations {

	private static File users = new File(Main.getInstance().getDataFolder(), "users");
	private static File phorses = new File(Main.getInstance().getDataFolder(), "phorses");
	private static File jewerly = new File(Main.getInstance().getDataFolder(), "jewerly");
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
		if(!jewerly.exists()) {
			jewerly.mkdir();
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
}