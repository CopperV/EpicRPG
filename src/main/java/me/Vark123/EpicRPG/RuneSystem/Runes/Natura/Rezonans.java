package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Rezonans extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;

	public Rezonans(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.5, -0.5, -0.5, 
				0.5, 0.5, 0.5);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0,1,0);
		startLoc.getWorld().playSound(startLoc, Sound.ENTITY_EVOKER_CAST_SPELL, 1, 1.2f);
		
		spellEffect(startLoc, 0, Particle.FIREWORK);
		spellEffect(startLoc, Math.PI/2, Particle.FLAME);
		spellEffect(startLoc, Math.PI, Particle.TOTEM_OF_UNDYING);
		spellEffect(startLoc, (3*Math.PI)/2, Particle.FALLING_WATER);
	}
	
	private void spellEffect(Location startLoc, double _angle, Particle particle) {
		
		new BukkitRunnable() {
			Location loc = startLoc.clone();
			double distance = 8;
			double radius = 0.5;
			double radiusStep = 0.05;
			double angle = _angle;
			double angleStep = (Math.PI) / 16;
			@Override
			public void run() {
				if (isCancelled())
					return;

				if (!casterInCastWorld()) {
					cancel();
					return;
				}

				if (loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					cancel();
					return;
				}
				if (loc.distanceSquared(startLoc) >= distance * distance) {
					cancel();
					return;
				}

				loc.getWorld().spawnParticle(particle, loc, 6, 0.15, 0.15, 0.15, 0.03);

				BoundingBox localBoundigBox = BoundingBox.of(loc, boundingBox.getWidthX() * 0.5,
						boundingBox.getHeight() * 0.5, boundingBox.getWidthZ() * 0.5);
				loc.getWorld().getNearbyEntities(localBoundigBox, entity -> {
					if (!(entity instanceof LivingEntity))
						return false;

					LivingEntity le = (LivingEntity) entity;
					return hitCondition.check(player, le);
				}).stream().min((e1, e2) -> {
					double dist1 = e1.getLocation().distanceSquared(loc);
					double dist2 = e2.getLocation().distanceSquared(loc);
					if (dist1 == dist2)
						return 0;
					return dist1 < dist2 ? -1 : 1;
				}).map(e -> (LivingEntity) e).ifPresent(entity -> {
					if (RuneUtils.damage(player, entity, rune)) {
						entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 0.4f);
						entity.getWorld().spawnParticle(particle, entity.getLocation().clone().add(0,1,0), 14,
								0.35, 0.35, 0.35, 0.05);
					}
					cancel();
				});

				radius += radiusStep;
				angle += angleStep;
				
				double x = radius * Math.sin(angle);
				double z = radius * Math.cos(angle);
				
				loc = startLoc.clone().add(x, 0, z);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
