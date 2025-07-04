package me.Vark123.EpicRPG.OldFightSystem.Listeners.Defense;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.OldFightSystem.Events.EpicDefenseEvent;
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
//		if(modifiers.hasGruboskornosc())
//			modifier += 0.1 * rpg.getStats().getKrag();
//		if(modifiers.hasTotemObronny())
//			modifier += 0.25;
//		if(modifiers.hasAuraRozproszenia())
//			modifier += 0.15;
//		if(modifiers.hasZyciodajnaZiemia())
//			modifier += 0.15;
//		if(modifiers.hasZyciodajnaZiemia_m())
//			modifier += 0.2;
//		if(modifiers.hasTajemnyBlask_m() && rpg.getInfo().getProffesion().equals("§cWojownik")) {
//			modifier += 0.5;
//		} else if(modifiers.hasTajemnyBlask() && rpg.getInfo().getProffesion().equals("§cWojownik")) {
//			modifier += 0.3;
//		}
//		if(modifiers.hasLodowaTarcza())
//			modifier += 0.18;
//		if(modifiers.hasLodowaTarcza_h())
//			modifier += 0.21;
//		if(modifiers.hasLodowaTarcza_m())
//			modifier += 0.25;
		
		e.decreaseModifier(modifier);
	}

}
