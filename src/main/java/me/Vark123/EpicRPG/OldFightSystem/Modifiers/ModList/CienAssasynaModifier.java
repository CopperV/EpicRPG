package me.Vark123.EpicRPG.OldFightSystem.Modifiers.ModList;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.OldFightSystem.Modifiers.DamageModifier;

@Deprecated
public class CienAssasynaModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		
		if(!cause.equals(DamageCause.ENTITY_ATTACK))
			return damage;
		
		return damage;
	}

}
