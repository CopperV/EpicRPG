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
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class PrzywolanieBlyskawicy extends ACastableRune {
	private static final Random rand = new Random();
	private IRuneHitCondition hitCondition;
	private IRunePostDamageEffect hitEffect;

	public PrzywolanieBlyskawicy(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}
	
	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.25, 0);
		int timer = 3;
		
		InstantRangeRuneTemplate.castEffect(
				this, 
				startLoc,
				rune.getObszar(),
				0,
				20*timer,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_AMBIENT, 1.2f, 0.7f);
					
					new BukkitRunnable() {
						int _timer = timer*20;
						@Override
						public void run() {
							if(isCancelled())
								return;
							if(!casterInCastWorld()) {
								cancel();
								return;
							}
							if(_timer <= 0) {
								castEffect(loc);
								
								cancel();
								return;
							}
							--_timer;
							
							loc.getWorld().spawnParticle(Particle.FIREWORK, loc, 6,
									0.2, 0.2, 0.2, 0.04);
						}
					}.runTaskTimer(Main.getInstance(), 0, 1);
				}, 
				hitCondition, 
				(loc, entity) -> {
					RuneUtils.damage(player, entity, rune, hitEffect);
				});
	}
	
	private void castEffect(Location loc) {
		loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.2f, 0.5f);
		
		double radius = rune.getObszar();
		for(double i = 0.5; i < radius; i+=0.5) {
			double angleStep = (Math.PI*2) / (i*4+1);
			for(int j = 0; j < (i*4+1); ++j) {
				double angle = j * angleStep;
				double x = i * Math.sin(angle);
				double z = i * Math.cos(angle);
				
				Location pos = loc.clone().add(x,0,z);
				pos.getWorld().spawnParticle(Particle.FIREWORK, pos, 2,
						0.1, 0.15, 0.1, 0.03);
			}
		}
		int count = 4 + rand.nextInt(5);
		for(int i = 0; i < count; ++i) {
			double offsetX = (rand.nextDouble() - 0.5) * 3;
            double offsetZ = (rand.nextDouble() - 0.5) * 3;
            double offsetY = 2 + rand.nextDouble() * 3.5;
            
            Location next = loc.clone().add(new Vector(offsetX, offsetY, offsetZ));
            
            Utils.drawLine(Particle.FIREWORK, loc, next, 0.1, 3, 0.13f, 0.13f, 0.13f, 0.05f);
            Utils.drawLine(Particle.ELECTRIC_SPARK, loc, next, 0.07, 2, 0.1f, 0.1f, 0.1f, 0.03f);
		
            loc = next;
		}
	}

}
