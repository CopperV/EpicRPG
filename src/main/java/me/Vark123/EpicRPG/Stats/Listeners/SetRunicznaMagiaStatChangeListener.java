package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;

public class SetRunicznaMagiaStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		if(info.getSetCounts().getOrDefault("Runiczna_Magia", 0) < 2)
			return;
		
		switch(statType) {
			case INTELIGENCJA:
			case MANA:
			case OBRAZENIA:
			case OCHRONA:
			case SILA:
			case WALKA:
			case WYTRZYMALOSC:
			case ZDOLNOSCI_MYSLIWSKIE:
			case ZRECZNOSC:
				e.increaceStatValue(5);
				break;
			case KRAG:
			case ZYCIE:
			case SUMMONS:
			default:
				break;
		}
	}
	
}
