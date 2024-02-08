package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class VanillaPotionModifierEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		
		if(!(damager instanceof Player))
			return;
		Player p = (Player) damager;
		
		int modifier = 0;
		if(p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			double level = p.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier();
			modifier += (level + 1) * 0.05;
		}
		if(p.hasPotionEffect(PotionEffectType.WEAKNESS)) {
			double level = p.getPotionEffect(PotionEffectType.WEAKNESS).getAmplifier();
			modifier -= (level + 1) * 0.1;
		}
		
		e.increaseModifier(modifier);
		
	}

}
