package me.Vark123.EpicRPG.BlackrockSystem.Events;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;

public class BlackrockHealDebuffEvent implements Listener {

	@EventHandler
	public void onHeal(RpgPlayerHealEvent e) {
		if(e.isCancelled()) {
			return;
		}
		
		Player p = e.getP();
		if(!p.getWorld().getName().equalsIgnoreCase("blackrock"))
			return;
		p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SKELETON_STEP, 1, 1);
		e.setHeal(e.getHealAmount()*0.5);
	}
	
}
