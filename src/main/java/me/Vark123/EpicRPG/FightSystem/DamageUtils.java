package me.Vark123.EpicRPG.FightSystem;

import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.commons.lang.mutable.MutableDouble;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.FightSystem.Events.CritCalculateEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public final class DamageUtils {

	private final static Random rand = new Random();
	
	private DamageUtils() {}
	
	public static double randomizeDamage(double dmg) {
		return randomizeDamage(dmg, 95, 105);
	}
	
	public static double randomizeDamage(double dmg, double min, double max) {
		dmg *= ((rand.nextDouble(max-min)+min))/100.;
		return dmg;
	}
	
	public static double randomizeDamage(double dmg, RpgStats stats) {
		double zrMod = stats.getFinalZrecznosc() / 50.;
		double min = 95 - zrMod;
		double max = 105 + zrMod;
		return randomizeDamage(dmg, min, max);
	}
	
	public static boolean checkCrit(RpgPlayer rpg) {
		CritCalculateEvent event = new CritCalculateEvent(rpg, 0);
		Bukkit.getPluginManager().callEvent(event);
		
		int chance = event.getChance();
		int los = rand.nextInt(500);
		
		return los < chance;
	}

	public static boolean tryDodge(Player p) {
		if(!PlayerManager.getInstance().playerExists(p))
			return false;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		double los = rand.nextDouble(100);
		double chance = (stats.getFinalZdolnosci()+stats.getFinalZrecznosc())/65.;
		if(chance > 25)
			chance = 25;
		if(los >= chance)
			return false;
		return true;
	}
	
	public static double getProjectileDamage(@Nonnull ItemStack bow) {
		MutableDouble dmg = new MutableDouble(0);
		
		if(!bow.hasItemMeta() || !bow.getItemMeta().hasLore())
			return dmg.doubleValue();
		
		bow.getItemMeta().getLore().parallelStream().filter(s -> {
			return s.contains("§4- §8Obrazenia: §7");
		}).anyMatch(s -> {
			double damage = Integer.parseInt(ChatColor.stripColor(s).split(": ")[1]);
			dmg.setValue(damage);
			return true;
		});
		
		return dmg.doubleValue();
	}
	
}
