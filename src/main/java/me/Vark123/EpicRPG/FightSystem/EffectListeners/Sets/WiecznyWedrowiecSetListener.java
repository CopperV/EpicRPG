package me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.FightSystem.Calculators.IDamageCalculator.DamageCalculatorResult;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicMegaCritCalculateEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class WiecznyWedrowiecSetListener implements Listener {

	private static final Random rand = new Random();
	
	private static final PotionEffect normalSlowEffect = new PotionEffect(PotionEffectType.SLOWNESS, 20*4, 0);
	private static final PotionEffect heroicSlowEffect = new PotionEffect(PotionEffectType.SLOWNESS, 20*5, 0);
	private static final PotionEffect mythicSlowEffect = new PotionEffect(PotionEffectType.SLOWNESS, 20*6, 1);

	@EventHandler(priority = EventPriority.MONITOR)
	public void slowDonwEnemyEffect(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		LivingEntity victim = (LivingEntity) e.getVictim();
		
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec", 0) >= 2 &&
				rand.nextDouble() < 0.02) {
			Location loc = victim.getLocation().clone().add(0,1,0);
			loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 0.8f, 1.3f);
			loc.getWorld().spawnParticle(Particle.ENTITY_EFFECT, loc, 20, 
					0.6, 0.6, 0.6, rand.nextDouble(0.2, 0.6), Color.fromRGB(32, 32, 32));
			
			victim.addPotionEffect(normalSlowEffect);
		}
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec_H", 0) >= 2 &&
				rand.nextDouble() < 0.03) {
			Location loc = victim.getLocation().clone().add(0,1,0);
			loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 0.8f, 1.3f);
			loc.getWorld().spawnParticle(Particle.ENTITY_EFFECT, loc, 20, 
					0.6, 0.6, 0.6, rand.nextDouble(0.2, 0.6), Color.fromRGB(32, 32, 32));
			
			victim.addPotionEffect(heroicSlowEffect);
		}
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec_M", 0) >= 2 &&
				rand.nextDouble() < 0.035) {
			Location loc = victim.getLocation().clone().add(0,1,0);
			loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 0.8f, 1.3f);
			loc.getWorld().spawnParticle(Particle.ENTITY_EFFECT, loc, 20, 
					0.6, 0.6, 0.6, rand.nextDouble(0.2, 0.6), Color.fromRGB(32, 32, 32));
			
			victim.addPotionEffect(mythicSlowEffect);
		}

	}
	
	@EventHandler
	public void onCritIncrease(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;
		
		if(e.getArgs() == null || e.getArgs().length < 1 
				|| !(e.getArgs()[0] instanceof DamageCalculatorResult))
			return;
		if(!((DamageCalculatorResult) e.getArgs()[0]).isCrit)
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec", 0) >= 4) {
			e.increaseModifier(0.1);
		}
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec_H", 0) >= 4) {
			e.increaseModifier(0.17);
		}
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec_M", 0) >= 4) {
			e.increaseModifier(0.25);
		}
	}
	
	@EventHandler
	public void onCalc(EpicMegaCritCalculateEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec", 0) >= 3) {
			e.addChance(0.02);
		}
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec_H", 0) >= 3) {
			e.addChance(0.025);
		}
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec_M", 0) >= 3) {
			e.addChance(0.03);;
		}
	}
	
}
