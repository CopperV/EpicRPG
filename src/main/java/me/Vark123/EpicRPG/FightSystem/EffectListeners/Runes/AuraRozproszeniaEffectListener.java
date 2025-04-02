package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class AuraRozproszeniaEffectListener implements Listener {
	
	@EventHandler
	public void onMod(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		if(!Utils.hasEntityBuff(e.getVictim(), EpicModifierTypes.AURA_ROZPROSZENIA))
			return;
		
		e.decreaseModifier(0.15);
	}

}
