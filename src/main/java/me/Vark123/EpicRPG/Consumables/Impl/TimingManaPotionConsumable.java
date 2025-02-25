package me.Vark123.EpicRPG.Consumables.Impl;

import org.bukkit.Color;

public class TimingManaPotionConsumable extends DrinkConsumable {
	
	public TimingManaPotionConsumable(int amount, int duration) {
		super(rpg -> {
			rpg.getStats().createRegenManaTask(duration, amount);
		}, Color.AQUA);
	}

}
