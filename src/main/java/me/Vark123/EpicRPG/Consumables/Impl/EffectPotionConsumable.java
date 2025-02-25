package me.Vark123.EpicRPG.Consumables.Impl;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectPotionConsumable extends DrinkConsumable {

	public EffectPotionConsumable(PotionEffectType type, int duration, int amplifier, Color color) {
		this(new PotionEffect(type, duration, amplifier), color);
	}
	
	public EffectPotionConsumable(PotionEffect effect, Color color) {
		super(rpg -> {
			rpg.getPlayer().addPotionEffect(effect);
		}, color);
	}

}
