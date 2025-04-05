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
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public class WyssanieEffectListener implements Listener {
	
	@EventHandler
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MAGIC))
			return;
		
		Object[] args = e.getArgs();
		if(args == null 
				|| args.length < 2
				|| !(args[1] instanceof EpicRune))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;

		EpicRune rune = (EpicRune) args[1];
		if(!rune.getMythicType().equals("Wyssanie"))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		double healAmount = e.getFinalDamage() * 0.02;
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, healAmount);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CAT_HISS, 1, 0.6f);
		p.getWorld().spawnParticle(Particle.HEART, p.getLocation().clone().add(0, 1, 0), 14,
				0.4, 0.8, 0.4, 0.1);
		p.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, e.getVictim().getLocation().clone().add(0, 1, 0), 14,
				0.4, 0.4, 0.4, 0.1);
	}

}
