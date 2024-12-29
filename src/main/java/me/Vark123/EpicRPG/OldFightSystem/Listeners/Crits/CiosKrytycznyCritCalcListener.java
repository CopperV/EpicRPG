package me.Vark123.EpicRPG.OldFightSystem.Listeners.Crits;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.OldFightSystem.Events.CritCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;

public class CiosKrytycznyCritCalcListener implements Listener {

	@EventHandler
	public void onCalc(CritCalculateEvent e) {
		RpgPlayer rpg = e.getRpg();
		RpgSkills skills = rpg.getSkills();
		if(!skills.hasCiosKrytyczny())
			return;
		e.addChance(25);
	}
	
}
