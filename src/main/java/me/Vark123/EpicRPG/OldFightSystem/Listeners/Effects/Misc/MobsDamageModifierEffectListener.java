package me.Vark123.EpicRPG.OldFightSystem.Listeners.Effects.Misc;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.OldFightSystem.EpicDamageType;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicEffectEvent;

public class MobsDamageModifierEffectListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		
		if(!MythicBukkit.inst().getMobManager().isActiveMob(victim.getUniqueId()))
			return;
	
		switch(victim.getName()) {
			case "§7§oZbrojny szkielet":
				e.setDmg(e.getDmg()*0.8);
				damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8f, 0.8f);
				damager.getWorld().spawnParticle(Particle.CRIT, victim.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
				break;
			case "§8§lStarozytny szkielet-wojownik":
				if(e.getDamageType().equals(EpicDamageType.MELEE) || e.getDamageType().equals(EpicDamageType.PROJECTILE)) {
					e.setDmg(e.getDmg()*0.8);
					damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8f, 0.8f);
					damager.getWorld().spawnParticle(Particle.CRIT, victim.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
					break;
				}
				break;
			case "§7§oGargulec lodowej korony":
				e.setDmg(e.getDmg()*0.8);
				damager.getWorld().playSound(damager.getLocation(), Sound.BLOCK_STONE_BREAK, 1.3f, 0.6f);
				damager.getWorld().spawnParticle(Particle.CRIT, victim.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
				break;
		}
	}

}
