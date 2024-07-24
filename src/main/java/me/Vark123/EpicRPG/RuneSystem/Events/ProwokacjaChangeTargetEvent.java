package me.Vark123.EpicRPG.RuneSystem.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class ProwokacjaChangeTargetEvent implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onTarget(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity entity = e.getVictim();
		if(entity instanceof Player)
			return;
		
		MythicBukkit.inst().getMobManager().getActiveMob(entity.getUniqueId()).ifPresent(mob -> {
			if(!mob.hasThreatTable())
				return;
		});
	}

}
