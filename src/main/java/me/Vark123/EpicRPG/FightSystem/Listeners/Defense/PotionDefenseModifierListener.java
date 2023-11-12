package me.Vark123.EpicRPG.FightSystem.Listeners.Defense;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class PotionDefenseModifierListener implements Listener {
	
	@EventHandler
	public void onMod(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		double modifier = 0;
		switch(modifiers.getPotionWytrzymalosc()) {
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
		
		e.decreaseModifier(modifier);
	}

}
