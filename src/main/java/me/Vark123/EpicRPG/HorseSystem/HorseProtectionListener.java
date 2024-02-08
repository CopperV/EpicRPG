package me.Vark123.EpicRPG.HorseSystem;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HorseProtectionListener implements Listener {

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getEntity();
		if(!(victim instanceof AbstractHorse))
			return;
		
		AbstractHorse horse = (AbstractHorse) victim;
		horse.getPassengers().stream()
			.filter(en -> en instanceof Player)
			.findAny()
			.ifPresent(en -> e.setCancelled(true));
	}
	
}
