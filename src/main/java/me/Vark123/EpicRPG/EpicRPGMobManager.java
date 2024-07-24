package me.Vark123.EpicRPG;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Utils.Pair;
import me.clip.placeholderapi.PlaceholderAPI;

@Singleton
public class EpicRPGMobManager {

	private final static EpicRPGMobManager instance = new EpicRPGMobManager();
	
	private final Map<String, Pair<Integer, Integer>> mobExp;
	private final Map<String, List<String>> mobCmds;
	private final Map<String, Integer> mobCoins;
	private final Map<String, Map<String,Integer>> mobPoints;
	private final Map<String, Double> mobMoney;
	private final Random rand;
	
	private final List<String> waterMobs = Arrays.asList(
			ChatColor.translateAlternateColorCodes('&', "&bZywiolak wody"),
			ChatColor.translateAlternateColorCodes('&', "&9Straznik wod"),
			ChatColor.translateAlternateColorCodes('&', "&bStraznik Swiatynny"),
			ChatColor.translateAlternateColorCodes('&', "&b&lNeptulon - Wladca Wod - BOSS"),
			ChatColor.translateAlternateColorCodes('&', "&3&oZbieracz"),
			ChatColor.translateAlternateColorCodes('&', "&2&oBagienny szczur"),
			ChatColor.translateAlternateColorCodes('&', "&2Krwiopijca bagienny"),
			ChatColor.translateAlternateColorCodes('&', "&2&lBagienny truten"),
			ChatColor.translateAlternateColorCodes('&', "&e&oWaz blotny"),
			ChatColor.translateAlternateColorCodes('&', "&3&lPelzacz swiatynny"),
			ChatColor.translateAlternateColorCodes('&', "&9&lSwiatynna Krolowa Pelzaczy"),
			ChatColor.translateAlternateColorCodes('&', "&9&lKejran - BOSS"),
			ChatColor.translateAlternateColorCodes('&', "&c&oKrwiozercza Pijawka"),
			ChatColor.translateAlternateColorCodes('&', "&a&oDrifter"),
			ChatColor.translateAlternateColorCodes('&', "&2Szambler"),
			ChatColor.translateAlternateColorCodes('&', "&e&lPurchlak"),
			ChatColor.translateAlternateColorCodes('&', "&e&oCordyceps"),
			ChatColor.translateAlternateColorCodes('&', "&2&lElement Zero - BOSS"),
			ChatColor.translateAlternateColorCodes('&', "&8Szczur kanalowy"),
			ChatColor.translateAlternateColorCodes('&', "&3Megawonsz9"),
			ChatColor.translateAlternateColorCodes('&', "&4&l&oBeltessor"));
	
	private EpicRPGMobManager() {
		rand = new Random();
		mobExp = new ConcurrentHashMap<>();
		mobCmds = new ConcurrentHashMap<>();
		mobCoins = new ConcurrentHashMap<>();
		mobPoints = new ConcurrentHashMap<>();
		mobMoney = new ConcurrentHashMap<>();
	}
	
	public static EpicRPGMobManager getInstance() {
		return instance;
	}
	
	public void clear() {
		mobExp.clear();
		mobCmds.clear();
		mobCoins.clear();
		mobPoints.clear();
	}

	public void addMobExp(String mob, Pair<Integer, Integer> xp) {
		mobExp.put(mob, xp);
	}
	
	public Pair<Integer, Integer> getMobExp(String mob){
		return mobExp.getOrDefault(mob, new Pair<>(0, 1));
	}
	
	public int getRandomMobExp(String mob) {
		Pair<Integer, Integer> pair = getMobExp(mob);
		return rand.nextInt(pair.getKey(), pair.getValue());
	}
	
	public void addMobCmds(String mob, List<String> cmds) {
		mobCmds.put(mob, cmds);
	}
	
	public List<String> getMobCmds(String mob) {
		return mobCmds.getOrDefault(mob, new LinkedList<>());
	}
	
	public void executeCommands(String mob) {
		if(!mobCmds.containsKey(mob))
			return;
		mobCmds.get(mob).stream().forEach(s -> {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
		});
	}
	
	public void executeCommands(Player p, String mob) {
		if(!mobCmds.containsKey(mob))
			return;
		mobCmds.get(mob).stream().forEach(s -> {
			s = PlaceholderAPI.setPlaceholders(p, s);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
		});
	}
	
	public void addMobCoins(String mob, int coins) {
		mobCoins.put(mob, coins);
	}
	
	public int getMobCoins(String mob) {
		return mobCoins.getOrDefault(mob, 0);
	}
	
	public void addMobPoints(String mob, String id, int amount) {
		Map<String, Integer> map = mobPoints.getOrDefault(mob, new HashMap<>());
		map.put(id, amount);
		mobPoints.put(mob, map);
	}
	
	public int getMobPoints(String mob, String id) {
		return mobPoints.getOrDefault(mob, new HashMap<>()).getOrDefault(id, 0);
	}
	
	public void addMobMoney(String mob, double money) {
		mobMoney.put(mob, money);
	}
	
	public double getMobMoney(String mob) {
		return mobMoney.getOrDefault(mob, 0d);
	}
	
	public boolean isWaterMob(String mob) {
		return waterMobs.contains(mob);
	}

}
