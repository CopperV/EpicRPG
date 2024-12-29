package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import me.Vark123.EpicRPG.Utils.Utils;

public class EntityLastDamageCauseListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamage(EntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		Utils.setLastDamageCause(e.getEntity(), e);
	}
	
}
