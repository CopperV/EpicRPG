package me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Melee;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class PotionMeleeModifierListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MELEE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		boolean crit = e.getCalculatedDamage().getValue();
		double modifier = 0;
		if(crit)
			switch(modifiers.getPotionZrecznosc()) {
				case 1:
					modifier = 0.15;
					break;
				case 2:
					modifier = 0.3;
					break;
				case 3:
					modifier = 0.45;
					break;
			}
		else
			switch(modifiers.getPotionSila()) {
				case 1:
					modifier = 0.1;
					break;
				case 2:
					modifier = 0.25;
					break;
				case 3:
					modifier = 0.4;
					break;
			}
		
		e.increaseModifier(modifier);
	}

}
