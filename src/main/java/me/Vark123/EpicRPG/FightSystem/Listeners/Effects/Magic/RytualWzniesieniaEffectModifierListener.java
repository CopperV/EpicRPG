package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

public class RytualWzniesieniaEffectModifierListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MAGIC))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(!modifiers.hasRytualWzniesienia())
			return;
		
		double restoreHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.03;
		RpgPlayerHealEvent healEvent = new RpgPlayerHealEvent(rpg, restoreHp);
		Bukkit.getPluginManager().callEvent(healEvent);
		p.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, p.getLocation().add(0,1,0), 12, 0.4F, 0.4F, 0.4F, 0.05f);
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, .7f);
	}

}
