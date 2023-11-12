package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class DodgeEffectModifierListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		if(!DamageUtils.tryDodge(p))
			return;
		
		e.setCancelled(true);
		Location loc = p.getLocation();
        p.getWorld().spawnParticle(Particle.CLOUD, loc, 15, 0.5, 1.5, 0.5, 0.1);
        p.playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 2, 0.1f);
	}

}
