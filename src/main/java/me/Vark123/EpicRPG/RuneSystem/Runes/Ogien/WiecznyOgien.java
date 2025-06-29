package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.MultiTotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class WiecznyOgien extends ACastableRune {

	private Random rand = new Random();
	private IRuneHitCondition hitCondition;

	public WiecznyOgien(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		double radius = rune.getObszar();
		Collection<Location> locations = castLoc.getWorld().getNearbyEntities(castLoc, radius, radius, radius, entity -> {
			if(entity.getLocation().distanceSquared(castLoc) > radius * radius)
				return false;
			
			if(!(entity instanceof LivingEntity le))
				return false;
			
			if(hitCondition != null)
				return hitCondition.check(player, le);
			return true;
		}).stream()
		.map(entity -> entity.getLocation().clone().add(0, 1, 0))
		.collect(Collectors.toSet());
		
		MultiTotemRuneTemplate.castTotems(
				this,
				locations,
				rune.getDurationTime(),
				radius*0.225, 
				4,
				20,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1f, rand.nextFloat(0.65f, 0.75f));
				},
				loc -> {
					for(double r = 0.5; r <= radius; r += 0.5) {
						int points = (int) (r * 2);
						for(int i = 0; i < points; ++i) {
							double angle = rand.nextDouble(Math.PI * 2);
							
							double x = r * Math.sin(angle);
							double z = r * Math.cos(angle);
							
							Location tmp = loc.clone().add(x,0,z);
							tmp.getWorld().spawnParticle(Particle.SMALL_FLAME, tmp, 1,
									0.05f, 0.05f, 0.05f, rand.nextDouble(0, 0.05));
						}
						
						points = (int) (r * 3);
						for(int i = 0; i < points; ++i) {
							double angle = rand.nextDouble(Math.PI * 2);
							
							double x = r * Math.sin(angle);
							double z = r * Math.cos(angle);
							
							Location tmp = loc.clone().add(x,0,z);
							tmp.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, tmp, 1,
									0.05f, 0.05f, 0.05f, rand.nextDouble(0, 0.07));
						}
					}
				},
				hitCondition,
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(entity, Sound.ENTITY_PLAYER_HURT_ON_FIRE, rand.nextFloat(0.8f, 1.2f), rand.nextFloat(0.6f, 0.8f));
						loc.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(0,1,0), 7, 0.4, 0.4, 0.4, rand.nextDouble(0, 0.1));
					}
				}, 
				loc -> { });
	}

}
