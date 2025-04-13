package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

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

public class Eksplozja extends ACastableRune {
	
	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;

	public Eksplozja(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}
	
	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.25, 0);
		int timer = 5;
		
		InstantRangeRuneTemplate.castEffect(
				this, 
				startLoc,
				rune.getObszar(),
				0,
				20*timer,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 0.2f);
					loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 0.5f);
					loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 0.8f);
					loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 1.1f);
					loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 1.4f);
					loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 1.7f);
					
					for(int i = 0; i < 100; ++i) {
						double radius = rand.nextDouble(1.5, 3);
						double theta = Math.random()*Math.PI*2;
						double x = radius * Math.sin(theta);
						double y = Math.random()*2.5+0.1;
						double z = radius * Math.cos(theta);
						double force = rand.nextDouble(0.1, 0.33);
						
						Vector vec = new Vector(x,y,z);
						Vector dir = vec.clone().normalize().multiply(-2);
						loc.getWorld().spawnParticle(Particle.CRIT, loc.clone().add(vec), 0,
								dir.getX(), dir.getY(), dir.getZ(), force);
					}
					
					new BukkitRunnable() {
						
						@Override
						public void run() {
							if(isCancelled())
								return;
							
							if(!casterInCastWorld())
								return;

							loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 3, 0.2f);
							
							for(int i = 0; i < 100; ++i) {
								double radius = 1;
								double theta = Math.random()*Math.PI*2;
								double x = radius * Math.sin(theta);
								double y = Math.random()*2.5+0.1;
								double z = radius * Math.cos(theta);
								double force = rand.nextDouble(0.25, 0.5);
								
								Vector vec = new Vector(x,y,z).normalize().multiply(8);
								loc.getWorld().spawnParticle(Particle.CRIT, loc, 0,
										vec.getX(), vec.getY(), vec.getZ(), force);
							}
						}
					}.runTaskLater(Main.getInstance(), 20*timer);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						Location eLoc = entity.getLocation().clone();
						double strength = rune.getObszar() - Math.abs(startLoc.distance(eLoc));
						Vector velocity = new Vector(
								eLoc.getX() - loc.getX(),
								0.5,
								eLoc.getZ() - loc.getZ()).normalize().setY(1.25).multiply(strength * 0.5);
						entity.setVelocity(velocity);
					}
				});
	}

}
