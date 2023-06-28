package me.Vark123.EpicRPG.FightSystem.Modifiers;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public interface DamageModifier {

	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause);
	
}
