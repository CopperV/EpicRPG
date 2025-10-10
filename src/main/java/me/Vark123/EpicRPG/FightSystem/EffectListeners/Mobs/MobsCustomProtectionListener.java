package me.Vark123.EpicRPG.FightSystem.EffectListeners.Mobs;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import net.md_5.bungee.api.ChatColor;

public class MobsCustomProtectionListener implements Listener {

	@EventHandler
	private void onDamage(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;

		Entity damager = e.getDamager();
		if(damager == null)
			return;
		
		Entity victim = e.getVictim();
		if(!MythicBukkit.inst().getMobManager().isActiveMob(victim.getUniqueId()))
			return;
		
		switch(ChatColor.stripColor(victim.getName())) {
			case "Zbrojny szkielet":
				e.setDamage(e.getDamage()*0.8);
				damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8f, 0.8f);
				damager.getWorld().spawnParticle(Particle.CRIT, victim.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
				break;
			case "Starozytny szkielet-wojownik":
				if(e.getDamageType().equals(DamageType.MELEE) || e.getDamageType().equals(DamageType.PROJECTILE)) {
					e.setDamage(e.getDamage()*0.8);
					damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8f, 0.8f);
					damager.getWorld().spawnParticle(Particle.CRIT, victim.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
					break;
				}
				break;
			case "Gargulec lodowej korony":
				e.setDamage(e.getDamage()*0.8);
				damager.getWorld().playSound(damager.getLocation(), Sound.BLOCK_STONE_BREAK, 1.3f, 0.6f);
				damager.getWorld().spawnParticle(Particle.CRIT, victim.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
				break;
		}
	}
}
