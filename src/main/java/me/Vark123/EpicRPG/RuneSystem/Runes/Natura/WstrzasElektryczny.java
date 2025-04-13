package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
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
import me.Vark123.EpicRPG.Utils.Utils;

public class WstrzasElektryczny extends ACastableRune {

	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;
	
	public WstrzasElektryczny(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location loc = player.getLocation().clone().add(0, 1, 0);
		loc.getWorld().playSound(loc, Sound.ITEM_TRIDENT_RIPTIDE_3, 0.9f, 1f);
		
		double radius = rune.getObszar();
		
		Collection<Entity> shooted = new HashSet<>();
		shooted.add(player);
		new BukkitRunnable() {
			Collection<Entity> lasts = new HashSet<>(shooted);
			Collection<Entity> newTargets = new HashSet<>();
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(shooted.size() > 26) {
					cancel();
					return;
				}
				
				newTargets.clear();
				
				lasts.stream().forEach(entity -> {
					Location loc = entity.getLocation().clone();
					entity.getWorld().getNearbyEntities(loc, radius, radius, radius, entity2 -> {
						if(entity2.getLocation().distanceSquared(loc) > radius * radius)
							return false;
						
						if(shooted.contains(entity2))
							return false;
						
						if(!(entity2 instanceof LivingEntity))
							return false;
						
						LivingEntity le = (LivingEntity) entity2;
						return hitCondition.check(player, le);
					}).forEach(target -> {
						shooted.add(target);
						newTargets.add(target);
						if(RuneUtils.damage(player, (LivingEntity) target, rune)) {
							loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.2f, rand.nextFloat(0.7f, 1f));
							loc.getWorld().spawnParticle(Particle.FIREWORK, loc, 9,
									0.35, 0.35, 0.35, 0.05);

							Utils.drawLine(Particle.ELECTRIC_SPARK, loc.clone().add(0,1,0), target.getLocation().clone().add(0,1,0),
									0.05, 2, 0.1f, 0.1f, 0.1f, 0.03f);
						}
					});
				});
				if(newTargets.isEmpty() || !casterInCastWorld()) {
					cancel();
					return;
				}
				
				lasts.clear();
				lasts.addAll(newTargets);
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
