package me.Vark123.EpicRPG.FightSystem.Listeners.Crits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Config;
import me.Vark123.EpicRPG.FightSystem.Events.EpicCritCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Utils.Utils;

public class StatsCritCalcListener implements Listener {
	
	@EventHandler
	public void onCalc(EpicCritCalculateEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		RpgStats stats = rpg.getStats();
		
		double walka = stats.getFinalWalka();
		double maxWalka = Config.get().getMaxWalkaCrit();
		
		double chance = Utils.scaleValue(0, maxWalka, 0, 1, walka);
		e.addChance(chance);
	}

}
