package me.Vark123.EpicRPG.FightSystem.EffectListeners.PostMisc;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Calculators.IDamageCalculator.DamageCalculatorResult;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;

public class CritInfoEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	private void onCrit(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getArgs() == null && e.getArgs().length < 1
				&& !(e.getArgs()[0] instanceof DamageCalculatorResult))
			return;
		
		DamageCalculatorResult damageInfo = (DamageCalculatorResult) e.getArgs()[0];
		if(!damageInfo.isCrit)
			return;
		
		LivingEntity damager = e.getDamager();
		Location loc = damager.getLocation().clone().add(0, 1, 0);
		damager.getWorld().playSound(damager, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.2f, 0.4f);
		loc.getWorld().spawnParticle(Particle.SCRAPE, loc, 20,
				.4, .8, .4, .04);
		loc.getWorld().spawnParticle(Particle.SCRAPE, e.getVictim().getLocation().clone().add(0,1,0), 20,
				.4, .8, .4, .04);
	}

}
