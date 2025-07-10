package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;

public class SetChlopiaSilaStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		switch(info.getSetCounts().getOrDefault("Chlopia_Sila", 0)) {
			case 4:
				if(statType == StatTypes.WYTRZYMALOSC)
					e.increaceStatValue(2);
			case 3:
				if(statType == StatTypes.SILA)
					e.increaceStatValue(1);
			case 2:
				if(statType == StatTypes.OCHRONA)
					e.increaceStatValue(2);
				break;
		}
	}
	
}
