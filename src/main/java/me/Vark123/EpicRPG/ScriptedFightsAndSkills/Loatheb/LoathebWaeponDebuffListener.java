package me.Vark123.EpicRPG.ScriptedFightsAndSkills.Loatheb;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class LoathebWaeponDebuffListener implements Listener {

	@EventHandler(priority=EventPriority.LOW)
	public void onHeal(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MELEE))
			return;
		
		Entity victim = e.getVictim();
		if(!victim.getName().equals("§2§l§oLoatheb"))
			return;

		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_SLIME_HURT, 1.5f, 0.8f);
		victim.getWorld().spawnParticle(Particle.SLIME, victim.getLocation().add(0,1,0), 8, .7f, .7f, .7f, 0.15f);
		e.decreaseModifier(0.5);
	}
	
}
