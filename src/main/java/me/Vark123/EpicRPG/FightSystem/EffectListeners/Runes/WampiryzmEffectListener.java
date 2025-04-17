package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDeathEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Utils.Utils;

public class WampiryzmEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MELEE))
			return;
		
		LivingEntity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;
		
		double modifier = 0;
		if(Utils.hasEntityBuff(player, EpicModifierTypes.WAMPIRYZM))
			modifier = 1;
		else if(Utils.hasEntityBuff(player, EpicModifierTypes.WAMPIRYZM_H))
			modifier = 1.1;
		else if(Utils.hasEntityBuff(player, EpicModifierTypes.WAMPIRYZM_M))
			modifier = 1.25;
		
		if(modifier <= 0)
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgStats stats = rpg.getStats();
		double damage = e.getFinalDamage();
		
		double value = modifier * (0.001*damage + 0.01*stats.getFinalSila() + 0.025*stats.getFinalWytrzymalosc());
		
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, value);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, 0.9f, 0.7f);
			player.getWorld().spawnParticle(Particle.HEART, player.getLocation().clone().add(0,1,0), 9,
					0.4, 0.6, 0.4, 0.1);
		}
	}
	
	@EventHandler
	public void onDeath(EpicDeathEvent e) {
		LivingEntity damager = e.getKiller();
		if(!(damager instanceof Player player))
			return;
		
		double modifier = 0;
		if(Utils.hasEntityBuff(player, EpicModifierTypes.WAMPIRYZM))
			modifier = 1;
		else if(Utils.hasEntityBuff(player, EpicModifierTypes.WAMPIRYZM_H))
			modifier = 1.1;
		else if(Utils.hasEntityBuff(player, EpicModifierTypes.WAMPIRYZM_M))
			modifier = 1.25;
		
		if(modifier <= 0)
			return;
		modifier *= 3;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgStats stats = rpg.getStats();
		
		double value = modifier * (0.01*rpg.getInfo().getLevel() + 0.01*stats.getFinalSila() + 0.025*stats.getFinalWytrzymalosc());
		
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, value);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, 0.9f, 0.7f);
			player.getWorld().spawnParticle(Particle.HEART, player.getLocation().clone().add(0,1,0), 9,
					0.4, 0.6, 0.4, 0,1);
		}
	}

}
