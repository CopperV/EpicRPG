package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.Utils.Utils;

public class SilaRownowagiEffectListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MAGIC))
			return;
		
		Object[] args = e.getArgs();
		if(args == null 
				|| args.length < 2
				|| !(args[1] instanceof EpicRune))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;

		EpicRune rune = (EpicRune) args[1];
		
		Player p = (Player) damager;
		
		boolean isBalanceType = rune.getMagicType().equalsIgnoreCase("rownowaga");
		
		if(Utils.hasEntityBuff(p, EpicModifierTypes.SILA_ROWNOWAGI))
			e.increaseModifier(isBalanceType ? 0.25 : 0.16);
		if(Utils.hasEntityBuff(p, EpicModifierTypes.SILA_ROWNOWAGI_H))
			e.increaseModifier(isBalanceType ? 0.29 : 0.2);
		if(Utils.hasEntityBuff(p, EpicModifierTypes.SILA_ROWNOWAGI_M))
			e.increaseModifier(isBalanceType ? 0.35 : 0.25);
	}

}
