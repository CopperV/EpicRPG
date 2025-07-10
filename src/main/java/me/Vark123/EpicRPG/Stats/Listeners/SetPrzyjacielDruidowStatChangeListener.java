package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;

public class SetPrzyjacielDruidowStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		switch(info.getSetCounts().getOrDefault("Przyjaciel_Druidow", 0)) {
			case 4:
				if(statType == StatTypes.SUMMONS)
					e.increaceStatValue(1);
			case 3:
				if(statType == StatTypes.WALKA)
					e.increaceStatValue(9);
			case 2:
				if(statType == StatTypes.ZDOLNOSCI_MYSLIWSKIE)
					e.increaceStatValue(16);
				break;
		}
	}
	
}
