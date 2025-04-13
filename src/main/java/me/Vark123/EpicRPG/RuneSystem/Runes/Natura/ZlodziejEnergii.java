package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class ZlodziejEnergii extends ACastableRune {
	
	private static final Random random = new Random();
	
	private IRuneHitCondition hitCondition1;
	private IRuneHitCondition hitCondition2;
	
	public ZlodziejEnergii(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition1 = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		hitCondition2 = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
	}

	@Override
	public void castSpell() {
		double baseHeal = rune.getDamage() * 0.1;
		
		Location startLoc = player.getLocation().clone().add(0, 0.05, 0);
		startLoc.getWorld().playSound(startLoc, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.1f, 0.9f);
		
		double radius = rune.getObszar();
		new BukkitRunnable() {
			double timer = rune.getDurationTime();
			double step = 4./20.;
			double points = rune.getObszar() * 10;
			double angleStep = Math.PI*2 / points;
			@Override
			public void run() {
				if(isCancelled())
					return;
				
				if(timer <= 0 || !casterInCastWorld()) {
					startLoc.getWorld().playSound(startLoc, Sound.BLOCK_BEACON_DEACTIVATE, 1.5f, 0.9f);
					
					cancel();
					return;
				}
				timer -= step;
				
				double randOffset = random.nextDouble(Math.PI*2);
				for(int i = 0; i < points; ++i){
					double angle = randOffset + i*angleStep;
					double x = radius * Math.sin(angle);
					double z = radius * Math.cos(angle);
					
					Location pos = startLoc.clone().add(x,0,z);
					pos.getWorld().spawnParticle(Particle.FALLING_LAVA, pos, 3,
							0.1, 0.05, 0.1, 0.02);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 4);
		
		new BukkitRunnable() {
			double timer = rune.getDurationTime();
			double step = 20./20.;
			@Override
			public void run() {
				if(isCancelled())
					return;
				
				if(timer <= 0 || !casterInCastWorld()) {
					cancel();
					return;
				}
				timer -= step;
				
				Collection<Entity> damageTargets = startLoc.getWorld().getNearbyEntities(startLoc, radius, radius, radius, entity -> {
					if(entity.getLocation().distanceSquared(startLoc) > radius * radius)
						return false;
					
					if(!(entity instanceof LivingEntity))
						return false;
					
					LivingEntity le = (LivingEntity) entity;
					return hitCondition1.check(player, le);
				});
				Collection<Entity> healTargets = startLoc.getWorld().getNearbyEntities(startLoc, radius, radius, radius, entity -> {
					if(entity.getLocation().distanceSquared(startLoc) > radius * radius)
						return false;
					
					if(!(entity instanceof LivingEntity))
						return false;
					
					LivingEntity le = (LivingEntity) entity;
					return hitCondition2.check(player, le);
				})
						.stream()
						.filter(entity -> entity instanceof Player)
						.collect(Collectors.toSet());
				
				if(damageTargets.size() < 1)
					return;
				
				damageTargets.forEach(entity -> {
					if(RuneUtils.damage(player, (LivingEntity) entity, rune)) {
						entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 0.8f, 1f);
						entity.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, entity.getLocation().clone().add(0,1,0), 9,
								0.3, 0.4, 0.3, 0.02);
					}
				});
				
				if(healTargets.size() < 1)
					return;
				
				double healPerPlayer = baseHeal / (double)damageTargets.size();
				healTargets.forEach(entity -> {
					Player player = (Player) entity;
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
					
					RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, healPerPlayer);
					Bukkit.getPluginManager().callEvent(event);
					if(event.isCancelled())
						return;
					
					entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_CAT_HISS, 0.8f, 1.2f);
					entity.getWorld().spawnParticle(Particle.HEART, entity.getLocation().clone().add(0,1,0), 9,
							0.4, 0.8, 0.4, 0.02);
				});
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

}
