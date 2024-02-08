package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;

public class InvincibleControllerListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getEntity();
		if(victim instanceof Player)
			return;
		
		ActiveMob _mob = MythicBukkit.inst().getMobManager().getMythicMobInstance(victim);
		if(_mob == null)
			return;
		
		MythicMob mob = _mob.getType();
		e.setCancelled(mob.getIsInvincible());
	}
	
}
