package me.Vark123.EpicRPG.ScriptedFightsAndSkills.Loatheb;

import java.util.Random;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class LoathebProjectileNeutralizeListener implements Listener {

	@EventHandler(priority=EventPriority.LOW)
	public void onHeal(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.PROJECTILE))
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(!(damager instanceof AbstractArrow))
			return;
		if(!(((AbstractArrow)damager).getShooter() instanceof Player))
			return;
		if(!(victim instanceof LivingEntity))
			return;

		if(!victim.getName().equals("§2§l§oLoatheb")) 
			return;
		if(new Random().nextInt(5) != 0)
			return;

		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1.5f, 0.6f);
		damager.getWorld().spawnParticle(Particle.SLIME, damager.getLocation(), 12, .3f, .3f, .3f, 0.1f);
		damager.remove();
		e.setCancelled(true);
	}
	
}
