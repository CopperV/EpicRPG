package me.Vark123.EpicRPG.FightSystem.Listeners.Death;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDeathEvent;
import me.Vark123.EpicRPG.FightSystem.Events.MagicEntityDamageByEntityEvent;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.Utils.Utils;

public class EntityDeathKrewPrzodkowRuneListener implements Listener {
	
	private static final DustOptions dust = new DustOptions(Color.fromRGB(154, 3, 67), 1.25f);
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicDeathEvent e) {
		LivingEntity victim = e.getVictim();
		EntityDamageEvent lastCause = Utils.getLastDamageCause(victim);
		if(!(lastCause instanceof MagicEntityDamageByEntityEvent event))
			return;
		
		Entity damager = event.getDamager();
		if(!(damager instanceof Player))
			return;

		EpicRune rune = event.getRune();
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		RpgStats stats = rpg.getStats();
		
		if(!rune.getMagicType().equalsIgnoreCase("krew"))
			return;
		if(!modifiers.hasActiveModifier(EpicModifierTypes.KREW_PRZODKOW))
			return;
		
		double value = 0.05*rpg.getInfo().getLevel() + 0.04*stats.getFinalMana() + 0.09*stats.getFinalInteligencja();

		RpgPlayerHealEvent healEvent = new RpgPlayerHealEvent(rpg, value);
		Bukkit.getPluginManager().callEvent(healEvent);
		if(!healEvent.isCancelled()) {
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.9f, 0.7f);
			p.getWorld().spawnParticle(Particle.DUST, p.getLocation().clone().add(0,1,0), 6,
					0.4, 0.6, 0.4, 0.25, dust);
		}
	}

}
