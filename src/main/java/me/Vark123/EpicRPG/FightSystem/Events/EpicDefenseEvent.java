package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.Utils.Pair;

@Getter
public class EpicDefenseEvent extends AEpicFightEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	@Setter
	private boolean cancelled;
	
	public EpicDefenseEvent(Entity damager, Entity victim, EpicDamageType damageType, double dmg, double modifier,
			Pair<Double, Boolean> calculatedDamage, Object... args) {
		super(damager, victim, damageType, dmg, modifier, calculatedDamage, args);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
