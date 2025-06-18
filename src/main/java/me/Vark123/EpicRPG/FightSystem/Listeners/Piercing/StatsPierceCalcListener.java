package me.Vark123.EpicRPG.FightSystem.Listeners.Piercing;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicPierceCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class StatsPierceCalcListener implements Listener {
	
	@EventHandler
	public void onCalc(EpicPierceCalculateEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		RpgStats stats = rpg.getStats();
		
		e.addChance(stats.getFinalZrecznosc() * 0.0004);
	}

}
