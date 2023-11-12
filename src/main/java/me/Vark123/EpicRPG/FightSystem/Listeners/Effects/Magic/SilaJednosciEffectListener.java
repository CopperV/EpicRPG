package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
import me.Vark123.EpicRPG.RuneSystem.Runes.SilaJednosci;

public class SilaJednosciEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();

		if(!modifiers.hasSilaJednosci() 
				|| !SilaJednosci.getGlobalEffected().containsKey(rpg.getPlayer()))
			return;
		
		SilaJednosci rune = SilaJednosci.getGlobalEffected().get(rpg.getPlayer());
		Player caster = rune.getCaster();
		Location castLoc = rune.getLoc();
		
		if(!caster.isOnline() 
				|| !caster.getWorld().getUID().equals(castLoc.getWorld().getUID())
				|| caster.getLocation().distance(castLoc) > rune.getRune().getObszar() && !caster.equals(p))
			return;
		
		double dmg = e.getCalculatedDamage().getKey();

		Entity damager = e.getDamager();
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, caster, DamageCause.CUSTOM, dmg);
		Bukkit.getPluginManager().callEvent(event);
		
		if(damager instanceof LivingEntity){
			ManualDamage.doDamage((LivingEntity) damager, caster, event.getDamage(), event);
		}
		else{
			ManualDamage.doDamage(caster, event.getDamage(), event);
		}
		
		caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.9f);
		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.9f);
		caster.getWorld().spawnParticle(Particle.WAX_ON, caster.getLocation().add(0,1,0), 8, 0.5f, 0.5f, 0.5f, 0.1f);
		victim.getWorld().spawnParticle(Particle.WAX_OFF, victim.getLocation().add(0,1,0), 8, 0.5f, 0.5f, 0.5f, 0.1f);
		
	}

}
