package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import org.bukkit.Location;
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

public class ProjectileRuneTemplate {

	private ProjectileRuneTemplate() { }
	
	public static void castProjectile(
			ACastableRune castableRune,
			Location startLoc,
			Vector direction,
			double velocity,
			int steps,
			int interval,
			double maxDistance,
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
						cancel();
					});
					
					loc.add(projectileDirection);
					++index;
				} while(index < steps);
			}
		}.runTaskTimer(Main.getInstance(), 0, interval);
		
	}
	
}
