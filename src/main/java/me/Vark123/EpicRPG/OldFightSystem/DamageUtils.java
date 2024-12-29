package me.Vark123.EpicRPG.OldFightSystem;

import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.commons.lang.mutable.MutableDouble;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.OldFightSystem.Events.CritCalculateEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Utils.Utils;

@Deprecated
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
	
	public static double randomizeEntityHpDamage(double dmg, RpgPlayer rpg, LivingEntity victim) {
		double zrFactor = Math.min(rpg.getStats().getFinalZrecznosc() * (0.01*0.02), 0.3);
		double minFactor = 1 - zrFactor;
		double maxFactor = 1 + zrFactor;
		double hpPercent = Utils.limitValue(0, 1, victim.getHealth() / victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		double percent = Utils.scaleValue(0, 1, maxFactor, minFactor, hpPercent);
		return dmg*percent;
	}
	
	public static double randomizeDamage(double dmg, RpgPlayer rpg) {
		RpgStats stats = rpg.getStats();
		RpgModifiers modifiers = rpg.getModifiers();
		
		double mod = stats.getFinalZrecznosc() / 35.;
		if(modifiers.hasSzalPrzedwiecznych())
			mod += 15;
		if(modifiers.hasSzalPrzedwiecznych_h())
			mod += 23;
		if(modifiers.hasSzalPrzedwiecznych_m())
			mod += 30;
		
		double min = 95 - mod;
		double max = 105 + mod;
		return randomizeDamage(dmg, min, max);
	}
	
	public static double getMegaCritModifier(RpgPlayer rpg) {
		double modifier = 0;
		
		Random rand = new Random();
		double chance = 0;
		
		RpgPlayerInfo info = rpg.getInfo();
		int wiecznyWedrowiecAmount = info.getSetCounts().getOrDefault("Wieczny_Wedrowiec", 0);
		int wiecznyWedrowiec_HAmount = info.getSetCounts().getOrDefault("Wieczny_Wedrowiec_H", 0);
		int wiecznyWedrowiec_MAmount = info.getSetCounts().getOrDefault("Wieczny_Wedrowiec_M", 0);
		
		if(wiecznyWedrowiecAmount >= 3)
			chance += 0.02;
		if(wiecznyWedrowiec_HAmount >= 3)
			chance += 0.025;
		if(wiecznyWedrowiec_MAmount >= 3)
			chance += 0.03;

		ItemStack weapon = rpg.getPlayer().getInventory().getItemInMainHand();
		if(weapon != null && !weapon.getType().equals(Material.AIR)) {
			NBTItem nbt = new NBTItem(weapon);
			switch(nbt.getString("MYTHIC_TYPE")) {
				case "Raid_1_Unikat":
					switch(wiecznyWedrowiecAmount) {
						case 4:
						case 3:
							chance += 0.003;
						case 2:
							chance += 0.003;
						case 1:
							chance += 0.003;
					}
					switch(wiecznyWedrowiec_HAmount) {
						case 4:
						case 3:
							chance += 0.005;
						case 2:
							chance += 0.005;
						case 1:
							chance += 0.005;
					}
					switch(wiecznyWedrowiec_MAmount) {
						case 4:
						case 3:
							chance += 0.0075;
						case 2:
							chance += 0.0075;
						case 1:
							chance += 0.0075;
					}
					break;
				case "Raid_1_M_Unikat":
					switch(wiecznyWedrowiecAmount) {
						case 4:
						case 3:
							chance += 0.005;
						case 2:
							chance += 0.005;
						case 1:
							chance += 0.005;
					}
					switch(wiecznyWedrowiec_HAmount) {
						case 4:
						case 3:
							chance += 0.007;
						case 2:
							chance += 0.007;
						case 1:
							chance += 0.007;
					}
					switch(wiecznyWedrowiec_MAmount) {
						case 4:
						case 3:
							chance += 0.01;
						case 2:
							chance += 0.01;
						case 1:
							chance += 0.01;
					}
					break;
			}
		}
		
		if(rand.nextDouble() > chance)
			return modifier;
		
		modifier = rpg.getStats().getFinalWalka() / 250.;
		return modifier;
	}
	
	@Deprecated
	public static boolean checkCrit(RpgPlayer rpg) {
		CritCalculateEvent event = new CritCalculateEvent(rpg, 0);
		Bukkit.getPluginManager().callEvent(event);
		
		int chance = event.getChance();
		int los = rand.nextInt(500);
		
		return los < chance;
	}
	
	public static boolean checkCrit(RpgPlayer rpg, Entity victim) {
		CritCalculateEvent event = new CritCalculateEvent(rpg, 0);
		Bukkit.getPluginManager().callEvent(event);
		
		int max = victim instanceof Player ? 2500 : 500;
		
		int chance = event.getChance();
		int los = rand.nextInt(max);
		
		return los < chance;
	}

	public static boolean tryDodge(Player p) {
		if(!PlayerManager.getInstance().playerExists(p))
			return false;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		double los = rand.nextDouble(100);
		double chance = (stats.getFinalZdolnosciMysliwskie()+stats.getFinalZrecznosc())/65.;
		
		if(rpg.getModifiers().hasWtopienie())
			chance += 8;
		if(rpg.getModifiers().hasWtopienie_h())
			chance += 10;
		if(rpg.getModifiers().hasWtopienie_m())
			chance += 12.5;
		
		if(chance > 30)
			chance = 30;

		if(rpg.getModifiers().hasTajemnyBlask_m() && rpg.getInfo().getProffesion().equals("§2Mysliwy")) {
			chance = 50;
		} else if(rpg.getModifiers().hasTajemnyBlask() && rpg.getInfo().getProffesion().equals("§2Mysliwy")) {
			chance = 30;
		}
		
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
