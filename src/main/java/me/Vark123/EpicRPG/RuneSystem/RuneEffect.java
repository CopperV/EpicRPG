package me.Vark123.EpicRPG.RuneSystem;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface RuneEffect {

	public void playEffect(Player damager, LivingEntity victim, ItemStackRune ir);
	
}
