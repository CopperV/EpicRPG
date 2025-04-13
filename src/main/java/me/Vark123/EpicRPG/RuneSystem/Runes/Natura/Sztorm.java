package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Sztorm extends ACastableRune {
	
	private static final Random random = new Random();
	private static final PotionEffect effect = new PotionEffect(PotionEffectType.SLOWNESS, 20*2, 2);
	
	private IRuneHitCondition hitCondition;
	
	public Sztorm(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.1, 0);
		Location effectLoc = startLoc.clone().add(0,30,0);
		double radius = rune.getObszar();
		
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				rune.getDurationTime(),
				rune.getObszar(),
				3,
				20,
				loc -> {
					loc.getWorld().playSound(loc, Sound.WEATHER_RAIN_ABOVE, 3f, 0.7f);
					new BukkitRunnable() {
						int timer = rune.getDurationTime();
						@Override
						public void run() {
							if(isCancelled())
								return;
							if(timer <= 0 || !casterInCastWorld()) {
								this.cancel();
								return;
							}
							--timer;

							float vol = 1.5f + random.nextFloat(1.5f);
							float pitch = 0.6f + random.nextFloat(0.3f);
							loc.getWorld().playSound(loc, Sound.WEATHER_RAIN_ABOVE, vol, pitch);
						}
					}.runTaskTimer(Main.getInstance(), 0, 20);
				},
				loc -> {
					for(int i = 0; i < radius * 10; ++i) {
						double r = random.nextDouble(radius);
						double angle = random.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double y = random.nextDouble(-0.1, 0.1);
						double z = r *  Math.cos(angle);
						Location pos = effectLoc.clone().add(x, y, z);
						
						loc.getWorld().spawnParticle(Particle.CLOUD, pos, 1,
								0, 0, 0, random.nextDouble(0, 0.05));
					}
					for(int i = 0; i < radius * 6; ++i) {
						double r = random.nextDouble(radius);
						double angle = random.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double y = random.nextDouble(-0.1, 0.1);
						double z = r *  Math.cos(angle);
						Location pos = effectLoc.clone().add(x, y, z);
						
						loc.getWorld().spawnParticle(Particle.WHITE_SMOKE, pos, 1,
								0, 0, 0, random.nextDouble(0, 0.06));
					}
					for(int i = 0; i < radius * 6; ++i) {
						double r = random.nextDouble(radius);
						double angle = random.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double y = random.nextDouble(-0.1, 0.1);
						double z = r *  Math.cos(angle);
						Location pos = effectLoc.clone().add(x, y, z);
						
						loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, pos, 1,
								0, 0, 0, random.nextDouble(0, 0.02));
					}
					for(int i = 0; i < radius * 12; ++i) {
						double r = random.nextDouble(radius);
						double angle = random.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double y = random.nextDouble(-0.1, 0.1);
						double z = r *  Math.cos(angle);
						Location pos = effectLoc.clone().add(x, y, z);
						
						loc.getWorld().spawnParticle(Particle.FALLING_WATER, pos, 1,
								0, 0, 0, random.nextDouble(0, 0.05));
					}
				},
				hitCondition,
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						entity.addPotionEffect(effect);
						
						if(random.nextDouble() < 0.25) {
							loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.3f, 0.6f);
							loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().add(0,1,0), 10, 
									0.4f, 0.6f, 0.4f, 0.02f);
							Vector vel = new Vector(
									random.nextDouble(-1, 1),
									random.nextDouble(2),
									random.nextDouble(-1, 1))
									.normalize()
									.multiply(random.nextDouble(1.5, 3.5));
							entity.setVelocity(vel);
						}
					}
				},
				loc -> { });
	}

}
