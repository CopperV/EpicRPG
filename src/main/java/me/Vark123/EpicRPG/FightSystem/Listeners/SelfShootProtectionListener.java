package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SelfShootProtectionListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		Entity victim = e.getEntity();
		if(!(victim instanceof Player))
			return;
		if(!(damager instanceof Projectile))
			return;
		
		Projectile projectile = (Projectile) damager;
		damager = (Entity) projectile.getShooter();
		if(!damager.equals(victim))
			return;
		
		projectile.remove();
		e.setCancelled(true);
	}
	
}
