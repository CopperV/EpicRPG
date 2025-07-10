package me.Vark123.EpicRPG.RuneSystem.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastRuneCdCalcEvent;

public class RuneCooldownSetMlodyAdeptReduceListener implements Listener {
	
	@EventHandler
	public void calc(RuneCastRuneCdCalcEvent e) {
		RpgPlayerInfo info = e.getRpgPlayer().getInfo();
		
		if(info.getSetCounts().getOrDefault("Mlody_Adept", 0) < 4)
			return;
		
		e.decreaseModifier(0.05);
	}
	
}
