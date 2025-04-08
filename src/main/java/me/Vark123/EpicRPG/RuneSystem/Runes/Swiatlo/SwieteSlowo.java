package me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class SwieteSlowo extends ACastableRune {
	
	private IRuneHitCondition hitCondition;

	public SwieteSlowo(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.2, 0);
		
		WaveRuneTemplate.castWave(
				this,
				startLoc, 
				rune.getObszar(),
				9, 
				0.28, 
				1,
				1, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ITEM_TOTEM_USE, 1.2f, 1.5f);
					createBeamEffect(loc.clone());
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.FIREWORK, loc, 4,
							0.05, 0.1, 0.05, 0.02);
					loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, loc, 2,
							0.05, 0.1, 0.05, 0.01);
					loc.getWorld().spawnParticle(Particle.WAX_OFF, loc, 3,
							0.05, 0.1, 0.05, 0.03);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1.6f);
					}
					
				},
				loc -> { });
	}
	
	private void createBeamEffect(Location start) {
		new BukkitRunnable() {
			double radius = 0;
			double radiusStep = 0.28;
			Location end = start.clone().add(0,35,0);
			Vector vec = new Vector(
					end.getX() - start.getX(),
					end.getY() - start.getY(),
					end.getZ() - start.getZ()
					).normalize().multiply(0.2);
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!casterInCastWorld() || radius >= rune.getObszar()) {
					cancel();
					return;
				}
				radius += radiusStep;
				
				Location pos = start.clone();
				while(pos.distanceSquared(start) <= end.distanceSquared(start)) {
					pos.getWorld().spawnParticle(Particle.FIREWORK, pos, 4,
							0.1, 0.15, 0.1, 0.02);
					pos.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, pos, 2,
							0.1, 0.15, 0.1, 0.01);
					pos.getWorld().spawnParticle(Particle.WAX_OFF, pos, 3,
							0.1, 0.15, 0.1, 0.03);
					
					pos.add(vec);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
