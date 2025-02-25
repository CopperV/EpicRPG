package me.Vark123.EpicRPG.Consumables.Impl;

import org.bukkit.Bukkit;
import org.bukkit.Color;

import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;

public class WitcherPotionConsumable extends DrinkConsumable {
	
	public WitcherPotionConsumable(int hpAmount, int manaAmount) {
		super(rpg -> {
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hpAmount);
			Bukkit.getPluginManager().callEvent(event);
			
			rpg.getStats().addPresentManaSmart(manaAmount);
		}, Color.ORANGE);
	}

}
