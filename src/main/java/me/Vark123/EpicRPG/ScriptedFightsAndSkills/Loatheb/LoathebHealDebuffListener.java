package me.Vark123.EpicRPG.ScriptedFightsAndSkills.Loatheb;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;

public class LoathebHealDebuffListener implements Listener {

	@EventHandler(priority=EventPriority.NORMAL)
	public void onHeal(RpgPlayerHealEvent e) {
		if(e.isCancelled())
			return;
		Player p = e.getP();
		if(!p.getWorld().getName().toLowerCase().contains("dungeon12"))
			return;
		p.getNearbyEntities(30, 10, 30)
			.stream()
			.map(Entity::getName)
			.filter(name -> name.equals("§2§l§oLoatheb"))
			.findAny()
			.ifPresent(name -> e.setHeal(e.getHealAmount()*0.6));
	}
	
}
