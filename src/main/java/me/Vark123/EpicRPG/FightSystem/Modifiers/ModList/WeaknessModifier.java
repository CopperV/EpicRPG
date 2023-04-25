package me.Vark123.EpicRPG.FightSystem.Modifiers.ModList;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;

public class WeaknessModifier implements DamageModifier {

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
		Player p = (Player) damager;
		if(p.hasPotionEffect(PotionEffectType.WEAKNESS)) {
			double level = p.getPotionEffect(PotionEffectType.WEAKNESS).getAmplifier();
			if(level > 8)
				return -1;
			damage *= (1-(level+1)*0.1);
		}
		
		return damage;
	}

}
