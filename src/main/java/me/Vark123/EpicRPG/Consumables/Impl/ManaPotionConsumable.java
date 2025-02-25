package me.Vark123.EpicRPG.Consumables.Impl;

import org.bukkit.Color;

public class ManaPotionConsumable extends DrinkConsumable {
	
	public ManaPotionConsumable(int amount) {
		super(rpg -> {
			rpg.getStats().addPresentManaSmart(amount);
		}, Color.BLUE);
	}

}
