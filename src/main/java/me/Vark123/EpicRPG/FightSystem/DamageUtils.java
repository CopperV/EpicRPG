package me.Vark123.EpicRPG.FightSystem;

import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.commons.lang.mutable.MutableDouble;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgModifiers;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.RpgStats;

public class DamageUtils {

	private final static Random rand = new Random();
	
	private DamageUtils() {}
	
	public static double randomizeDamage(double dmg) {
		return randomizeDamage(dmg, 90, 110);
	}
	
	public static double randomizeDamage(double dmg, double min, double max) {
		dmg *= ((rand.nextDouble(max-min)+min))/100.;
		return dmg;
	}
	
	public static boolean checkDodge(Player p) {
		if(!PlayerManager.getInstance().playerExists(p))
			return false;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		double los = rand.nextDouble(100);
		double szansa = (stats.getFinalZdolnosci()+stats.getFinalZrecznosc())/65;
		if(szansa > 25)
			szansa = 25;
		if(los >= szansa)
			return false;
		Location loc = p.getLocation();
        p.getWorld().spawnParticle(Particle.CLOUD, loc, 15, 0.5, 1.5, 0.5, 0.1);
        p.playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 2, 0.1f);
		return true;
	}
	
	public static double conditionalDebuffDamage(Player damager, Entity victim, double damage) {
		
		if(damager.isInsideVehicle() && damager.getVehicle() instanceof AbstractHorse) {
			damager.sendMessage(Main.getInstance().getPrefix()+" §cZbyt ciezko walczy sie na koniu!");
			damager.sendMessage(Main.getInstance().getPrefix()+" §cNie moge zadac skutecznych obrazen!");
			damage /= 4.;
			damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*2, 2));
			damager.playSound(damager.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
		}
		
		Material m = damager.getLocation().getBlock().getType();
		Material m2 = damager.getLocation().getBlock().getType();
		if(m.equals(Material.WATER) || m.equals(Material.LAVA)) {
			if(!EpicRPGMobManager.getInstance().isWaterMob(victim.getName())) {
				damager.sendMessage(Main.getInstance().getPrefix()+" §cWoda stawia straszny opor w walce...");
				damager.sendMessage(Main.getInstance().getPrefix()+" §cNie moge tak walczyc!");
				damage /= 20.;
				damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*2, 2));
				damager.playSound(damager.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
			}
		}else if(m2.equals(Material.WATER) || m2.equals(Material.LAVA)) {
			if(!EpicRPGMobManager.getInstance().isWaterMob(victim.getName())) {
				damager.sendMessage(Main.getInstance().getPrefix()+" §cWoda stawia straszny opor w walce...");
				damager.sendMessage(Main.getInstance().getPrefix()+" §cNie moge tak walczyc!");
				damage /= 20.;
				damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*2, 2));
				damager.playSound(damager.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
			}
		}
		
		return damage;
	}
	
	public static boolean checkCrit(RpgPlayer rpg) {
		RpgModifiers modifiers = rpg.getModifiers();
		RpgStats stats = rpg.getStats();
		RpgPlayerInfo info = rpg.getInfo();
		int los = rand.nextInt(500);
		int chance = stats.getFinalWalka();
		if(info.getShortProf().toLowerCase().contains("mys")){
			chance += 50;
		}
		
		switch(modifiers.getPotionWalka()) {
			case 1:
				chance += 37;
				break;
			case 2:
				chance += 75;
				break;
			case 3:
				chance += 125;
				break;
		}
		
		return los < chance;
	}
	
	public static void showCritInfo(Player p) {
		Location loc = p.getLocation();
        p.getWorld().spawnParticle(Particle.FLAME, loc, 8, 0.5, 1.5, 0.5, 0.1);
        p.playSound(loc, Sound.ENTITY_PLAYER_ATTACK_CRIT, 2, 0.2f);
	}
	
	public static double getBowDamage(@Nonnull ItemStack it) {
		MutableDouble dmg = new MutableDouble(0);
		
		if(!it.hasItemMeta() || !it.getItemMeta().hasLore())
			return dmg.doubleValue();
		
		it.getItemMeta().getLore().parallelStream().filter(s -> {
			return s.contains("§4- §8Obrazenia: §7");
		}).anyMatch(s -> {
			double damage = Integer.parseInt(ChatColor.stripColor(s).split(": ")[1]);
			dmg.setValue(damage);
			return true;
		});
		
		return dmg.doubleValue();
	}
	
}
