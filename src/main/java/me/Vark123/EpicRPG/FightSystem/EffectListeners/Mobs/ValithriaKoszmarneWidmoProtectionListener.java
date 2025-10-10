package me.Vark123.EpicRPG.FightSystem.EffectListeners.Mobs;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;

public class ValithriaKoszmarneWidmoProtectionListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	private void onDamage(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getDamageType().equals(DamageType.MAGIC))
			return;
		
		Entity victim = e.getVictim();
		
		AbstractEntity aVictim = BukkitAdapter.adapt(victim);
		if(!MythicBukkit.inst().getMobManager().isActiveMob(aVictim))
			return;
		
		if(!MythicBukkit.inst().getMobManager().getMythicMobInstance(aVictim)
				.getMobType().equals("Valithria_MSummon_6"))
			return;

		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_GHAST_SCREAM, 0.4f, 0.6f);
		victim.getWorld().spawnParticle(Particle.ENCHANTED_HIT, victim.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
		
		e.setCancelled(true);
	}
	
}
