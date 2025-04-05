package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class CiosWPlecyEffectListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MELEE))
			return;
		
		LivingEntity damager = e.getDamager();
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.CIOS_W_PLECY))
			return;
		
		Entity victim = e.getVictim();
		Location loc1 = victim.getLocation();
		Location loc2 = damager.getLocation();
		Vector vec1 = loc1.clone().subtract(loc2).toVector().normalize();
		Vector vec2 = loc1.getDirection();
		
		vec1.setY(0).normalize();
		vec2.setY(0).normalize();
		double calcAngle = Math.toDegrees(vec1.angle(vec2)) - 180;
		if(Math.abs(calcAngle) < 135)
			return;
		
		AbstractEntity ae = BukkitAdapter.adapt(e.getVictim());
		ae.setMetadata("CiosWPlecyEffect", true);
		
		e.increaseModifier(0.4);
	}
	
	@EventHandler
	public void onMod(EpicPostDamageEffectEvent e) {
		Entity victim = e.getVictim();
		AbstractEntity ae = BukkitAdapter.adapt(victim);
		if(!ae.hasMetadata("CiosWPlecyEffect"))
			return;
		
		ae.removeMetadata("CiosWPlecyEffect");
		if(e.isCancelled())
			return;
		
		LivingEntity damager = e.getDamager();
		
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.CIOS_W_PLECY))
			return;
		
		Location loc = victim.getLocation().clone().add(0,1,0);
		loc.getWorld().playSound(loc, Sound.BLOCK_GRINDSTONE_USE, 2, 2);
		loc.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 7,
				.4, .6, .4, .05);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onDamageController(EntityDamageEvent e) {
		AbstractEntity ae = BukkitAdapter.adapt(e.getEntity());
		if(e.isCancelled() && ae.hasMetadata("CiosWPlecyEffect"))
			ae.removeMetadata("CiosWPlecyEffect");
	}

}
