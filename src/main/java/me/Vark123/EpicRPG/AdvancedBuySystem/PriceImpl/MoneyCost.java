package me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.AdvancedBuySystem.AdvancedBuyCost;

public class MoneyCost extends AdvancedBuyCost {

	private double amount;
	
	public MoneyCost(double amount) {
		super();
		this.amount = amount;
	}

	@Override
	public boolean check(Player p) {
		return !(Main.eco.getBalance(p) < amount);
	}

	@Override
	public void spend(Player p) {
		Main.eco.withdrawPlayer(p, amount);
	}

}
