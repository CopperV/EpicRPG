package me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.AdvancedBuySystem.AdvancedBuyCost;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class Event2Cost extends AdvancedBuyCost {

	private int amount;
	
	public Event2Cost(int amount) {
		super();
		this.amount = amount;
	}

	@Override
	public boolean check(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(rpg == null)
			return false;
		return !(rpg.getVault().getEventCurrency2() < amount);
	}

	@Override
	public void spend(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(rpg == null)
			return;
		rpg.getVault().removeEventCurrency2(amount);
	}

}
