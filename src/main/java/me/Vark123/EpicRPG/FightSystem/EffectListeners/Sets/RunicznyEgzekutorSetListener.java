package me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class RunicznyEgzekutorSetListener implements Listener {

	@EventHandler
	public void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;
		
		Entity victim = e.getVictim();
		if(!MythicBukkit.inst().getMobManager().isActiveMob(victim.getUniqueId()))
			return;

		ActiveMob aMob = MythicBukkit.inst().getMobManager().getActiveMob(victim.getUniqueId()).get();
		if(aMob.hasThreatTable()) {
			if(!aMob.getThreatTable().getTopThreatHolder().getBukkitEntity().getUniqueId().equals(player.getUniqueId()))
				return;
		} else {
			if(aMob.getNewTarget() == null ||
					!aMob.getNewTarget().getBukkitEntity().getUniqueId().equals(player.getUniqueId()))
				return;
		}
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor", 0) >= 4)
			e.increaseModifier(0.15);
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_H", 0) >= 4)
			e.increaseModifier(0.2);
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_M", 0) >= 4)
			e.increaseModifier(0.25);
	}
	
	@EventHandler
	public void onDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player player))
			return;

		Entity damager = e.getDamager();
		if(damager == null)
			return;
		
		if(!MythicBukkit.inst().getMobManager().isActiveMob(damager.getUniqueId()))
			return;

		ActiveMob aMob = MythicBukkit.inst().getMobManager().getActiveMob(damager.getUniqueId()).get();
		if(aMob.hasThreatTable()) {
			if(!aMob.getThreatTable().getTopThreatHolder().getBukkitEntity().getUniqueId().equals(player.getUniqueId()))
				return;
		} else {
			if(aMob.getNewTarget() == null ||
					!aMob.getNewTarget().getBukkitEntity().getUniqueId().equals(player.getUniqueId()))
				return;
		}
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor", 0) >= 3)
			e.decreaseModifier(0.15);
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_H", 0) >= 3)
			e.decreaseModifier(0.2);
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_M", 0) >= 3)
			e.decreaseModifier(0.25);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamageEffectToHeal(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getDamager();
		if(!(victim instanceof Player player))
			return;
		
		if(e.getDamager() == null)
			return;
		
		Random rand = new Random();
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor", 0) >= 2 &&
				rand.nextDouble() < 0.01) {
			double hpToRestore = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.15;
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hpToRestore);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()) {
				player.spawnParticle(Particle.HEART, player.getLocation().clone().add(0,1,0), 16, 0.5f, 0.8f, 0.5f, 0.1f);
				player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.44f);
			}
			
		}
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_H", 0) >= 2 &&
				rand.nextDouble() < 0.015) {
			double hpToRestore = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.17;
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hpToRestore);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()) {
				player.spawnParticle(Particle.HEART, player.getLocation().clone().add(0,1,0), 16, 0.5f, 0.8f, 0.5f, 0.1f);
				player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.44f);
			}
			
		}
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_M", 0) >= 2 &&
				rand.nextDouble() < 0.02) {
			double hpToRestore = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.2;
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hpToRestore);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()) {
				player.spawnParticle(Particle.HEART, player.getLocation().clone().add(0,1,0), 16, 0.5f, 0.8f, 0.5f, 0.1f);
				player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.44f);
			}
			
		}
	}
	
}
