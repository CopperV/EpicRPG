package me.Vark123.EpicRPG.FightSystem.Listeners.Randomize;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageRandomizeEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class RuneRandomizeCalcListener implements Listener {

	@EventHandler
	private void onCalc(EpicDamageRandomizeEvent e) {
		Player player = e.getRpgPlayer().getPlayer();
		if(Utils.hasEntityBuff(player, EpicModifierTypes.SZAL_PRZEDWIECZNYCH))
			e.increaseModifier(0.2);
		if(Utils.hasEntityBuff(player, EpicModifierTypes.SZAL_PRZEDWIECZNYCH_H))
			e.increaseModifier(0.25);
		if(Utils.hasEntityBuff(player, EpicModifierTypes.SZAL_PRZEDWIECZNYCH_M))
			e.increaseModifier(0.3);
	}
	
}
