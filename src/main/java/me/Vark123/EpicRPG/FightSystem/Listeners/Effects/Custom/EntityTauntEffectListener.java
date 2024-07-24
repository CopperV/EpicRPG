package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.RuneSystem.Runes.Prowokacja;

public class EntityTauntEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onTaunt(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!Prowokacja.getTargets().containsKey(victim))
			return;

		Player p = Prowokacja.getTargets().get(victim);
		if(!p.getWorld().getName().equalsIgnoreCase(victim.getWorld().getName()))
			return;
		
		MythicBukkit.inst().getMobManager().getActiveMob(victim.getUniqueId()).ifPresent(mob -> {
			if(mob.hasThreatTable())
				mob.getThreatTable().Taunt(BukkitAdapter.adapt(Prowokacja.getTargets().get(victim)));
			else {
				mob.setTarget(BukkitAdapter.adapt(Prowokacja.getTargets().get(victim)));
			}
		});
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onAttack(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		
		if(Prowokacja.getTargets().containsKey(victim))
			return;
		
		MutableObject<Entity> attacker = new MutableObject<>(e.getDamager());
		if(attacker.getValue() instanceof Projectile)
			attacker.setValue((Entity) ((Projectile) attacker.getValue()).getShooter());
		
		MythicBukkit.inst().getMobManager().getActiveMob(victim.getUniqueId()).ifPresent(mob -> {
			if(mob.hasThreatTable())
				return;
			
			List<String> selectors = mob.getType().getAITargetSelectors();
			if(!(selectors.contains("attacker") 
					|| selectors.contains("damager") 
					|| selectors.contains("hurtbytarget")))
				return;

			AbstractEntity ae = BukkitAdapter.adapt(attacker.getValue());
			
			if(mob.getNewTarget() == null) {
				mob.setTarget(ae);
				return;
			}
			
			if(mob.getNewTarget().equals(ae))
				return;

			Random rand = new Random();
			if(rand.nextDouble() > 0.05)
				return;
			
			mob.setTarget(ae);
		});
		
	}

}
