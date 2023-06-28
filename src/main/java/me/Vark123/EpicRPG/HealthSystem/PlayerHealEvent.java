package me.Vark123.EpicRPG.HealthSystem;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerHealEvent implements Listener {

	@EventHandler(priority=EventPriority.MONITOR)
	public void onHeal(RpgPlayerHealEvent e) {
		if(e.isCancelled()) 
			return;
		
		double a = e.getHealAmount();
		if(a <= 0) return;
		
		Player p = e.getP();
		double heal = p.getHealth()+a;
		if(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() < heal) {
			heal = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		}
		p.setHealth(heal);
	}

}
