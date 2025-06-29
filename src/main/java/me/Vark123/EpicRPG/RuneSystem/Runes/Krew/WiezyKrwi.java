package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class WiezyKrwi extends ACastableRune {

	private static final DustOptions dust = new DustOptions(Color.fromRGB(138, 3, 3), 0.8f);
	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;

	private LivingEntity target = null;
	private Collection<UUID> shooted = new HashSet<>();
	
	public WiezyKrwi(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.6, -0.6, -0.6, 
				0.6, 0.6, 0.6);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		player.getWorld().playSound(startLoc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1f, 0.8f);
		
		double r = 30;
		double angle = 30;
		
		Location _loc = player.getLocation();
		player.getWorld().getNearbyEntities(_loc, r, r, r, entity -> {
			if(entity.getLocation().distanceSquared(_loc) > r*r)
				return false;
			
			if(!(entity instanceof LivingEntity))
				return false;
			
			if(Utils.getAngle(player, entity) > angle)
				return false;
			
			LivingEntity le = (LivingEntity) entity;
			return hitCondition.check(player, le);
		}).stream().map(entity -> (LivingEntity) entity).min((e1, e2) -> {
			double comp1 = (e1.getLocation().distance(_loc) / angle
					- e2.getLocation().distance(_loc) / angle) * 0.7;
			double comp2 = ((Utils.getAngle(player, e1)
					- Utils.getAngle(player, e2))) / angle * 0.3;
			return Double.compare(comp1 + comp2, 0);
		}).ifPresent(e -> target = e);
		
		if(target != null) {
			Utils.drawLine(Particle.DUST, startLoc, target.getLocation().clone().add(0,1,0),
					0.05, 2, 0.1f, 0.1f, 0.1f, 0.2f, dust);
			spellEffect(target);
			return;
		}
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.44, 
				3,
				1,
				40, 
				boundingBox,
				loc -> { }, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc, 8, 
							0.12f, 0.12f, 0.12f, rand.nextDouble(0.1, 0.3), dust);
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
		loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, rand.nextFloat(1.1f, 1.33f));
		loc.getWorld().spawnParticle(Particle.DUST, loc, 9,
				0.35, 0.35, 0.35, 0.14, dust);
		
		if(shooted.size() >= 5)
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
			Utils.drawLine(Particle.DUST, loc, entity.getLocation().clone().add(0,1,0),
					0.05, 2, 0.1f, 0.1f, 0.1f, 0.2f, dust);
			
			spellEffect(entity);
		});
	}

}
