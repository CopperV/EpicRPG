package me.Vark123.EpicRPG.OldFightSystem;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

@Deprecated
public class RpgDamageEvent implements Listener {

	@EventHandler(priority=EventPriority.HIGH)
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		DamageCause cause = e.getCause();
		Entity damager = e.getDamager();

		switch(cause) {
			case ENTITY_ATTACK:
				e.setDamage(DamageManager.getInstance().getAttackCalculator().calc(damager, e.getEntity(), e.getDamage()));
				break;
			case PROJECTILE:
				e.setDamage(DamageManager.getInstance().getProjectileCalculator().calc(damager, e.getEntity(), e.getDamage()));
				damager = (Entity) ((Projectile) damager).getShooter();
				break;
			default:
				break;
		}
		
		if(e.getDamage() < 0) {
			e.setCancelled(true);
			return;
		}
		
		e.setDamage(DamageManager.getInstance().getDefenseCalculator().calc(damager, e.getEntity(), e.getDamage()));
		
		if(e.getDamage() < 0) {
			e.setCancelled(true);
			return;
		}
		
	}
	
}
