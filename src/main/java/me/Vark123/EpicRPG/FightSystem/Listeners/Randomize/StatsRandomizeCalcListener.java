package me.Vark123.EpicRPG.FightSystem.Listeners.Randomize;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageRandomizeEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class StatsRandomizeCalcListener implements Listener {

	@EventHandler
	private void onCalc(EpicDamageRandomizeEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		e.increaseModifier(rpg.getStats().getFinalZrecznosc() / 35. * 0.01);
	}
	
}
