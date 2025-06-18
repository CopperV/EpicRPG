package me.Vark123.EpicRPG.FightSystem;

import java.util.Random;

import org.apache.commons.lang.mutable.MutableDouble;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.Events.EpicCritCalculateEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageRandomizeEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDodgeCalculateEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicMegaCritCalculateEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPierceCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;
import me.Vark123.EpicRPG.Utils.Utils;

public final class DamageUtils {

	private static final Random rand = new Random();
	
	private DamageUtils() { }
	
	public static boolean checkCrit(RpgPlayer rpgPlayer, Entity victim) {
		EpicCritCalculateEvent event = new EpicCritCalculateEvent(rpgPlayer);
		Bukkit.getPluginManager().callEvent(event);
		
		double max = victim != null && victim instanceof Player ?
				5 : 1;
		
		double chance = event.getChance();
		double los = rand.nextDouble(max);
		
		return los < chance;
	}
	
	public static boolean checkMegaCrit(RpgPlayer rpgPlayer, Entity victim) {
		EpicMegaCritCalculateEvent event = new EpicMegaCritCalculateEvent(rpgPlayer);
		Bukkit.getPluginManager().callEvent(event);
		
		double max = 1;
		
		double chance = event.getChance();
		double los = rand.nextDouble(max);
		
		return los < chance;
	}
	
	public static boolean tryDodge(RpgPlayer rpgPlayer) {
		EpicDodgeCalculateEvent event = new EpicDodgeCalculateEvent(rpgPlayer);
		Bukkit.getPluginManager().callEvent(event);
		
		double max = 0.3;
		double chance = Math.min(event.getChance(), max);
		
		if(rpgPlayer.getInfo().getProffesion().equals("§2Mysliwy")) {
			if(rpgPlayer.getModifiers().hasActiveModifier(EpicModifierTypes.TAJEMNY_BLASK))
				chance += 0.2;
			if(rpgPlayer.getModifiers().hasActiveModifier(EpicModifierTypes.TAJEMNY_BLASK_M))
				chance += 0.35;
		}
		
		double los = rand.nextDouble();
		return los < chance;
	}
	
	public static double randomizeDamage(RpgPlayer rpgPlayer, double damage) {
		RpgStats stats = rpgPlayer.getStats();
		
		double mod = stats.getFinalZrecznosc() / 35.;
		mod *= 0.01;
		EpicDamageRandomizeEvent event = new EpicDamageRandomizeEvent(rpgPlayer, mod);
		Bukkit.getPluginManager().callEvent(event);
		
		mod = Math.min(event.getModifier(), 0.6);
		double min = 0.95 - mod;
		double max = 1.05 + mod;
		
		damage *= ((rand.nextDouble(max-min)+min));
		return damage;
	}
	
	@Deprecated
	public static double randomizeEntityHpDamage(double damage, RpgPlayer rpgPlayer, LivingEntity victim) {
		double zrFactor = Math.min(rpgPlayer.getStats().getFinalZrecznosc() * (0.01*0.02), 0.3);
		double minFactor = 1 - zrFactor;
		double maxFactor = 1 + zrFactor;
		double hpPercent = Utils.limitValue(0, 1, victim.getHealth() / victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		double percent = Utils.scaleValue(0, 1, maxFactor, minFactor, hpPercent);
		return damage*percent;
	}
	
	public static boolean checkArmorPierce(RpgPlayer damager, LivingEntity victim, double armor) {
		EpicPierceCalculateEvent event = new EpicPierceCalculateEvent(damager);
		Bukkit.getPluginManager().callEvent(event);
		
		double maxChance = 0.6;
		
		double chance = Utils.limitValue(0, maxChance, event.getChance());
		double random = rand.nextDouble();
		return random < chance;
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
	
	public static void applyTimingDirectDamageEffect(LivingEntity damager, LivingEntity victim, double damage,
			DamageType type, DamageCause cause, IRuneLocationEffect onTickEffect, int delay, int interval, int duration) {
		new BukkitRunnable() {
			double timer = duration;
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(timer <= 0 || victim.isDead()) {
					cancel();
					return;
				}
				timer -= interval;
				
				if(onTickEffect != null)
					onTickEffect.playEffect(victim.getLocation().clone());
				
				if(!applyDirectDamageEffect(damager, victim, damage, type, cause)) {
					cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), delay, interval);
	}
	
	public static boolean applyDirectDamageEffect(LivingEntity damager, LivingEntity victim, double damage,
			DamageType type, DamageCause cause) {
		Utils.neutralizeEntityNoDamageTicks(damager, victim);
		
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(
				damager, 
				victim,
				cause,
				DamageSource
					.builder(type)
					.withCausingEntity(damager)
					.withDirectEntity(damager)
					.build(),
				damage);
		
		Bukkit.getPluginManager().callEvent(event);
		if(!ManualDamage.tryDoDamage(damager, victim, event.getFinalDamage(), event))
			return false;
		
		return true;
	}
	
	public static boolean applyDirectDamageEffect(LivingEntity victim, double damage,
			DamageType type, DamageCause cause) {
		EntityDamageEvent event = new EntityDamageEvent(
				victim,
				cause,
				DamageSource
					.builder(type)
					.build(),
				damage);
		
		Bukkit.getPluginManager().callEvent(event);
		if(!ManualDamage.tryDoDamage(victim, event.getFinalDamage(), event))
			return false;
		
		return true;
	}
	
}
