package me.Vark123.EpicRPG.FightSystem.Listeners.Crits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.CritCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class StatsCritCalcListener implements Listener {

	@EventHandler
	public void onCalc(CritCalculateEvent e) {
		RpgPlayer rpg = e.getRpg();
		RpgStats stats = rpg.getStats();
		int chance = stats.getFinalWalka();
		e.addChance(chance);
	}
	
}
