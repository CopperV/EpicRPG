package me.Vark123.EpicRPG.FightSystem.Listeners.Piercing;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicPierceCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class ProfPierceCalcListener implements Listener {
	
	@EventHandler
	public void onCalc(EpicPierceCalculateEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		RpgPlayerInfo info = rpg.getInfo();
		if(!info.getShortProf().toLowerCase().contains("mys"))
			return;
		
		e.addChance(0.02);
	}

}
