package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireworkdDamageListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		if(!(e.getDamager() instanceof Firework))
			return;
		
		e.setCancelled(true);
	}

}
