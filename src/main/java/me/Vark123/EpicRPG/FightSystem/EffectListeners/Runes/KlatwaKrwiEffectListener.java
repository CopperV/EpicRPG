package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public class KlatwaKrwiEffectListener implements Listener {
	
	private static final DustOptions dust = new DustOptions(Color.fromRGB(250, 0, 0), 1.25f);

	@EventHandler
	public void onMod(EpicAttackEvent e) {
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
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(!rune.getMagicType().equalsIgnoreCase("krew"))
			return;
		if(!modifiers.hasActiveModifier(EpicModifierTypes.KLATWA_KRWI))
			return;
		
		e.increaseModifier(0.3);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
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
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		RpgStats stats = rpg.getStats();
		
		if(!rune.getMagicType().equalsIgnoreCase("krew"))
			return;
		if(!modifiers.hasActiveModifier(EpicModifierTypes.KLATWA_KRWI))
			return;
		
		double value = e.getFinalDamage()*0.001 + 0.01*stats.getFinalMana() + 0.03*stats.getFinalInteligencja();

		RpgPlayerHealEvent healEvent = new RpgPlayerHealEvent(rpg, value);
		Bukkit.getPluginManager().callEvent(healEvent);
		if(!healEvent.isCancelled()) {
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.9f, 0.7f);
			p.getWorld().spawnParticle(Particle.DUST, p.getLocation().clone().add(0,1,0), 6,
					0.4, 0.6, 0.4, 0.25, dust);
		}
	}

}
