package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import lombok.Getter;

@Getter
public class CustomProjectileEntityDamageEvent extends EntityDamageByEntityEvent {
	
	public CustomProjectileEntityDamageEvent(Projectile damager, Entity damagee, double damage) {
		super(damager, damagee, DamageCause.PROJECTILE, damage);
	}

}
