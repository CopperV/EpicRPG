package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import net.md_5.bungee.api.ChatColor;

public class KyraDamageEffectListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Kyra - Wielka Valkyria - Legendary Boss"))
			return;
		
		victim.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 0.6f);
		victim.getWorld().spawnParticle(Particle.CRIT_MAGIC, damager.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
		
		if(victim.getWorld().getName().toLowerCase().contains("heroic"))
			e.setDmg(e.getDmg() * 0.5);
		else
			e.setDmg(e.getDmg() * 0.6);
	}
	
}
