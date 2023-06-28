package me.Vark123.EpicRPG.RuneSystem.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class WedrownyCienTargetEvent implements Listener {

	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if(e.isCancelled())
			return;
		
		Entity target = e.getTarget();
		if(target == null)
			return;
		
		if(!(target instanceof Player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) target);
		if(!rpg.getModifiers().hasWedrownyCien())
			return;
		
		e.setCancelled(true);
	}
	
}
