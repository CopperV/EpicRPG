package me.Vark123.EpicRPG.HorseSystem;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class HorseDismountEvent implements Listener {

	@EventHandler
	public void onDismount(VehicleExitEvent e) {
		if(!(e.getExited() instanceof Player))
			return;
		if(!(e.getVehicle() instanceof AbstractHorse))
			return;
		
		AbstractHorse horse = (AbstractHorse) e.getVehicle();
		if(horse.getCustomName() == null || !horse.getCustomName().equalsIgnoreCase("§7Kon "+e.getExited().getName()))
			return;
		
		horse.remove();
	}

}
