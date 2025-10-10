package me.Vark123.EpicRPG.FightSystem.EffectListeners.Mobs;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import net.md_5.bungee.api.ChatColor;

public class EsAlareMeModifierListener implements Listener {

	@EventHandler
	private void onDamage(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Es Alare'Me - Praelfi General"))
			return;
		
		if(!MythicBukkit.inst().getMobManager().isActiveMob(victim.getUniqueId()))
			return;

		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(victim);
		if(!aMob.getStance().equals("phase2_spear"))
			return;
		
		damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, 0.6f);
		damager.getWorld().spawnParticle(Particle.ENCHANTED_HIT, victim.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
		
		e.setDamage(e.getDamage() * 0.6);
	}
	
}
