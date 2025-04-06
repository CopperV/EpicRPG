package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneDirectionGetter;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLivingEntityEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;

public class RainRuneTemplate {

	private static final Random rand = new Random();
	
	private RainRuneTemplate() { }
	
	public static void castRain(
			ACastableRune castableRune,
			Location startLoc,
			double radius,
			double duration,
			double velocity,
			int effectTickSteps,
			int effectTickInterval,
			int projectileTickSteps,
			int projectileTickInterval,
			IRuneDirectionGetter dropDirection,
			double dropHeight,
			double maxDropDistance,
			BoundingBox boundingBox,
			IRuneLocationEffect onStartEffect,
			IRuneLocationEffect onTickEffect,
			IRuneHitCondition hitCondition,
			IRuneLivingEntityEffect onHitEffect,
			IRuneLocationEffect onRainEndEffect,
			IRuneLocationEffect onProjectileStartEffect,
			IRuneLocationEffect onProjectileEndEffect) {
		
		if(onStartEffect != null)
			onStartEffect.playEffect(startLoc);
		
		new BukkitRunnable() {
			int timer = (int) (duration * (20./(double)effectTickInterval));
			@Override
			public void run() {
				if(isCancelled())
					return;
				
				if(!castableRune.casterInCastWorld()) {
					cancel();
					return;
				}
				
				if(timer <= 0) {
					if(onRainEndEffect != null)
						onRainEndEffect.playEffect(startLoc);
					cancel();
					return;
				}
				--timer;
				
				for(int i = 0; i < effectTickSteps; ++i) {
					double angle = rand.nextDouble(Math.PI * 2);
					double r = rand.nextDouble(radius);
					double x = Math.sin(angle) * r;
					double y = dropHeight;
					double z = Math.cos(angle) * r;
					
					Location loc = startLoc.clone().add(x, y, z);
					ProjectileRuneTemplate.castProjectile(
							castableRune, 
							loc, 
							dropDirection.getVector(startLoc, loc),
							velocity, 
							projectileTickSteps, 
							projectileTickInterval, 
							maxDropDistance, 
							boundingBox, 
							onProjectileStartEffect,
							onTickEffect,
							hitCondition,
							onHitEffect,
							onProjectileEndEffect);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, effectTickInterval);
	}
	
}
