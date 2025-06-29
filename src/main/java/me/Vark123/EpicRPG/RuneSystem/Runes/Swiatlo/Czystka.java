package me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Czystka extends ACastableRune {

	private Random rand = new Random();
	private IRuneHitCondition hitCondition;
	
	public Czystka(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		double radius = rune.getObszar();
		double damage = rune.getDamage();
		
		castLoc.getWorld().playSound(castLoc, Sound.BLOCK_END_PORTAL_SPAWN, rand.nextFloat(1, 2), rand.nextFloat(1.2f, 1.5f));
		castLoc.getWorld().spawnParticle(Particle.WHITE_SMOKE, castLoc.clone().add(0,1,0), 20, radius, radius, radius);
		castLoc.getWorld().spawnParticle(Particle.CLOUD, castLoc.clone().add(0,1,0), 20, radius, radius, radius);

		Collection<LivingEntity> entities = new HashSet<>();
		castLoc.getWorld().getNearbyEntities(castLoc, radius, radius, radius, entity -> {
			if(entity.getLocation().distanceSquared(castLoc) > radius * radius)
				return false;
			
			if(!(entity instanceof LivingEntity))
				return false;
			
			LivingEntity le = (LivingEntity) entity;
			if(hitCondition != null)
				return hitCondition.check(player, le);
			return true;
		}).forEach(entity -> entities.add((LivingEntity) entity));
		
		if(entities.size() < 1)
			return;
		
		entities.forEach(entity -> {
			RuneUtils.damage(player, entity, rune, damage / entities.size());
		});
	}

}
