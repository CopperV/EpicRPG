package me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;

public class VanillaPotionEffectListener implements Listener {
	

	@EventHandler(priority = EventPriority.LOW)
	private void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity damager = e.getDamager();
		int modifier = 0;
		if(damager.hasPotionEffect(PotionEffectType.STRENGTH)) {
			double level = damager.getPotionEffect(PotionEffectType.STRENGTH).getAmplifier();
			modifier += (level + 1) * 0.05;
		}
		if(damager.hasPotionEffect(PotionEffectType.WEAKNESS)) {
			double level = damager.getPotionEffect(PotionEffectType.WEAKNESS).getAmplifier();
			modifier -= (level + 1) * 0.1;
		}
		
		e.increaseModifier(modifier);
	}

}
