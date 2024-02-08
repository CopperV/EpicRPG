package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import net.md_5.bungee.api.ChatColor;

public class AnubRekhanDamageEffectListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(damager == null)
			return;
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Anub'Rekhan"))
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

		if(damager instanceof AbstractArrow)
			damager = (Entity) ((AbstractArrow)damager).getShooter();
		
		damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 0.8f);
		damager.getWorld().spawnParticle(Particle.CRIT_MAGIC, damager.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
		
		e.setDmg(e.getDmg() * 0.1);
	}
	
}
