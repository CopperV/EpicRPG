package me.Vark123.EpicRPG.FightSystem.Modifiers.ModList;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;

public class ScaleDamageModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		switch(cause) {
			case PROJECTILE:
				if(damager instanceof AbstractArrow)
					damager = (Entity) ((AbstractArrow)damager).getShooter();
				break;
			default:
		}
		
		if(!(damager instanceof Player))
			return damage;
		return (damage * 0.75);
	}

}
