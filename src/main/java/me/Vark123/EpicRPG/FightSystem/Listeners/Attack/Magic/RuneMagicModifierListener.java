package me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Magic;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class RuneMagicModifierListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MAGIC))
			return;
		
		Object[] args = e.getArgs();
		if(args == null 
				|| args.length <= 0
				|| !(args[0] instanceof ItemStackRune))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;

		ItemStackRune ir = (ItemStackRune) args[0];
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		double modifier = 0;
		
		if(modifiers.hasInkantacja() 
				&& ir.getMagicType().equalsIgnoreCase("ogien"))
			modifier += 0.3;
		if(modifiers.hasPoswiecenie() 
				&& ir.getMagicType().equalsIgnoreCase("swiatlo"))
			modifier += 0.6;
		if(modifiers.hasZakazanyRytual() 
				&& ir.getMagicType().equalsIgnoreCase("chaos"))
			modifier += 0.35;
		if(modifiers.hasZakazanyRytual_h() 
				&& ir.getMagicType().equalsIgnoreCase("chaos"))
			modifier += 0.4;
		if(modifiers.hasZakazanyRytual_m() 
				&& ir.getMagicType().equalsIgnoreCase("chaos"))
			modifier += 0.47;
		if(modifiers.hasZewKrwi() 
				&& ir.getMagicType().equalsIgnoreCase("krew"))
			modifier += ((double) modifiers.getZewKrwiMod())/100.0;
		if(modifiers.hasPelnia() 
				&& ir.getMagicType().equalsIgnoreCase("woda"))
			modifier += 0.45;
		if(modifiers.hasSilaRownowagi()) {
			if(ir.getMagicType().equalsIgnoreCase("rownowaga"))
				modifier += 0.35;
			else
				modifier += 0.2;
		}
		if(modifiers.hasSilaRownowagi_h()) {
			if(ir.getMagicType().equalsIgnoreCase("rownowaga"))
				modifier += 0.42;
			else
				modifier += 0.25;
		}
		if(modifiers.hasSilaRownowagi_m()) {
			if(ir.getMagicType().equalsIgnoreCase("rownowaga"))
				modifier += 0.5;
			else
				modifier += 0.33;
		}
		
		e.increaseModifier(modifier);
	}

}
