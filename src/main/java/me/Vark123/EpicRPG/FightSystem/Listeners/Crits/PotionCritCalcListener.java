package me.Vark123.EpicRPG.FightSystem.Listeners.Crits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicCritCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class PotionCritCalcListener implements Listener {
	
	@EventHandler
	public void onCalc(EpicCritCalculateEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		RpgModifiers modifiers = rpg.getModifiers();
		switch(modifiers.getPotionWalka()) {
			case 1:
				e.addChance(0.075);
				break;
			case 2:
				e.addChance(0.15);
				break;
			case 3:
				e.addChance(0.25);
				break;
		}
		
	}

}
