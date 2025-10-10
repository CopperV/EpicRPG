package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Splugawienie extends ACastableRune {
	
	private static final Random rand = new Random();

	private IRuneHitCondition hitCondition;
	
	public Splugawienie(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location loc = player.getLocation().clone().add(0,0.1,0);
		
		loc.getWorld().playSound(loc, Sound.ENTITY_MAGMA_CUBE_JUMP, 1.5f, 0.65f);
		
		new BukkitRunnable() {
			Map<UUID, Double> mobModifiers = new ConcurrentHashMap<>();
			
			int timer = rune.getDurationTime()*4;
			
			MutableDouble radius = new MutableDouble(rune.getObszar());
			double maxRadius = rune.getObszar() * 10;
			
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(timer <= 0 || !casterInCastWorld()) {
					cancel();
					return;
				}
				--timer;

				double r = getRadius(radius.doubleValue(), maxRadius);
				loc.getWorld()
						.getNearbyEntities(loc, r, r, r, entity -> {
							if(entity.getLocation().distanceSquared(loc) > r * r)
								return false;
							
							if(!(entity instanceof LivingEntity))
								return false;
							
							LivingEntity le = (LivingEntity) entity;
							return hitCondition.check(player, le);
						})
						.stream()
						.map(entity -> (LivingEntity) entity)
						.forEach(entity -> {
							UUID uid = entity.getUniqueId();
							double modifier = mobModifiers.getOrDefault(uid, 1.);
							if(RuneUtils.damage(player, entity, rune, rune.getDamage() * modifier)) {
								Location eLoc = entity.getLocation().clone().add(0,1,0);
								
								eLoc.getWorld().playSound(eLoc, Sound.ENTITY_EVOKER_CAST_SPELL, 0.9f, 1.6f);
								eLoc.getWorld().spawnParticle(Particle.SMOKE, eLoc, 8,
										0.4, 0.8, 0.4, 0.02);
								
								radius.add(0.5);
								mobModifiers.put(uid, modifier + 0.025);
							}
						});
				
				int outerPoints = (int) (3 * r);
				int innerPoints = (int) (4 * r * Math.log(r));

				for(int i = 0; i <= outerPoints; ++i) {
					double angle = rand.nextDouble(2*Math.PI);
					
					double x = r * Math.cos(angle);
					double z = r * Math.sin(angle);
					
					Location tmpLoc = loc.clone().add(x,0,z);
					loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, tmpLoc, 1, .05, .05, .05, .02);
				}
				
				for(int i = 0; i <= innerPoints; ++i) {
					double radius = rand.nextDouble(r);
					double angle = rand.nextDouble(2*Math.PI);
					
					double x = radius * Math.cos(angle);
					double z = radius * Math.sin(angle);
					
					Location tmpLoc = loc.clone().add(x,0,z);
					loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, tmpLoc, 1, .05, .05, .05, .02);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
	}
	
	private double getRadius(double radius, double maxRadius) {
		return Math.min(radius, maxRadius);
	}

}
