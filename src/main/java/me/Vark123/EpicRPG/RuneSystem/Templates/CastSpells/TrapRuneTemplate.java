package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLivingEntityEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;

public class TrapRuneTemplate {

	private TrapRuneTemplate() { }
	
	public static void castTrap(
			ACastableRune castableRune,
			Location loc,
			int duration,
			int tickInterval,
			double radius,
			IRuneLocationEffect onStartEffect,
			IRuneLocationEffect onTickEffect,
			IRuneHitCondition hitCondition,
			IRuneLivingEntityEffect onHitEffect,
			IRuneLocationEffect onEndEffect) {
		
		if(onStartEffect != null)
			onStartEffect.playEffect(loc);
		
		new BukkitRunnable() {
			Player player = castableRune.getPlayer();
			double timer = duration;
			double tickStep = tickInterval / duration;
			@Override
			public void run() {
				if(isCancelled())
					return;
				
				if(!castableRune.casterInCastWorld()) {
					cancel();
					return;
				}
					
				if(timer <= 0) {
					if(onEndEffect != null)
						onEndEffect.playEffect(loc);
					cancel();
					return;
				}
				timer -= tickStep;
				
				if(onTickEffect != null)
					onTickEffect.playEffect(loc);
				
				if(onHitEffect != null) {
					loc.getWorld().getNearbyEntities(loc, radius, radius, radius, entity -> {
						if(entity.getLocation().distanceSquared(loc) > radius * radius)
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
						onHitEffect.playEffect(loc, e);
						cancel();
					});
				}
					
			}
		}.runTaskTimer(Main.getInstance(), 0, tickInterval);
		
	}
	
}
