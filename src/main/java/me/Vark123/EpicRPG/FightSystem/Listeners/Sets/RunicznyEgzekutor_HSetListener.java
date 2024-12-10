package me.Vark123.EpicRPG.FightSystem.Listeners.Sets;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class RunicznyEgzekutor_HSetListener implements Listener {

	@EventHandler
	public void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		int amount = info.getSetCounts().getOrDefault("Runiczny_Egzekutor_H", 0);
		if(amount < 4)
			return;
		
		Entity victim = e.getVictim();
		if(!MythicBukkit.inst().getMobManager().isActiveMob(victim.getUniqueId()))
			return;
		
		ActiveMob aMob = MythicBukkit.inst().getMobManager().getActiveMob(victim.getUniqueId()).get();
		if(aMob.hasThreatTable()) {
			if(aMob.getThreatTable().getTopThreatHolder().getBukkitEntity().getUniqueId().equals(damager.getUniqueId())) {
				e.increaseModifier(0.2);
			}
		} else {
			if(aMob.getNewTarget() != null && aMob.getNewTarget().getUniqueId().equals(damager.getUniqueId())) {
				e.increaseModifier(0.2);
			}
		}
	}
	
	@EventHandler
	public void onDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getDamager();
		if(!(victim instanceof Player))
			return;

		Entity damager = e.getDamager();
		if(damager == null)
			return;
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(damager instanceof Player)
			return;

		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		int amount = info.getSetCounts().getOrDefault("Runiczny_Egzekutor_H", 0);
		if(amount < 3)
			return;
		
		if(!MythicBukkit.inst().getMobManager().isActiveMob(damager.getUniqueId()))
			return;
		
		ActiveMob aMob = MythicBukkit.inst().getMobManager().getActiveMob(damager.getUniqueId()).get();
		if(aMob.hasThreatTable()) {
			if(aMob.getThreatTable().getTopThreatHolder().getBukkitEntity().getUniqueId().equals(victim.getUniqueId())) {
				e.decreaseModifier(0.2);
			}
		} else {
			if(aMob.getNewTarget() != null && aMob.getNewTarget().getUniqueId().equals(victim.getUniqueId())) {
				e.decreaseModifier(0.2);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamageEffectToHeal(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getDamager();
		if(!(victim instanceof Player))
			return;

		Entity damager = e.getDamager();
		if(damager == null)
			return;
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(damager instanceof Player)
			return;

		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		int amount = info.getSetCounts().getOrDefault("Runiczny_Egzekutor_H", 0);
		if(amount < 2)
			return;
		
		if(!MythicBukkit.inst().getMobManager().isActiveMob(damager.getUniqueId()))
			return;
		
		Random rand = new Random();
		if(rand.nextDouble() > 0.015)
			return;
		
		double hpToRestore = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.17;
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hpToRestore);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		p.spawnParticle(Particle.HEART, p.getLocation().clone().add(0,1,0), 16, 0.5f, 0.8f, 0.5f, 0.1f);
		p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.44f);
	}
	
}
