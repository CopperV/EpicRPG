package me.Vark123.EpicRPG.BlackrockSystem.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.BlackrockSystem.BlackRockOperations;
import me.Vark123.EpicRPG.BlackrockSystem.BlackrockEvent;
import me.Vark123.EpicRPG.BlackrockSystem.BlackrockManager;

public class BlackrockAddAllEvent implements Listener {

	@EventHandler
	public void onCall(BlackrockEvent e) {
		if(e.isCancelled())
			return;
		if(!e.getOperation().equals(BlackRockOperations.ADD_ALL))
			return;
		Bukkit.getOnlinePlayers().forEach(p -> {
			BlackrockManager.getInstance().completeDailyBlackrock(p);
		});
	}
	
}
