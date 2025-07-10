package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class SetKarczemnyMoczymordaStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		var statType = e.getStat();
		
		if(info.getSetCounts().getOrDefault("Karczemny_Moczymorda", 0) < 2)
			return;

		if(Utils.hasWeapon(e.getRpgPlayer().getPlayer()))
			return;
		
		if(statType == StatTypes.SILA || statType == StatTypes.WYTRZYMALOSC)
			e.increaceStatValue(12);
	}
	
}
