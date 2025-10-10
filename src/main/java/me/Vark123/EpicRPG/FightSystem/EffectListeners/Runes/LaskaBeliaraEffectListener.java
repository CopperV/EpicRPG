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

public class LaskaBeliaraEffectListener implements Listener {
	
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
		
		if(!rune.getMagicType().equalsIgnoreCase("mrok"))
			return;
		
		if(Utils.hasEntityBuff(p, EpicModifierTypes.LASKA_BELIARA))
			e.increaseModifier(0.33);
	}

}
