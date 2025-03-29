package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLivingEntityEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;

public class InstantRangeRuneTemplate {

	private InstantRangeRuneTemplate() { }
	
	public static void castEffect(
			ACastableRune castableRune,
			Location loc,
			double radius,
			int castEffectDelay,
			int hitEffectDelay,
			IRuneLocationEffect castEffect,
			IRuneHitCondition hitCondition,
			IRuneLivingEntityEffect hitEffect) {
		
		if(castEffect != null)
			new BukkitRunnable() {
				@Override
				public void run() {
					castEffect.playEffect(loc);
				}
			}.runTaskLater(Main.getInstance(), castEffectDelay);
			
		new BukkitRunnable() {
			Player player = castableRune.getPlayer();
			@Override
			public void run() {
				if(hitEffect == null)
					return;
				
				loc.getWorld().getNearbyEntities(loc, radius, radius, radius, entity -> {
					if(entity.getLocation().distanceSquared(loc) > radius * radius)
						return false;
					
					if(!(entity instanceof LivingEntity))
						return false;
					
					LivingEntity le = (LivingEntity) entity;
					if(hitCondition != null)
						return hitCondition.check(player, le);
					return true;
				}).forEach(entity -> {
					hitEffect.playEffect(entity.getLocation(), (LivingEntity) entity);
				});
			}
		}.runTaskLater(Main.getInstance(), hitEffectDelay);
		
	}
	
}
