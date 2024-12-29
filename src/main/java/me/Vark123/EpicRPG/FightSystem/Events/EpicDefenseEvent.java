package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicRPG.FightSystem.DamageType;

@Getter
public class EpicDefenseEvent extends AEpicDamageEvent {

	private static final HandlerList handlers = new HandlerList();

	public EpicDefenseEvent(LivingEntity damager, LivingEntity victim, DamageSource damageSource, DamageType damageType,
			double damage, Object... args) {
		super(damager, victim, damageSource, damageType, damage, args);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
