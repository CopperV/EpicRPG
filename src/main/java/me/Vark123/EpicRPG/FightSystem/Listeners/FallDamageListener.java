package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class FallDamageListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onDamage(EntityDamageEvent e) {
		Entity victim = e.getEntity();
		if(!(victim instanceof Player))
			return;
		DamageCause cause = e.getCause();
		if(!cause.equals(DamageCause.FALL))
			return;
		e.setDamage(e.getDamage()*5);
	}

}
