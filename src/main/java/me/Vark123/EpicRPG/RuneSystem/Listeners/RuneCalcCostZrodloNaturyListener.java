package me.Vark123.EpicRPG.RuneSystem.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastCostCalcEvent;
import me.Vark123.EpicRPG.Utils.Utils;

public class RuneCalcCostZrodloNaturyListener implements Listener {

	@EventHandler
	private void onCalc(RuneCastCostCalcEvent e) {
		if(e.isCancelled())
			return;
		
		Player player = e.getRpgPlayer().getPlayer();
		if(!Utils.hasEntityBuff(player, EpicModifierTypes.ZRODLO_NATURY))
			return;
		
		if(e.getRune().getMythicType().equals("ZrodloNatury"))
			return;
		
		e.decreaseModifier(0.2);
	}
	
}
