package me.Vark123.EpicRPG.FightSystem.Listeners.Defense;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class RuneDefenseModifierListener implements Listener {
	
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
		if(modifiers.hasGruboskornosc())
			modifier += 0.1 * rpg.getStats().getKrag();
		if(modifiers.hasTotemObronny())
			modifier += 0.25;
		if(modifiers.hasAuraRozproszenia())
			modifier += 0.15;
		if(modifiers.hasZyciodajnaZiemia())
			modifier += 0.15;
		if(modifiers.hasZyciodajnaZiemia_m())
			modifier += 0.2;
		
		e.decreaseModifier(modifier);
	}

}
