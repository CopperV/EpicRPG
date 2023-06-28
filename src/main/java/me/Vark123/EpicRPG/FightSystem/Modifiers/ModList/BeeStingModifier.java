package me.Vark123.EpicRPG.FightSystem.Modifiers.ModList;

import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;

public class BeeStingModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		
		if(damager instanceof Bee) {
			Bee bee = (Bee) damager;
			if(!bee.hasStung()) {
				new BukkitRunnable() {
					
					@Override
					public void run() {
						bee.setHasStung(false);
					}
				}.runTaskLaterAsynchronously(Main.getInstance(), 1);
			}
		}
		
		return damage;
	}

}
