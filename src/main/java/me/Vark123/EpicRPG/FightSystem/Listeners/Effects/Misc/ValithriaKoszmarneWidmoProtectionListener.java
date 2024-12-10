package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import net.md_5.bungee.api.ChatColor;

public class ValithriaKoszmarneWidmoProtectionListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void runeResistance(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getDamageType().equals(EpicDamageType.MAGIC))
			return;
		
		Entity victim = e.getVictim();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Upiorne Widmo"))
			return;
		
		AbstractEntity aVictim = BukkitAdapter.adapt(victim);
		if(!MythicBukkit.inst().getMobManager().isActiveMob(aVictim))
			return;
		
		if(!MythicBukkit.inst().getMobManager().getMythicMobInstance(aVictim)
				.getMobType().equals("Valithria_MSummon_6"))
			return;

		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_GHAST_SCREAM, 0.4f, 0.6f);
		victim.getWorld().spawnParticle(Particle.CRIT_MAGIC, victim.getLocation().add(0,1.2,0), 12, .7f, .7f, .7f, .05f);
		
		e.setCancelled(true);
	}

}
