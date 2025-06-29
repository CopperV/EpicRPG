package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

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
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Zaglada extends ACastableRune {

	private static final Random random = new Random();
	private static final int TRAILS_AMOUNT = 10;

	private IRuneHitCondition hitCondition;
	
	public Zaglada(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.1, 0);
		
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				rune.getObszar(),
				0,
				20*5,
				loc -> {
					double radius = rune.getObszar();
					
					Collection<Location> trailPoints = new LinkedList<>();
					for(int i = 0; i < TRAILS_AMOUNT; ++i) {
						Vector originalVector = new Vector(random.nextDouble(2)-1, 0, random.nextDouble(2)-1).normalize().multiply(0.25);
						Vector vec = originalVector.clone();
						Location trailLoc = loc.clone();
						
						int length = (int) (radius * 5);
						int distance = 10;
						while(length > 0 && trailLoc.distanceSquared(startLoc) <= (radius * radius)) {
							trailLoc.add(vec);
							trailPoints.add(trailLoc.clone());
							
							if(distance < 0 && random.nextDouble() < 0.1) {
								vec = originalVector.clone().rotateAroundY(random.nextDouble(Math.toRadians(90)) - Math.toRadians(45));
								distance = 10;
							}
							
							--length;
							--distance;
						}
					}
					
					loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 2.5f, 0.5f);
					new BukkitRunnable() {
						int points = (int) (radius * 6);
						int timer = 4*5;
						double speed = 0.01f;
						@Override
						public void run() {
							if(isCancelled())
								return;
							if(!casterInCastWorld()) {
								cancel();
								return;
							}
							if(timer <= 0) {
								loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_DEATH, 2f, 0.7f);
								for(int i = 0; i < points; ++i) {
									double angle = random.nextDouble(Math.PI*2);
									
									double x = radius * Math.sin(angle);
									double z = radius * Math.cos(angle);
									
									Location tmp = loc.clone().add(x,0,z);
									tmp.getWorld().spawnParticle(Particle.LARGE_SMOKE, tmp, 1, 0.1f, 0.1f, 0.1f, 0.15f);
								}
								for(Location tmp : trailPoints)
									tmp.getWorld().spawnParticle(Particle.SMOKE, tmp, 1, 0.1f, 0.1f, 0.1f, 0.15f);
								
								cancel();
								return;
							}
							
							for(int i = 0; i < points; ++i) {
								double angle = random.nextDouble(Math.PI*2);
								
								double x = radius * Math.sin(angle);
								double z = radius * Math.cos(angle);
								
								Location tmp = loc.clone().add(x,0,z);
								tmp.getWorld().spawnParticle(Particle.LARGE_SMOKE, tmp, 0, 0, 0.5f, 0, speed);
							}
							for(Location tmp : trailPoints)
								tmp.getWorld().spawnParticle(Particle.SMOKE, tmp, 0, 0, 0.5f, 0, speed);
							
							--timer;
							speed += 0.01;
						}
					}.runTaskTimer(Main.getInstance(), 0, 5);
				},
				hitCondition,
				(loc, entity) -> {
					RuneUtils.damage(player, entity, rune);
				});
	}

}
