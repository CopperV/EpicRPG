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

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class LoathebProjectileReflectListener implements Listener {

	@EventHandler(priority=EventPriority.NORMAL)
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

		AbstractEntity aVictim = BukkitAdapter.adapt(victim);
		String phase = MythicBukkit.inst().getMobManager().getMythicMobInstance(aVictim).getStance();
		if(!phase.equalsIgnoreCase("phase2") && !phase.equalsIgnoreCase("phase3"))
			return;
		if(new Random().nextInt(10) != 0)
			return;

		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_SLIME_JUMP, 1.5f, 1.2f);
		damager.getWorld().spawnParticle(Particle.COMPOSTER, damager.getLocation(), 12, .3f, .3f, .3f, 0.1f);
		Entity shooter = (Entity) ((AbstractArrow) damager).getShooter();
		BukkitAdapter.adapt(shooter).damage((float) (e.getFinalDamage() * 0.1));
		damager.remove();
		e.setCancelled(true);
	}
	
}
