package me.Vark123.EpicRPG.RuneSystem.Runes.Woda;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;

public class LodowaTarcza_H extends ACastableRune {

	private Random rand = new Random();
	private IRuneHitCondition hitCondition;
	
	public LodowaTarcza_H(RpgPlayer rpgPlayer, EpicRune rune) {
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
				40, 
				loc -> {
					createSpellEffect(loc);
				}, 
				hitCondition,
				(loc, entity) -> {
					BufferRuneTemplate.castEffect(
							castableRune, 
							rune.getName(),
							EpicModifierTypes.LODOWA_TARCZA_H,
							entity,
							target -> { },
							target -> {
								target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 0.8f);

								Location _loc = target.getLocation().clone().add(0, 1, 0);

								double force = rand.nextDouble(0.01, 0.05);
								loc.getWorld().spawnParticle(Particle.SMOKE, _loc, 6, 0.4f, 0.8f, 0.4f, force);
							},
							new TimingRuneEffect(4, target -> {
								Location _loc = target.getLocation().clone().add(0, 1, 0);

								loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, _loc, 4, 0.4f, 0.8f, 0.4f, rand.nextDouble(0.01, 0.05));
								loc.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, _loc, 3, 0.4f, 0.8f, 0.4f, rand.nextDouble(0.02, 0.09));
							}));
				});
	}
	
	private void createSpellEffect(Location loc) {
		loc.getWorld().playSound(loc, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, 1.5f, .6f);
		
		new BukkitRunnable() {
			double radius = rune.getObszar();
			double r = 0.5;
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!casterInCastWorld() || r >= radius) {
					this.cancel();
					return;
				}

                // Half Sphere Effect (Fading FLAME particles)
				int particleCount = (int) (250 * (r/radius)); // Decrease over time
                for (int i = 0; i < particleCount; i++) {
                    double theta = rand.nextDouble() * Math.PI;
                    double phi = rand.nextDouble() * Math.PI * 2;
                    double x = radius * Math.sin(theta) * Math.cos(phi);
                    double y = radius * Math.cos(theta);
                    double z = radius * Math.sin(theta) * Math.sin(phi);
                    loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, loc.clone().add(x, y, z), 1, 0, 0, 0, 0);
                }
                for (int i = 0; i < particleCount; i++) {
                    double theta = rand.nextDouble() * Math.PI;
                    double phi = rand.nextDouble() * Math.PI * 2;
                    double x = radius * Math.sin(theta) * Math.cos(phi);
                    double y = radius * Math.cos(theta);
                    double z = radius * Math.sin(theta) * Math.sin(phi);
                    loc.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, loc.clone().add(x, y, z), 1, 0, 0, 0, 0);
                }
                for (int i = 0; i < particleCount; i++) {
                    double theta = rand.nextDouble() * Math.PI;
                    double phi = rand.nextDouble() * Math.PI * 2;
                    double x = radius * Math.sin(theta) * Math.cos(phi);
                    double y = radius * Math.cos(theta);
                    double z = radius * Math.sin(theta) * Math.sin(phi);
                    loc.getWorld().spawnParticle(Particle.SNEEZE, loc.clone().add(x, y, z), 1, 0, 0, 0, 0);
                }
                
                r += 0.25;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
