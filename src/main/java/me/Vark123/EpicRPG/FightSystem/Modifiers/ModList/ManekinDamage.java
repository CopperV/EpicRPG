package me.Vark123.EpicRPG.FightSystem.Modifiers.ModList;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;

public class ManekinDamage implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		Entity send;
		switch(cause) {
			case PROJECTILE:
				if(damager instanceof AbstractArrow)
					send = (Entity) ((AbstractArrow)damager).getShooter();
				else
					send = damager;
				break;
			default:
				send = damager;
		}
		if(victim.getName().equalsIgnoreCase("§b§oManekin treningowy"))
			send.sendMessage("§7[§c§lTRENING§7] §aZadales §e"+(int)damage+" §aobrazen.");
		return damage;
	}

	
	
}
