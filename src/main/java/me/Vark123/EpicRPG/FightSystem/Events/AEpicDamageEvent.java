package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.FightSystem.DamageType;

@Getter
public abstract class AEpicDamageEvent extends Event implements Cancellable {

	protected LivingEntity damager;
	protected LivingEntity victim;
	
	protected DamageSource damageSource;
	protected DamageType damageType;
	
	@Setter
	protected double damage;
	@Setter
	protected double modifier = 1;

	protected Object[] args;
	
	@Setter
	protected boolean cancelled;

	public AEpicDamageEvent(LivingEntity damager, LivingEntity victim, DamageSource damageSource, DamageType damageType,
			double damage, Object[] args) {
		super();
		this.damager = damager;
		this.victim = victim;
		this.damageSource = damageSource;
		this.damageType = damageType;
		this.damage = damage;
		this.args = args;
	}

	public void increaseModifier(double modifier) {
		this.modifier += modifier;
	}
	
	public void decreaseModifier(double modifier) {
		this.modifier -= modifier;
	}
	
	public double getFinalDamage() {
		return damage * modifier;
	}
	
}
