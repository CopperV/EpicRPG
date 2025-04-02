package me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;

public class DodgeEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onDodge(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		
		if(!DamageUtils.tryDodge(PlayerManager.getInstance().getRpgPlayer(p)))
			return;
		
		e.setCancelled(true);
		
		Location loc = p.getLocation().clone().add(0,1,0);
		p.getWorld().spawnParticle(Particle.WHITE_SMOKE, loc, 13,
				.4, .8, .4, .04);
		p.playSound(p,Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.3f, 0.4f);
	}

}
