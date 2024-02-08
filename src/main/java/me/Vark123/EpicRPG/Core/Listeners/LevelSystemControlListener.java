package me.Vark123.EpicRPG.Core.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Core.Events.CoinsModifyEvent;
import me.Vark123.EpicRPG.Core.Events.StygiaModifyEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class LevelSystemControlListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCoins(CoinsModifyEvent e) {
		if(e.isCancelled())
			return;
		
		RpgPlayer rpg = e.getRpg();
		RpgPlayerInfo info = rpg.getInfo();
		switch(e.getReason().toLowerCase()) {
			case "mob":
			case "quest":
				if(info.getLevel() < 50)
					e.setCancelled(true);
				break;
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onStygia(StygiaModifyEvent e) {
		if(e.isCancelled())
			return;
		
		RpgPlayer rpg = e.getRpg();
		RpgPlayerInfo info = rpg.getInfo();
		switch(e.getReason().toLowerCase()) {
			case "mob":
			case "quest":
				if(info.getLevel() < 60)
					e.setCancelled(true);
				break;
		}
	}
	
}
