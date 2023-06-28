package me.Vark123.EpicRPG.RuneSystem.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.Runes.Prowokacja;

public class ProwokacjaChangeTargetEvent implements Listener {

	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if(e.isCancelled())
			return;
		
		Entity entity = e.getEntity();
		if(!Prowokacja.getTargets().containsKey(entity))
			return;
		
		Player oldTarget = Prowokacja.getTargets().get(entity);
		Entity newTarget = e.getTarget();
		if(oldTarget.equals(newTarget))
			return;
		
		if(!PlayerManager.getInstance().playerExists(oldTarget))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(oldTarget);
		RpgModifiers modifiers = rpg.getModifiers();
		if(!(modifiers.hasZryw()
				|| modifiers.hasProwokacja()))
			return;
		
		e.setCancelled(true);
	}

}
