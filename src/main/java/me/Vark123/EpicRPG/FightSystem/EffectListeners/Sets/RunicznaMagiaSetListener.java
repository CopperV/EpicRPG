package me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class RunicznaMagiaSetListener implements Listener {
	
	@EventHandler
	public void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Runiczna_Magia", 0) < 3)
			return;
		
		e.increaseModifier(0.05);
	}

	@EventHandler
	public void onDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		if(info.getSetCounts().getOrDefault("Runiczna_Magia", 0) < 4)
			return;
		
		e.decreaseModifier(0.05);
	}
	
}
