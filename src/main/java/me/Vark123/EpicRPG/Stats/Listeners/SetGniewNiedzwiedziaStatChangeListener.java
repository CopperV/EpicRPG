package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;

public class SetGniewNiedzwiedziaStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		if(info.getSetCounts().getOrDefault("Gniew_Niedzwiedzia", 0) < 2)
			return;

		if(statType != StatTypes.OCHRONA)
			return;

		e.increaceStatValue(20);
	}
	
}
