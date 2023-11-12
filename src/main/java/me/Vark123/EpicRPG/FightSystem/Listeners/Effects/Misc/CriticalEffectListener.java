package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class CriticalEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		boolean crit = e.getCalculatedDamage().getValue();
		if(!crit)
			return;
		
		Location loc = p.getLocation();
        p.getWorld().spawnParticle(Particle.FLAME, loc, 8, 0.5, 1.5, 0.5, 0.1);
        p.playSound(loc, Sound.ENTITY_PLAYER_ATTACK_CRIT, 2, 0.2f);
	}

}
