package me.Vark123.EpicRPG.OldRuneSystem;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@Deprecated
@FunctionalInterface
public interface RuneEffect {

	public void playEffect(Player damager, LivingEntity victim, ItemStackRune ir);
	
}
