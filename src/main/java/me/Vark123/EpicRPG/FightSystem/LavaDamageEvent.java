package me.Vark123.EpicRPG.FightSystem;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.Main;

public class LavaDamageEvent implements Listener {

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		Entity victim = e.getEntity();
		if(!(victim instanceof Player))
			return;
		DamageCause cause = e.getCause();
		if(!cause.equals(DamageCause.LAVA))
			return;
		Player p = (Player) victim;
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()->{
			if(victim.isDead())
				return;
			p.setNoDamageTicks(0);
		}, 1);
		e.setDamage(0.01 * p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	}

}
