package me.Vark123.EpicRPG.FightSystem.EffectListeners.Mobs;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import net.md_5.bungee.api.ChatColor;

public class KyraModifierListener implements Listener {

	@EventHandler
	private void onDamage(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Kyra - Wielka Valkyria - Legendary Boss"))
			return;
		
		victim.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 0.6f);
		victim.getWorld().spawnParticle(Particle.ENCHANTED_HIT, damager.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
		
		if(victim.getWorld().getName().toLowerCase().contains("heroic"))
			e.setDamage(e.getDamage() * 0.5);
		else if(victim.getWorld().getName().toLowerCase().contains("mythic"))
			e.setDamage(e.getDamage() * 0.4);
		else
			e.setDamage(e.getDamage() * 0.6);
	}
	
}
