package me.Vark123.EpicRPG.FightSystem.EffectListeners.Mobs;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import net.md_5.bungee.api.ChatColor;

public class AnubRekhanModifierListener implements Listener {

	@EventHandler
	private void onDamage(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(damager == null)
			return;
		
		String name = ChatColor.stripColor(victim.getName());
		if(!name.startsWith("Anub'Rekhan"))
			return;

		
		Location loc1 = victim.getLocation();
		Location loc2 = damager.getLocation();
		Vector vec1 = loc1.clone().subtract(loc2).toVector().normalize();
		Vector vec2 = loc1.getDirection();
		
		vec1.setY(0).normalize();
		vec2.setY(0).normalize();
		double calcAngle = Math.toDegrees(vec1.angle(vec2)) - 180;
		
		if(Math.abs(calcAngle) < 135)
			return;
		
		damager = e.getDamageSource().getCausingEntity();
		
		damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 0.8f);
		damager.getWorld().spawnParticle(Particle.ENCHANTED_HIT, damager.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
		
		e.setDamage(e.getDamage()*0.1);
	}
	
}
