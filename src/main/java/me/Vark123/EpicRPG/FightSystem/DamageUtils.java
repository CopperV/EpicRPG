package me.Vark123.EpicRPG.FightSystem;

import java.util.Random;

import org.apache.commons.lang.mutable.MutableDouble;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Config;
import me.Vark123.EpicRPG.FightSystem.Events.EpicCritCalculateEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageRandomizeEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Utils.Utils;

public final class DamageUtils {

	private static final Random rand = new Random();
	
	private DamageUtils() { }
	
	public static boolean checkCrit(RpgPlayer rpgPlayer, Entity victim) {
		EpicCritCalculateEvent event = new EpicCritCalculateEvent(rpgPlayer, victim);
		Bukkit.getPluginManager().callEvent(event);
		
		int max = Config.get().getMaxWalkaCrit();
		max = victim != null && victim instanceof Player ?
				5 * max : max;
		
		int chance = event.getChance();
		int los = rand.nextInt(max);
		
		return los < chance;
	}
	
	public static double randomizeDamage(RpgPlayer rpgPlayer, double damage) {
		RpgStats stats = rpgPlayer.getStats();
		
		double mod = stats.getFinalZrecznosc() / 35.;
		mod *= 0.01;
		EpicDamageRandomizeEvent event = new EpicDamageRandomizeEvent(rpgPlayer, mod);
		Bukkit.getPluginManager().callEvent(event);
		
		mod = event.getModifier();
		double min = 0.95 - mod;
		double max = 1.05 + mod;
		
		damage *= ((rand.nextDouble(max-min)+min));
		return damage;
	}
	
	public static double randomizeEntityHpDamage(double damage, RpgPlayer rpgPlayer, LivingEntity victim) {
		double zrFactor = Math.min(rpgPlayer.getStats().getFinalZrecznosc() * (0.01*0.02), 0.3);
		double minFactor = 1 - zrFactor;
		double maxFactor = 1 + zrFactor;
		double hpPercent = Utils.limitValue(0, 1, victim.getHealth() / victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		double percent = Utils.scaleValue(0, 1, maxFactor, minFactor, hpPercent);
		return damage*percent;
	}
	
	public static double getProjectileDamage(Entity shooter, ItemStack bow) {
		if(MythicBukkit.inst().getMobManager().isMythicMob(shooter)) {
			return MythicBukkit.inst().getMobManager().getMythicMobInstance(shooter).getDamage();
		}
		
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
