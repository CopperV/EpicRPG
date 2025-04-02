package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLivingEntityEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;

public class WaveRuneTemplate {
	
	private WaveRuneTemplate() { }
	
	public static void castWave(
			ACastableRune castableRune,
			Location startLoc,
			double radius,
			double pointsPerRadius,
			double velocity,
			int steps,
			int interval,
			IRuneLocationEffect onStartEffect,
			IRuneLocationEffect onTickEffect,
			IRuneHitCondition hitCondition,
			IRuneLivingEntityEffect onHitEffect,
			IRuneLocationEffect onEndEffect) {

		if(onStartEffect != null)
			onStartEffect.playEffect(startLoc);
		
		new BukkitRunnable() {
			Player player = castableRune.getPlayer();
			Collection<Entity> hitted = new HashSet<>();
			double r = 0;
			@Override
			public void run() {
				if(isCancelled())
					return;
				
				for(int i = 0; i < steps; ++i) {
					if(!castableRune.casterInCastWorld()) {
						cancel();
						return;
					}
					if(r >= radius) {
						if(onEndEffect != null)
							onEndEffect.playEffect(startLoc);
						
						cancel();
						return;
					}

					if(onTickEffect != null) {
						double _points = Math.max(r * pointsPerRadius, 1);
						double angleStep = (Math.PI * 2) / _points;
						for(int j = 0; j < _points; ++j) {
							double angle = angleStep * j;
							double x = r * Math.sin(angle);
							double z = r * Math.cos(angle);
							
							Location tickLoc = startLoc.clone().add(x,0,z);
							onTickEffect.playEffect(tickLoc);
						}
					}
					
					if(onHitEffect != null) {
						startLoc.getWorld().getNearbyEntities(startLoc, r, r, r, entity -> {
							if(hitted.contains(entity))
								return false;
							
							if(entity.getLocation().distanceSquared(startLoc) > r*r)
								return false;
							
							if(!(entity instanceof LivingEntity))
								return false;
							
							LivingEntity le = (LivingEntity) entity;
							if(hitCondition != null)
								return hitCondition.check(player, le);
							
							return true;
						}).forEach(entity -> {
							hitted.add(entity);
							
							onHitEffect.playEffect(entity.getLocation(), (LivingEntity) entity);
						});
					}
					
					r += velocity;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, interval);
	}
	
}
