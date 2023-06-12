package me.Vark123.EpicRPG.HorseSystem;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class HorseRemoveOnPlayerDeathEvent implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(e.getEntity().isInsideVehicle()) {
			Entity vehicle = e.getEntity().getVehicle();
			if(!(vehicle instanceof AbstractHorse))
				return;
			
			AbstractHorse horse = (AbstractHorse) vehicle;
			if(horse.getCustomName() == null || !horse.getCustomName().equalsIgnoreCase("§7Kon "+e.getEntity().getName()))
				return;
			
			horse.remove();
		}
	}

}
