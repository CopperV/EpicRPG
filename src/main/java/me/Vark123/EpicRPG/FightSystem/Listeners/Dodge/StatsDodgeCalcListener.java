package me.Vark123.EpicRPG.FightSystem.Listeners.Dodge;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDodgeCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class StatsDodgeCalcListener implements Listener {

	@EventHandler
	private void onDodge(EpicDodgeCalculateEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		RpgStats stats = rpg.getStats();
		
		double chance = ((stats.getFinalZdolnosciMysliwskie()+stats.getFinalZrecznosc())/65.) * 0.01;
		e.addChance(chance);
	}
	
}
