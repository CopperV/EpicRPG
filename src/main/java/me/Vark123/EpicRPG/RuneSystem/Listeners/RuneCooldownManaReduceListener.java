package me.Vark123.EpicRPG.RuneSystem.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastRuneCdCalcEvent;
import me.Vark123.EpicRPG.Utils.Utils;

public class RuneCooldownManaReduceListener implements Listener {
	
	@EventHandler
	public void calc(RuneCastRuneCdCalcEvent e) {
		double mana = e.getRpgPlayer().getStats().getFinalMana();
		
		double maxMana = 3000;
		double maxReduce = 0.5;
		
		mana = Utils.limitValue(0, maxMana, mana);
		
		double reduce = Utils.scaleValue(0, maxMana, 0, maxReduce, mana);
		e.decreaseModifier(reduce);
	}
	
}
