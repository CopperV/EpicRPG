package me.Vark123.EpicRPG.RuneSystem.Listeners;

import java.util.Date;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.papermc.paper.event.entity.EntityMoveEvent;

public class RuneStunEffectListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onMove(EntityMoveEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getFrom().distanceSquared(e.getTo()) < 0.01f)
			return;
		
		Entity entity = e.getEntity();
		AbstractEntity ae = BukkitAdapter.adapt(entity);

		long time = new Date().getTime();
		if(!ae.hasMetadata("epic_stun") || ((long) ae.getMetadata("epic_stun").get()) < time)
			return;

		e.setCancelled(true);
	}


	@EventHandler(priority = EventPriority.LOW)
	public void onMove(PlayerMoveEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getFrom().distanceSquared(e.getTo()) < 0.001f)
			return;
		
		Entity entity = e.getPlayer();
		AbstractEntity ae = BukkitAdapter.adapt(entity);

		long time = new Date().getTime();
		if(!ae.hasMetadata("epic_stun") || ((long) ae.getMetadata("epic_stun").get()) < time)
			return;

		e.setCancelled(true);
	}
}
