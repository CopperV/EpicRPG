package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;

public class SetSnieznyTropicielStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		switch(info.getSetCounts().getOrDefault("Sniezny_Tropiciel", 0)) {
			case 4:
				if(statType == StatTypes.SUMMONS)
					e.increaceStatValue(1);
			case 3:
				if(statType == StatTypes.OBRAZENIA)
					e.increaceStatValue(30);
			case 2:
				if(statType == StatTypes.ZDOLNOSCI_MYSLIWSKIE)
					e.increaceStatValue(30);
				break;
		}
	}
	
}
