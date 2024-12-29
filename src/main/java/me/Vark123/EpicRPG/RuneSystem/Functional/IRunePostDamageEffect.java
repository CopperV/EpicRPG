package me.Vark123.EpicRPG.RuneSystem.Functional;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public interface IRunePostDamageEffect {

	public void playeEffect(Player damager, LivingEntity victim, EpicRune rune);
	
}
