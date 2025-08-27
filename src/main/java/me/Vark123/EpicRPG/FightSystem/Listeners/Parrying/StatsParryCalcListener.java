package me.Vark123.EpicRPG.FightSystem.Listeners.Parrying;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicParryCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class StatsParryCalcListener implements Listener {
	
	@EventHandler
	public void onCalc(EpicParryCalculateEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		RpgStats stats = rpg.getStats();
		
		e.addChance((stats.getFinalSila() + stats.getFinalWytrzymalosc() * 1.5) * 0.0003);
	}

}
