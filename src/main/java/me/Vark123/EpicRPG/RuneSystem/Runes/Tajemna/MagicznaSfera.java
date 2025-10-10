package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import java.util.Random;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class MagicznaSfera extends ACastableRune {
	
	private static final Random rand = new Random();
	private static final DustOptions dust = new DustOptions(Color.fromRGB(157, 0, 255), 2.5f);

	private IRuneHitCondition hitCondition;
	
	public MagicznaSfera(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location loc = player.getLocation().clone().add(0,1,0);
		
		loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_PREPARE_ATTACK, 1.25f, 1.2f);
		loc.getWorld().spawnParticle(Particle.REVERSE_PORTAL, loc, 100, 0.5, 0.5, 0.5, 3);
		
		new BukkitRunnable() {
			int timer = rune.getDurationTime()*4;
			MutableDouble radius = new MutableDouble(rune.getObszar());
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(timer <= 0 || !casterInCastWorld()) {
					cancel();
					return;
				}
				--timer;

				double r = radius.doubleValue();
				loc.getWorld()
						.getNearbyEntities(loc, r, r, r, entity -> {
							if(entity.getLocation().distanceSquared(loc) > r * r)
								return false;
							
							if(!(entity instanceof LivingEntity))
								return false;
							
							LivingEntity le = (LivingEntity) entity;
							return hitCondition.check(player, le);
						})
						.stream()
						.map(entity -> (LivingEntity) entity)
						.forEach(entity -> {
							if(radius.doubleValue() < 1)
								return;
							if(RuneUtils.damage(player, entity, rune)) {
								Location eLoc = entity.getLocation().clone().add(0,1,0);
								
								eLoc.getWorld().playSound(eLoc, Sound.ENTITY_EVOKER_CAST_SPELL, 0.6f, 1.3f);
								eLoc.getWorld().spawnParticle(Particle.WITCH, eLoc, 8,
										0.4, 0.8, 0.4, 0.07);
								
								radius.subtract(0.5);
							}
						});
				
				if(radius.doubleValue() < 1) {
					cancel();
					return;
				}

				double tmpRadius = radius.getValue();
				for(int i = 0; i <= Math.ceil(tmpRadius*tmpRadius)*3; ++i) {
					double angle1 = rand.nextDouble(2*Math.PI);
					double angle2 = rand.nextDouble(2*Math.PI);
					
					double x = tmpRadius * Math.cos(angle1)*Math.cos(angle2);
					double y = tmpRadius * Math.sin(angle2);
					double z = tmpRadius * Math.sin(angle1)*Math.cos(angle2);
					
					Location tmpLoc = loc.clone().add(x,y,z);
					loc.getWorld().spawnParticle(Particle.DUST, tmpLoc, 1, .05, .05, .05, .02, dust);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
