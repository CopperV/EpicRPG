package me.Vark123.EpicRPG.OldFightSystem.Events;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import lombok.Getter;
import me.Vark123.EpicRPG.OldRuneSystem.ItemStackRune;

@Getter
public class MagicEntityDamageByEntityEvent extends EntityDamageByEntityEvent {

	private ItemStackRune rune;
	
	public MagicEntityDamageByEntityEvent(Entity damager, Entity damagee, double damage, ItemStackRune rune) {
		super(damager, damagee, DamageCause.CUSTOM, damage);
		this.rune = rune;
	}

}
