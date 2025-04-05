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

public class TotemRuneTemplate {

	private TotemRuneTemplate() { }
	
	public static void castTotem(
			ACastableRune castableRune,
			Location startLoc,
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
			onStartEffect.playEffect(startLoc);
		
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
						onEndEffect.playEffect(startLoc);
					cancel();
					return;
				}
				--timer;
				
				if(onTickEffect != null)
					onTickEffect.playEffect(startLoc);
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
					startLoc.getWorld().getNearbyEntities(startLoc, radius, radius, radius, entity -> {
						if(entity.getLocation().distanceSquared(startLoc) > radius * radius)
							return false;
						
						if(!(entity instanceof LivingEntity))
							return false;
						
						LivingEntity le = (LivingEntity) entity;
						if(hitCondition != null)
							return hitCondition.check(player, le);
						return true;
					}).forEach(entity -> {
						onHitEffect.playEffect(entity.getLocation(), (LivingEntity) entity);
					});
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, hitInterval);
	}
	
}
