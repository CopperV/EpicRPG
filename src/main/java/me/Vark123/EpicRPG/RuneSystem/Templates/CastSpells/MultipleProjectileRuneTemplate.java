package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLivingEntityEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;

public class MultipleProjectileRuneTemplate {

	private MultipleProjectileRuneTemplate() { }
	
	public static void castProjectile(
			ACastableRune castableRune,
			Location startLoc,
			Vector direction,
			double velocity,
			int steps,
			int interval,
			double maxDistance,
			int maxHits,
			BoundingBox boundingBox,
			IRuneLocationEffect onStartEffect,
			IRuneLocationEffect onTickEffect,
			IRuneHitCondition hitCondition,
			IRuneLivingEntityEffect onHitEffect,
			IRuneLocationEffect onEndEffect) {
		
		if(onStartEffect != null)
			onStartEffect.playEffect(startLoc);
		
		new BukkitRunnable() {
			Player player = castableRune.getPlayer();
			Location loc = startLoc.clone();
			Vector projectileDirection = direction.clone().normalize().multiply(velocity);
			Collection<Entity> hitted = new HashSet<>();
			@Override
			public void run() {
				int index = 0;
				do {
					if(isCancelled())
						return;
					
					if(!castableRune.casterInCastWorld()) {
						cancel();
						return;
					}
					
					if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
						if(onEndEffect != null)
							onEndEffect.playEffect(loc);
						cancel();
						return;
					}
					if(loc.distanceSquared(startLoc) >= maxDistance*maxDistance) {
						if(onEndEffect != null)
							onEndEffect.playEffect(loc);
						cancel();
						return;
					}
					
					if(onTickEffect != null)
						onTickEffect.playEffect(loc);
					
					BoundingBox localBoundigBox = BoundingBox.of(
							loc,
							boundingBox.getWidthX() * 0.5, 
							boundingBox.getHeight() * 0.5,
							boundingBox.getWidthZ() * 0.5);
					loc.getWorld().getNearbyEntities(localBoundigBox, entity -> {
						if(hitted.contains(entity))
							return false;
						
						if(!(entity instanceof LivingEntity))
							return false;
						
						LivingEntity le = (LivingEntity) entity;
						if(hitCondition != null)
							return hitCondition.check(player, le);
						return true;
					}).stream().min((e1, e2) -> {
						double dist1 = e1.getLocation().distanceSquared(loc);
						double dist2 = e2.getLocation().distanceSquared(loc);
						if(dist1 == dist2)
							return 0;
						return dist1 < dist2 ? -1 : 1;
					}).map(e -> (LivingEntity) e)
					.ifPresent(e -> {
						if(onHitEffect != null)
							onHitEffect.playEffect(loc, e);
						
						hitted.add(e);
						if(maxHits > 0 && hitted.size() >= maxDistance)
							cancel();
					});
					
					loc.add(projectileDirection);
					++index;
				} while(index < steps);
			}
		}.runTaskTimer(Main.getInstance(), 0, interval);
		
	}
	
}
