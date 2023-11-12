package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.Utils.Pair;

@Getter
public class EpicAttackEvent extends AEpicFightEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	@Setter
	private boolean cancelled;
	
	public EpicAttackEvent(Entity damager, Entity victim, EpicDamageType damageType, double dmg, double modifier,
			Pair<Double, Boolean> calculatedDamage, Object... args) {
		super(damager, victim, damageType, dmg, modifier, calculatedDamage, args);
	}
	
	public void increaseModifier(double modifier) {
		this.modifier += modifier;
	}
	
	public void decreaseModifier(double modifier) {
		this.modifier -= modifier;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
