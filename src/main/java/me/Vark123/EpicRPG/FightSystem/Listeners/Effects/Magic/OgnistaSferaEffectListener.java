package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.ManualDamage;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class OgnistaSferaEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(!modifiers.hasOgnistaStrzala())
			return;
		
		double dmg = e.getDmg() * 0.25;
		if(modifiers.hasInkantacja())
			dmg *= 1.3;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(victim, damager, DamageCause.CONTACT, dmg);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
			ManualDamage.doDamage((Player) victim, (LivingEntity) damager, dmg, event);
			victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
		}
	}

}
