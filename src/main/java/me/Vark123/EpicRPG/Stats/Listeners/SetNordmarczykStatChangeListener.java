package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class SetNordmarczykStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		Player player = e.getRpgPlayer().getPlayer();
		
		switch(info.getSetCounts().getOrDefault("Nordmarczyk", 0)) {
			case 4:
				if(!Utils.hasWeapon(player)) {
					if(statType == StatTypes.SILA || statType == StatTypes.WYTRZYMALOSC)
						e.increaceStatValue(30);
				}
			case 3:
				if(!Utils.hasWeapon(player)) {
					if(statType == StatTypes.OBRAZENIA)
						e.increaceStatValue(35);
				}
			case 2:
				if(statType == StatTypes.OBRAZENIA)
					e.increaceStatValue(20);
				break;
		}
	}
	
}
