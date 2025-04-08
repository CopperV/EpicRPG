package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class RytualWzniesieniaEffectListener implements Listener {
	
	@EventHandler
	public void onMagicAttack(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MAGIC))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;

		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		RpgStats stats = rpg.getStats();
		
		if(!modifiers.hasActiveModifier(EpicModifierTypes.RYTUAL_WZNIESIENIA))
			return;
		
		double value = 3 * (stats.getFinalWytrzymalosc()*0.025 + stats.getFinalSila()*0.0175 + stats.getFinalMana()*0.012);
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, value);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		p.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, p.getLocation().clone().add(0,1,0), 12,
				0.4F, 0.8F, 0.4F, 0.05f);
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, .7f);
	}
	
	@EventHandler
	public void onMeleeAttack(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MELEE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;

		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		RpgStats stats = rpg.getStats();
		
		if(!modifiers.hasActiveModifier(EpicModifierTypes.RYTUAL_WZNIESIENIA))
			return;
		
		double value = 5 * (stats.getFinalWytrzymalosc()*0.021 + stats.getFinalSila()*0.015 + stats.getFinalMana()*0.007);
		stats.addPresentManaSmart((int) value);
		
		p.getWorld().spawnParticle(Particle.SOUL, p.getLocation().clone().add(0,1,0), 12, 
				.4f, .8f, .4f, .05f);
		p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
	}

}
