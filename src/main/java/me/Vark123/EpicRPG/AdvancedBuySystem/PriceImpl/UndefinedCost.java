package me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.AdvancedBuySystem.AdvancedBuyCost;

public class UndefinedCost extends AdvancedBuyCost {

	@Override
	public boolean check(Player p) {
		return false;
	}

	@Override
	public void spend(Player p) {}

}
