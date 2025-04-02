package me.Vark123.EpicRPG.RuneSystem.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastGlobalCdCalcEvent;
import me.Vark123.EpicRPG.Utils.Utils;

public class RuneGlobalCooldownInteligencjaReduceListener implements Listener {
	
	@EventHandler
	public void calc(RuneCastGlobalCdCalcEvent e) {
		double inteligencja = e.getRpgPlayer().getStats().getFinalInteligencja();
		
		double maxInteligencja = 1500;
		double maxReduce = 0.75;
		
		inteligencja = Utils.limitValue(0, maxInteligencja, inteligencja);
		
		double reduce = Utils.scaleValue(0, maxInteligencja, 0, maxReduce, inteligencja);
		e.decreaseModifier(reduce);
	}
	
}
