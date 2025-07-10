package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;

public class SetStalowaOchronaStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		switch(info.getSetCounts().getOrDefault("Stalowa_Ochrona", 0)) {
			case 4:
				if(statType == StatTypes.OCHRONA)
					e.increaceStatValue(25);
			case 3:
				if(statType == StatTypes.WYTRZYMALOSC)
					e.increaceStatValue(12);
			case 2:
				break;
		}
	}
	
}
