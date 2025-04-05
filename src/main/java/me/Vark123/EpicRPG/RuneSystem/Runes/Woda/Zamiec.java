package me.Vark123.EpicRPG.RuneSystem.Runes.Woda;

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
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Zamiec extends ACastableRune {
	
	private Random random = new Random();
	private IRuneHitCondition hitCondition;
	
	public Zamiec(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				rune.getDurationTime(),
				rune.getObszar(),
				4,
				20,
				loc -> {
					new BukkitRunnable() {
						int timer = rune.getDurationTime() * 2;
						@Override
						public void run() {
							if(isCancelled())
								return;
							if(timer <= 0 || !casterInCastWorld()) {
								this.cancel();
								return;
							}
							--timer;

							float vol = 0.8f + random.nextFloat(0.4f);
							float pitch = 0.5f + random.nextFloat(0.3f);
							loc.getWorld().playSound(loc, Sound.ENTITY_BREEZE_IDLE_GROUND, vol, pitch);
						}
					}.runTaskTimer(Main.getInstance(), 0, 10);
				},
				loc -> {
					for(int i = 0; i < 25; ++i) {
						double theta1 = Math.random() * Math.PI * 2;
						double theta2 = Math.random() * Math.PI * 2;
						double x = rune.getObszar() * Math.sin(theta1);
						double y = Math.random()*6 + -3;
						double z = rune.getObszar() * Math.cos(theta2);
						Vector v = Vector.getRandom().normalize();
						Location tmp = loc.clone().add(x,y,z);
						tmp.getWorld().spawnParticle(Particle.SNOWFLAKE, tmp, 0, v.getX(), v.getY(), v.getZ());
					}
					for(int i = 0; i < 25; ++i) {
						double theta1 = Math.random() * Math.PI * 2;
						double theta2 = Math.random() * Math.PI * 2;
						double x = rune.getObszar() * Math.sin(theta1);
						double y = Math.random()*6 + -3;
						double z = rune.getObszar() * Math.cos(theta2);
						Vector v = Vector.getRandom().normalize();
						Location tmp = loc.clone().add(x,y,z);
						tmp.getWorld().spawnParticle(Particle.CLOUD, tmp, 0, v.getX(), v.getY(), v.getZ());
					}
				},
				hitCondition,
				(loc, entity) -> {
					RuneUtils.damage(player, entity, rune);
				},
				loc -> { });
	}

}
