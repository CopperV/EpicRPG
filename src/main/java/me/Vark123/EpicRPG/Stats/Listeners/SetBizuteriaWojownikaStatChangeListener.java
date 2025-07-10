package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;

public class SetBizuteriaWojownikaStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		switch(info.getSetCounts().getOrDefault("Bizuteria_Wojownika", 0)) {
			case 3:
				if(statType == StatTypes.OCHRONA)
					e.increaceStatValue(10);
			case 2:
				if(statType == StatTypes.SILA || statType == StatTypes.WYTRZYMALOSC)
					e.increaceStatValue(4);
				break;
		}
	}
	
}
