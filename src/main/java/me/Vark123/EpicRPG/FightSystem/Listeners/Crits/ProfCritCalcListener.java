package me.Vark123.EpicRPG.FightSystem.Listeners.Crits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Config;
import me.Vark123.EpicRPG.FightSystem.Events.EpicCritCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Utils.Utils;

public class ProfCritCalcListener implements Listener {
	
	@EventHandler
	public void onCalc(EpicCritCalculateEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		RpgPlayerInfo info = rpg.getInfo();
		if(!info.getShortProf().toLowerCase().contains("mys"))
			return;
		
		double walka = 50;
		double maxWalka = Config.get().getMaxWalkaCrit();
		
		double chance = Utils.scaleValue(0, maxWalka, 0, 1, walka);
		e.addChance(chance);
	}

}
