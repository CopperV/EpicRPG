package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class EpicDeathEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	protected LivingEntity killer;
	protected LivingEntity victim;
	
	protected Player playerKiller = null;

	public EpicDeathEvent(LivingEntity killer, LivingEntity victim) {
		super();
		this.killer = killer;
		this.victim = victim;
		
		if(killer == null) {
			EntityDamageEvent event = Utils.getLastDamageCause(victim);
			if(event != null && event instanceof EntityDamageByEntityEvent) {
				killer = (LivingEntity) ((EntityDamageByEntityEvent) event).getDamager();
			}
		}
		if(killer instanceof Player)
			playerKiller = (Player) killer;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
