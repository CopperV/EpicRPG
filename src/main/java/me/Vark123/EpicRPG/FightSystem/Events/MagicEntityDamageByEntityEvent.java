package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import lombok.Getter;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

@Getter
public class MagicEntityDamageByEntityEvent extends EntityDamageByEntityEvent {

	private EpicRune rune;
	
	public MagicEntityDamageByEntityEvent(Entity damager, Entity damagee, 
			double damage, EpicRune rune) {
		super(damager, damagee, 
				DamageCause.CUSTOM, DamageSource
					.builder(DamageType.MAGIC)
					.withCausingEntity(damager)
					.withDirectEntity(damager)
					.build(),
				damage);
		this.rune = rune;
	}

}
