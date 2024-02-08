package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Defense;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class PvPListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player && victim instanceof Player))
			return;
		
		double modifier = e.getCalculatedDamage().getValue() ? 0.35 : 0.1;
		e.setDmg(e.getDmg() * modifier);
	}

}
