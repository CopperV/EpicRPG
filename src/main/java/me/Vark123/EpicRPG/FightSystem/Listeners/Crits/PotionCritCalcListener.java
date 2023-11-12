package me.Vark123.EpicRPG.FightSystem.Listeners.Crits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.CritCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class PotionCritCalcListener implements Listener {

	@EventHandler
	public void onCalc(CritCalculateEvent e) {
		RpgPlayer rpg = e.getRpg();
		RpgModifiers modifiers = rpg.getModifiers();
		int chance = 0;
		switch(modifiers.getPotionWalka()) {
			case 1:
				chance = 37;
				break;
			case 2:
				chance = 75;
				break;
			case 3:
				chance = 125;
				break;
		}
		e.addChance(chance);
	}
	
}
