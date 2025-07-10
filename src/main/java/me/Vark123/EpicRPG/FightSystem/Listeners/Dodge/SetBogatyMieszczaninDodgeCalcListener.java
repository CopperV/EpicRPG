package me.Vark123.EpicRPG.FightSystem.Listeners.Dodge;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDodgeCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class SetBogatyMieszczaninDodgeCalcListener implements Listener {

	@EventHandler
	private void onDodge(EpicDodgeCalculateEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Bogaty_Mieszczanin", 0) < 4)
			return;
		
		e.addChance(0.001);
	}
	
}
