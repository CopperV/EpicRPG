package me.Vark123.EpicRPG.OldFightSystem.Modifiers.ModList;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Shulker;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.OldFightSystem.Modifiers.DamageModifier;

public class ShulkerModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		double dmg = damage;
		
		if(!(victim instanceof Shulker))
			return dmg;

		switch(cause) {
			case PROJECTILE:
				if(damager instanceof AbstractArrow)
					damager = (Entity) ((AbstractArrow)damager).getShooter();
				break;
			default:
		}
		
		BoundingBox box = victim.getBoundingBox();
		if(Math.abs(box.getMaxY()-box.getMinY()) < 1.6) dmg *= 0.1;
		
		return dmg;
	}

}
