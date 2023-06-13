package me.Vark123.EpicRPG.BlackrockSystem.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.BlackrockSystem.BlackRockOperations;
import me.Vark123.EpicRPG.BlackrockSystem.BlackrockEvent;
import me.nikl.calendarevents.CalendarEvent;

public class BlackrockResetEvent implements Listener {

	@EventHandler
	public void onDate(CalendarEvent e) {
		if(e.isCancelled())
			return;
		if(!e.getLabels().contains("reset_blackrock"))
			return;
		Bukkit.broadcastMessage(Main.getInstance().getPrefix()+" §cCzarnogora zostala zrestartowana - mozecie znow zapuscic sie w czeluscia.");
		BlackrockEvent event = new BlackrockEvent(null, BlackRockOperations.REMOVE_ALL);
		Bukkit.getPluginManager().callEvent(event);
	}
	
}
