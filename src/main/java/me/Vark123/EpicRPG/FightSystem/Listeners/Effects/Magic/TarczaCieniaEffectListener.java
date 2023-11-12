package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class TarczaCieniaEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		RpgStats stats = rpg.getStats();
		
		if(!modifiers.hasTarczaCienia())
			return;
		
		double dmg = e.getDmg();
		int dmgTarczaCienia = (int) (stats.getPresentMana() < dmg/2 ? stats.getPresentMana() : dmg/2);
		if(dmgTarczaCienia <= 0)
			return;
		
		e.setDmg(dmg - dmgTarczaCienia);
		stats.removePresentMana(dmgTarczaCienia);
		
		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.9f, 1.1f);
		victim.getWorld().spawnParticle(Particle.SOUL, victim.getLocation().add(0,1,0), 25, 0.5f, 0.5f, 0.5f, 0.2f);
	}

}
