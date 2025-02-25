package me.Vark123.EpicRPG.Consumables.Impl;

import org.bukkit.Bukkit;
import org.bukkit.Color;

import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;

public class HpPotionConsumable extends DrinkConsumable {
	
	public HpPotionConsumable(int amount) {
		super(rpg -> {
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, amount);
			Bukkit.getPluginManager().callEvent(event);
		}, Color.RED);
	}

}
