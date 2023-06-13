package me.Vark123.EpicRPG.BlackrockSystem.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.BlackrockSystem.BlackRockOperations;
import me.Vark123.EpicRPG.BlackrockSystem.BlackrockEvent;
import me.Vark123.EpicRPG.BlackrockSystem.BlackrockManager;

public class BlackrockRemoveAllEvent implements Listener {
	
	@EventHandler
	public void onCall(BlackrockEvent e) {
		if(e.isCancelled())
			return;
		if(!e.getOperation().equals(BlackRockOperations.REMOVE_ALL))
			return;
		BlackrockManager.getInstance().getCompletedPlayers().clear();
	}

}
