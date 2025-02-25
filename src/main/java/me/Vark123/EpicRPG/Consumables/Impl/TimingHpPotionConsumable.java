package me.Vark123.EpicRPG.Consumables.Impl;

import org.bukkit.Color;

public class TimingHpPotionConsumable extends DrinkConsumable {
	
	public TimingHpPotionConsumable(int amount, int duration) {
		super(rpg -> {
			rpg.getStats().createRegenHpTask(duration, amount);
		}, Color.fromRGB(192, 24, 24));
	}

}
