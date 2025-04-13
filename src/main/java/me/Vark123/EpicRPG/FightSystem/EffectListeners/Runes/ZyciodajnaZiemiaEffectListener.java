package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class ZyciodajnaZiemiaEffectListener implements Listener {
	
	@EventHandler
	public void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity damager = e.getDamager();
		
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.ZYCIODAJNA_ZIEMIA))
			e.increaseModifier(0.15);
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.ZYCIODAJNA_ZIEMIA_M))
			e.increaseModifier(0.24);
		
	}
	
	@EventHandler
	public void onWojownikDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity victim = e.getVictim();
		
		if(Utils.hasEntityBuff(victim, EpicModifierTypes.ZYCIODAJNA_ZIEMIA))
			e.decreaseModifier(0.1);
		if(Utils.hasEntityBuff(victim, EpicModifierTypes.ZYCIODAJNA_ZIEMIA_M))
			e.decreaseModifier(0.15);
		
	}

}
