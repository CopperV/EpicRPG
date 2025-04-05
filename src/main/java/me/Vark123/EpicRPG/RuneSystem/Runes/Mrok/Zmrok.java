package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
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
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.MissileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Zmrok extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public Zmrok(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				0.75, 0.75, 0.75, 
				0.75, 0.75, 0.75);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone();
		double radius = rune.getObszar();
		
		List<Entity> startTargets = new ArrayList<>(player.getWorld().getNearbyEntities(startLoc, radius, radius, radius, entity -> {
			if(entity.getLocation().distanceSquared(startLoc) > radius * radius)
				return false;
			
			if(!(entity instanceof LivingEntity))
				return false;
			
			LivingEntity le = (LivingEntity) entity;
			if(hitCondition != null)
				return hitCondition.check(player, le);
			return true;
		}));
		
		List<Entity> targets;
		if(startTargets.size() > 5) {
			Random rand = new Random();
			targets = new ArrayList<>();
			while(targets.size() < 5) {
				Entity e = startTargets.get(rand.nextInt(startTargets.size()));
				if(targets.contains(e))
					continue;
				targets.add(e);
			}
		} else
			targets = startTargets;
		
		new BukkitRunnable() {
			int counter = 0;
			int timer = 1;
			@Override
			public void run() {
				if(counter >= targets.size() || !casterInCastWorld()) {
					cancel();
					return;
				}
				if(isCancelled())
					return;
				
				Location loc = player.getLocation();
				float angle = loc.getYaw()/60;
				Location castLoc = loc.clone();
				castLoc.add(0, 2.5, 0);
				castLoc.subtract(new Vector(Math.cos(angle), 0, Math.sin(angle))
						.normalize()
						.multiply(0.5));
				player.getWorld().spawnParticle(Particle.LARGE_SMOKE, castLoc, 4, 0.2f, 0.2f, 0.2f, 0.02f);
				
				if(timer % 20 == 0) {
					Entity e = targets.get(counter);
					spellEffect(castLoc, e);
					
					++counter;
				}
				++timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void spellEffect(Location startLoc, Entity target) {
		MissileRuneTemplate.castMissile(
				this, 
				startLoc, 
				(LivingEntity) target,
				0.5, 
				1,
				1,
				50, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_SHOOT, 1.2f, 1.4f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.SMOKE, loc, 8, 
							0.2f, 0.2f, 0.2f, 0.02f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_STRAY_DEATH, 1f, 0.6f);

						loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 15, 
								0.45f, 0.45f, 0.45f, 0.08f);
					}
				}, 
				loc -> { });
	}

}
