package me.Vark123.EpicRPG.FightSystem.Listeners.Crits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.CritCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class ProfCritCalcListener implements Listener {

	@EventHandler
	public void onCalc(CritCalculateEvent e) {
		RpgPlayer rpg = e.getRpg();
		RpgPlayerInfo info = rpg.getInfo();
		if(!info.getShortProf().toLowerCase().contains("mys"))
			return;
		e.addChance(50);
	}
	
}
