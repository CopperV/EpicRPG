package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Melee;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class WampiryzmEffectModifierListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MELEE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(modifiers.hasWampiryzm()) {
			double restoreHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.015;
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
			Bukkit.getPluginManager().callEvent(event);
			p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 5, 0.5F, 0.5F, 0.5F, 0.1f);
		}
		if(modifiers.hasWampiryzm_h()) {
			double restoreHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.02;
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
			Bukkit.getPluginManager().callEvent(event);
			p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 5, 0.5F, 0.5F, 0.5F, 0.1f);
		}
		if(modifiers.hasWampiryzm_m()) {
			double restoreHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.03;
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
			Bukkit.getPluginManager().callEvent(event);
			p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 5, 0.5F, 0.5F, 0.5F, 0.1f);
		}
	}

}
