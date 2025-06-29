package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLivingEntityEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;

public class MultiTotemRuneTemplate {

	private MultiTotemRuneTemplate() { }
	
	public static void castTotems(
			ACastableRune castableRune,
			Collection<Location> locations,
			int duration,
			double radius,
			int tickInterval,
			int hitInterval,
			IRuneLocationEffect onStartEffect,
			IRuneLocationEffect onTickEffect,
			IRuneHitCondition hitCondition,
			IRuneLivingEntityEffect onHitEffect,
			IRuneLocationEffect onEndEffect) {
		
		if(onStartEffect != null)
			locations.forEach(onStartEffect::playEffect);
		
		new BukkitRunnable() {
			int timer = (int) (duration * (20./(double)tickInterval));
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
						locations.forEach(onEndEffect::playEffect);
					cancel();
					return;
				}
				--timer;
				
				if(onTickEffect != null)
					locations.forEach(onTickEffect::playEffect);
			}
		}.runTaskTimer(Main.getInstance(), 0, tickInterval);
		
		new BukkitRunnable() {
			Player player = castableRune.getPlayer();
			int timer = (int) (duration * (20./(double)hitInterval));
			@Override
			public void run() {
				if(isCancelled())
					return;
				
				if(!castableRune.casterInCastWorld() || timer <= 0) {
					cancel();
					return;
				}
				--timer;
				
				if(onHitEffect != null) {
					Collection<LivingEntity> hitted = new HashSet<>();
					locations.forEach(loc -> {
						loc.getWorld().getNearbyEntities(loc, radius, radius, radius, entity -> {
							if(entity.getLocation().distanceSquared(loc) > radius * radius)
								return false;
							
							if(!(entity instanceof LivingEntity le))
								return false;
							
							if(hitted.contains(le))
								return false;
							
							if(hitCondition != null)
								return hitCondition.check(player, le);
							return true;
						}).forEach(entity -> hitted.add((LivingEntity) entity));
					});
					hitted.forEach(entity -> {
						onHitEffect.playEffect(entity.getLocation(), (LivingEntity) entity);
					});
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, hitInterval);
	}
	
}
