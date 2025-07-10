package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;

public class SetUczenRaczkiStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		switch(info.getSetCounts().getOrDefault("Uczen_Raczki", 0)) {
			case 3:
				if(statType == StatTypes.ZRECZNOSC)
					e.increaceStatValue(12);
			case 2:
				if(statType == StatTypes.ZRECZNOSC)
					e.increaceStatValue(10);
				break;
		}
	}
	
}
