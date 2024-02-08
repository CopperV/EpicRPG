package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.Utils.Pair;

@Getter
public abstract class AEpicFightEvent extends Event {

	protected Entity damager;
	protected Entity victim;
	
	protected EpicDamageType damageType;
	
	@Setter
	protected double dmg;
	@Setter
	protected double modifier;
	
	protected Pair<Double, Boolean> calculatedDamage;
	protected Object[] args;
	
	public AEpicFightEvent(Entity damager, Entity victim, EpicDamageType damageType, double dmg, double modifier,
			Pair<Double, Boolean> calculatedDamage, Object[] args) {
		super();
		this.damager = damager;
		this.victim = victim;
		this.damageType = damageType;
		this.dmg = dmg;
		this.modifier = modifier;
		this.calculatedDamage = calculatedDamage;
		this.args = args;
	}

	public void increaseModifier(double modifier) {
		this.modifier += modifier;
	}
	
	public void decreaseModifier(double modifier) {
		this.modifier -= modifier;
	}
	
}
