package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class GruboskornoscEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity victim = e.getVictim();
		if(victim == null)
			return;
		
		if(!Utils.hasEntityBuff(victim, EpicModifierTypes.GRUBOSKORNOSC))
			return;
		
		e.decreaseModifier(0.3);
	}

}
