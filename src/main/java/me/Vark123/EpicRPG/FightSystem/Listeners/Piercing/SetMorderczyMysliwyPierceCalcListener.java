package me.Vark123.EpicRPG.FightSystem.Listeners.Piercing;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicPierceCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class SetMorderczyMysliwyPierceCalcListener implements Listener {

	@EventHandler
	private void onDodge(EpicPierceCalculateEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Morderczy_Mysliwy", 0) < 3)
			return;
		
		e.addChance(0.01);
	}
	
}
