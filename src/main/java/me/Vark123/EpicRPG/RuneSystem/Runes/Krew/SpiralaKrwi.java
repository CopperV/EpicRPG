package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
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

public class SpiralaKrwi extends ACastableRune {
	
	private static final DustOptions dust = new DustOptions(Color.fromRGB(138, 3, 3), 1.3f);

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;

	public SpiralaKrwi(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.4, -0.4, -0.4, 
				0.4, 0.4, 0.4);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0,1,0);
		startLoc.getWorld().playSound(startLoc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 0.8f);
		
		spellEffect(startLoc, 0);
		spellEffect(startLoc, Math.PI/3.);
		spellEffect(startLoc, 2*Math.PI/3.);
		spellEffect(startLoc, 2*Math.PI);
		spellEffect(startLoc, 4*Math.PI/3.);
		spellEffect(startLoc, 5*Math.PI/3.);
	}
	
	private void spellEffect(Location startLoc, double _angle) {
		
		new BukkitRunnable() {
			Location loc = startLoc.clone();
			double distance = 10;
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

				loc.getWorld().spawnParticle(Particle.DUST, loc, 4, 0.15, 0.15, 0.15, 0.15, dust);
				loc.getWorld().spawnParticle(Particle.ENTITY_EFFECT, loc, 2, 0.15, 0.15, 0.15, 0.3, Color.fromRGB(138, 3, 3));

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
						entity.getWorld().spawnParticle(Particle.DUST, entity.getLocation().clone().add(0,1,0), 14,
								0.35, 0.35, 0.35, 0.15, dust);
					}
					cancel();
				});

				radius += radiusStep;
				angle += angleStep;
				
				double x = -radius * Math.sin(angle);
				double z = -radius * Math.cos(angle);
				
				loc = startLoc.clone().add(x, 0, z);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
