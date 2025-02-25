package me.Vark123.EpicRPG.Consumables.Impl;

import org.bukkit.Color;

public class TimingWitcherPotionConsumable extends DrinkConsumable {
	
	public TimingWitcherPotionConsumable(int hpAmount, int manaAmount, int duration) {
		super(rpg -> {
			rpg.getStats().createRegenHpTask(duration, hpAmount);
			rpg.getStats().createRegenManaTask(duration, manaAmount);
		}, Color.YELLOW);
	}

}
