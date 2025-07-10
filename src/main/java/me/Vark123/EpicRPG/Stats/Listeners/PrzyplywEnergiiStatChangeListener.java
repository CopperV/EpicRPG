package me.Vark123.EpicRPG.Stats.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Stats.StatChangeEvent;
import me.Vark123.EpicRPG.Stats.StatTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class PrzyplywEnergiiStatChangeListener implements Listener {

	@EventHandler
	public void onChange(StatChangeEvent e) {
		var statType = e.getStat();
		if(statType != StatTypes.INTELIGENCJA && statType != StatTypes.MANA)
			return;
		
		Player player = e.getRpgPlayer().getPlayer();
		if(Utils.hasEntityBuff(player, EpicModifierTypes.PRZYPLYW_ENERGII))
			e.increaceStatMultiplier(0.2);
		if(Utils.hasEntityBuff(player, EpicModifierTypes.PRZYPLYW_ENERGII_H))
			e.increaceStatMultiplier(0.23);
		if(Utils.hasEntityBuff(player, EpicModifierTypes.PRZYPLYW_ENERGII_M))
			e.increaceStatMultiplier(0.27);
	}
	
}
