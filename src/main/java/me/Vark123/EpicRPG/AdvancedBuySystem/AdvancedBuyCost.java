package me.Vark123.EpicRPG.AdvancedBuySystem;

import org.bukkit.entity.Player;

public abstract class AdvancedBuyCost {

	public abstract boolean check(Player p);
	public abstract void spend(Player p);
	
}
