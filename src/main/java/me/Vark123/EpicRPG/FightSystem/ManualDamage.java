package me.Vark123.EpicRPG.FightSystem;

import org.bukkit.EntityEffect;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.Vark123.EpicRPG.Utils.Utils;

public final class ManualDamage {

	private ManualDamage() { }
	
	public static boolean tryDoDamage(LivingEntity damager, LivingEntity victim, 
			double damage, EntityDamageByEntityEvent event) {
		return tryDoDamage(damager, victim, damage, event, true);
	}
	
	public static boolean tryDoDamage(LivingEntity victim, 
			double damage, EntityDamageEvent event) {
		return tryDoDamage(null, victim, damage, event, true);
	}
	
	public static boolean tryDoDamage(LivingEntity damager, LivingEntity victim, 
			double damage, EntityDamageEvent event, boolean checkFlag) {
		if(checkFlag && event.isCancelled())
			return false;
		
		return doDamage(damager, victim, damage, event);
	}
	
	public static boolean doDamage(LivingEntity damager, LivingEntity victim, double damage, EntityDamageEvent event) {
		if(event.isCancelled())
			return false;
		
		Utils.setLastDamageCause(victim, event);
		
		double absorption = victim.getAbsorptionAmount();
		if(absorption > 0) {
			if(absorption > damage) {
				victim.setAbsorptionAmount(absorption - damage);
				damage = 0;
			} else {
				victim.setAbsorptionAmount(0);
				damage -= absorption;
			}
		}

		if(victim.getHealth() < damage) {
			victim.playEffect(EntityEffect.ENTITY_DEATH);
			victim.setHealth(0);
			return true;
		}
		
		victim.playHurtAnimation(0);
		
		if(victim.getHealth() > victim.getAttribute(Attribute.MAX_HEALTH).getValue())
			victim.setHealth(victim.getAttribute(Attribute.MAX_HEALTH).getValue());
		victim.setHealth(victim.getHealth() - damage);
		return true;
	}
	
}
