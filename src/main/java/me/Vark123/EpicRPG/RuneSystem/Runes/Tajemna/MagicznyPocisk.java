package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class MagicznyPocisk extends ACastableRune {

	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private DustOptions dust = new DustOptions(Color.PURPLE, 1);
	
	public MagicznyPocisk(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.6, -0.6, -0.6, 
				0.6, 0.6, 0.6);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		player.getWorld().playSound(startLoc, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 0.8f);
		
		new BukkitRunnable() {
			Location loc = startLoc.clone();
			Location projectileLoc = loc.clone();
			Vector projectileDirection = loc.getDirection().clone().normalize().multiply(0.5);
			Vector up = new Vector(0, 1, 0);
			Vector right = projectileDirection.clone().normalize().crossProduct(up).normalize();
			double theta = rand.nextDouble(Math.PI*2);
			double thetaStep = Math.PI * 2 / 24;
			double amplitude = 2;
			double maxDistance = 30;
			@Override
			public void run() {
				int index = 0;
				do {
					if(isCancelled())
						return;
					
					if(!casterInCastWorld()) {
						cancel();
						return;
					}
					
					if(projectileLoc.getBlock().getType().isSolid() && !projectileLoc.getBlock().isLiquid()) {
						cancel();
						return;
					}
					if(loc.distanceSquared(startLoc) >= maxDistance*maxDistance) {
						cancel();
						return;
					}
					
					projectileLoc.getWorld().spawnParticle(Particle.DUST, projectileLoc, 9,
							0.15, 0.15, 0.15, 0.1, dust);
					
					BoundingBox localBoundigBox = BoundingBox.of(
							projectileLoc,
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
						if(RuneUtils.damage(player, e, rune))
							projectileLoc.getWorld().playSound(projectileLoc, Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1.5f);
						cancel();
					});
					
					double offset = amplitude * Math.sin(theta);
					theta += thetaStep;
					
					loc.add(projectileDirection);
					projectileLoc = loc.clone().add(right.clone().multiply(offset));
					
					++index;
				} while(index < 2);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
