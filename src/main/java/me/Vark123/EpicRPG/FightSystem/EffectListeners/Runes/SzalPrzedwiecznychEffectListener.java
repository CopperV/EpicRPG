package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class SzalPrzedwiecznychEffectListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity damager = e.getDamager();
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.SZAL_PRZEDWIECZNYCH))
			e.increaseModifier(0.15);
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.SZAL_PRZEDWIECZNYCH_H))
			e.increaseModifier(0.17);
		if(Utils.hasEntityBuff(damager, EpicModifierTypes.SZAL_PRZEDWIECZNYCH_M))
			e.increaseModifier(0.2);
	}

}
