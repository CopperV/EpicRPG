package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;

public class SetTemplariuszStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		if(info.getSetCounts().getOrDefault("Templariusz", 0) < 3)
			return;
		
		if(statType != StatTypes.OCHRONA && statType != StatTypes.WYTRZYMALOSC)
			return;
		
		e.increaceStatValue(15);
	}
	
}
