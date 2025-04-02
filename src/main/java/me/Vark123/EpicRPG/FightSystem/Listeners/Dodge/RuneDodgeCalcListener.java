package me.Vark123.EpicRPG.FightSystem.Listeners.Dodge;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDodgeCalculateEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;

public class RuneDodgeCalcListener implements Listener {

	@EventHandler
	private void onDodge(EpicDodgeCalculateEvent e) {
		RpgPlayer rpg = e.getRpgPlayer();
		RpgModifiers modifiers = rpg.getModifiers();
		
		double modifier = 0;
		
		if(modifiers.hasActiveModifier(EpicModifierTypes.WTOPIENIE))
			modifier += 0.08;
		if(modifiers.hasActiveModifier(EpicModifierTypes.WTOPIENIE_H))
			modifier += 0.1;
		if(modifiers.hasActiveModifier(EpicModifierTypes.WTOPIENIE_M))
			modifier += 0.125;
		
		e.addChance(modifier);
	}
	
}
