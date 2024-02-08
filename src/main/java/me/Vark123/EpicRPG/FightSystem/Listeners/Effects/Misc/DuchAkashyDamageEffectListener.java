package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import net.md_5.bungee.api.ChatColor;

public class DuchAkashyDamageEffectListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Duch Akashy"))
			return;
		
		victim.getWorld().playSound(damager.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 0.7f);
		victim.getWorld().spawnParticle(Particle.CRIT_MAGIC, damager.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
		
		e.setDmg(e.getDmg() * 0.7);
	}
	
}
