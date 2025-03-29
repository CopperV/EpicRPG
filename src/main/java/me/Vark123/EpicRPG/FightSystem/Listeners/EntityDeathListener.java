package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDeathEvent;

public class EntityDeathListener implements Listener {

	@EventHandler
	public void onMobDeath(MythicMobDeathEvent e) {
		Entity victim = e.getEntity();
		LivingEntity killer = e.getKiller();
		if(!(victim instanceof LivingEntity))
			return;
		
		EpicDeathEvent event = new EpicDeathEvent(killer, (LivingEntity) victim);
		Bukkit.getPluginManager().callEvent(event);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player victim = e.getEntity();
		Entity killer = victim.getKiller();
		if(!(killer instanceof LivingEntity))
			return;
		
		EpicDeathEvent event = new EpicDeathEvent((LivingEntity) killer, victim);
		Bukkit.getPluginManager().callEvent(event);
	}
	
}
