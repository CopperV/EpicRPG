package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate.BufferRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class AuraRozproszenia extends ACastableRune {

	private Random rand = new Random();
	private IRuneHitCondition hitCondition;
	
	public AuraRozproszenia(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		Location startLoc = castLoc.clone().add(0, 0.1, 0);
		
		InstantRangeRuneTemplate.castEffect(
				this, 
				startLoc,
				rune.getObszar(),
				0,
				0, 
				loc -> {
					createSpellEffect(loc);
				}, 
				hitCondition,
				(loc, entity) -> {
					BufferRuneTemplate.castEffect(
							castableRune, 
							rune.getName(),
							EpicModifierTypes.AURA_ROZPROSZENIA,
							entity,
							target -> { },
							target -> {
								target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 0.8f);

								Location _loc = target.getLocation().clone().add(0, 1, 0);

								double force = rand.nextDouble(0.01, 0.05);
								loc.getWorld().spawnParticle(Particle.SMOKE, _loc, 6, 0.4f, 0.8f, 0.4f, force);
							},
							new BufferRuneEffect(4, target -> {
								Location _loc = target.getLocation().clone().add(0, 1, 0);

								double force = rand.nextDouble(0.01, 0.1);
								loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, _loc, 6, 0.4f, 0.8f, 0.4f, force);
							}));
				});
	}
	
	private void createSpellEffect(Location loc) {
		loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1.5f, .6f);
		
		new BukkitRunnable() {
			double time = 0;
			double radius = rune.getObszar();
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!casterInCastWorld() || time >= 5) {
					this.cancel();
					return;
				}

                // Half Sphere Effect (Fading FLAME particles)
				int particleCount = (int) (250 * (5 - time)); // Decrease over time
                for (int i = 0; i < particleCount; i++) {
                    double theta = rand.nextDouble() * Math.PI;
                    double phi = rand.nextDouble() * Math.PI * 2;
                    double x = radius * Math.sin(theta) * Math.cos(phi);
                    double y = radius * Math.cos(theta);
                    double z = radius * Math.sin(theta) * Math.sin(phi);
                    loc.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(x, y, z), 1, 0, 0, 0, 0);
                }

                
                // Pentagram Effect (Rotating SMALL_FLAME pentagram at base)
                double angleOffset = time * 2 * Math.PI * 0.05; // Full rotation in 1 second
                Utils.drawPentagram(
                		Particle.SMALL_FLAME,
                		loc, 
                		new Vector(0,1,0),
                		5, 
                		radius,
                		0.1, 
                		2, 
                		angleOffset);
                
                // Particle Beam Effect (TRIAL_SPAWNER_DETECTION from center to top of dome)
                if (time <= 3) {
                    for (double h = 0; h <= radius; h += 0.1) {
                        loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, loc.clone().add(0, h, 0), 1, 0.05f, 0.1f, 0.05f, 0.01f);
                    }
                }
                
                time += 0.05;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
