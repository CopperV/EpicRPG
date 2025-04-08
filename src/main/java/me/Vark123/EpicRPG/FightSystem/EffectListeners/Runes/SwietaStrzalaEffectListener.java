package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class SwietaStrzalaEffectListener implements Listener {
	
	private static final IRuneHitCondition hitCondition = new PvPRuneHitCondition();

	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;
		
		LivingEntity damager = e.getDamager();
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.SWIETA_STRZALA))
			return;
		
		e.increaseModifier(0.12);
	}
	
	@EventHandler
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;
		
		LivingEntity victim = e.getVictim();
		LivingEntity damager = e.getDamager();
		
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.SWIETA_STRZALA))
			return;
		
		Location loc = victim.getLocation().clone().add(0,1,0);
		loc.getWorld().spawnParticle(Particle.FIREWORK, loc, 40,
				2.5, 2.5, 2.5, 0.15);
		loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, loc, 40,
				2, 2, 2, 0.08);
		
		double radius = 4;
		double damage = e.getFinalDamage() * 0.25;
		victim.getWorld().getNearbyEntities(loc, radius, radius, radius, entity -> {
			if(entity.equals(victim))
				return false;
			
			if(entity.getLocation().distanceSquared(loc) > radius * radius)
				return false;
			
			if(!(entity instanceof LivingEntity))
				return false;
			
			LivingEntity le = (LivingEntity) entity;

			return hitCondition.check((Player) damager, le);
		}).forEach(entity -> {
			DamageUtils.applyDirectDamageEffect(
					damager, 
					(LivingEntity) entity, 
					damage, 
					e.getDamageSource().getDamageType(),
					DamageCause.CUSTOM);
		});
	}

}
