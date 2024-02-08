package me.Vark123.EpicRPG.OldFightSystem.Modifiers;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public interface DamageModifier {

	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause);
	
}
