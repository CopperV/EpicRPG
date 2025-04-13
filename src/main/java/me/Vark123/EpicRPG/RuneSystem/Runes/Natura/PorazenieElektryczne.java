package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class PorazenieElektryczne extends ACastableRune {

	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	private Collection<UUID> shooted = new HashSet<>();
	
	public PorazenieElektryczne(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.7, -0.7, -0.7, 
				0.7, 0.7, 0.7);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.4, 
				3,
				1,
				35, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ITEM_TRIDENT_RIPTIDE_3, 0.9f, 1.4f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.FIREWORK, loc, 4, 
							0.1f, 0.1f, 0.1f, rand.nextDouble(0.01, 0.03));
					loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 6, 
							0.15f, 0.15f, 0.15f, 0);
				}, 
				hitCondition, 
				(loc, e) -> {
					spellEffect(e);			
				}, 
				loc -> { });
	}
	
	private void spellEffect(Entity target) {
		if(!RuneUtils.damage(player, (LivingEntity) target, rune))
			return;

		shooted.add(target.getUniqueId());
		
		Location loc = target.getLocation().clone().add(0,1,0);
		loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, rand.nextFloat(1.2f, 1.8f));
		loc.getWorld().spawnParticle(Particle.FIREWORK, loc, 7,
				0.3, 0.3, 0.3, 0.04);
		
		if(shooted.size() >= 5)
			return;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled() || !casterInCastWorld())
					return;
				
				loc.getWorld().getNearbyEntities(loc, 5,5,5, entity -> {
					if(shooted.contains(entity.getUniqueId()))
						return false;
					if(entity.getLocation().distanceSquared(loc) > 5*5)
						return false;
					if(!(entity instanceof LivingEntity))
						return false;
					
					LivingEntity le = (LivingEntity) entity;
					return hitCondition.check(player, le);
				}).stream().min((e1, e2) -> {
					double dist1 = e1.getLocation().distanceSquared(loc);
					double dist2 = e2.getLocation().distanceSquared(loc);
					if(dist1 == dist2)
						return 0;
					return dist1 < dist2 ? -1 : 1;
				}).map(e -> (LivingEntity) e)
				.ifPresent(entity -> {
					Utils.drawLine(Particle.ELECTRIC_SPARK, loc, entity.getLocation().clone().add(0,1,0),
							0.05, 2, 0.1f, 0.1f, 0.1f, 0.03f);
					
					spellEffect(entity);
				});
			}
		}.runTaskLater(Main.getInstance(), 5);
	}

}
