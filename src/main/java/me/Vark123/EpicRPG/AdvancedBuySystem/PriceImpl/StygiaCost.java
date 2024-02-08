package me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.AdvancedBuySystem.AdvancedBuyCost;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class StygiaCost extends AdvancedBuyCost {

	private int amount;
	
	public StygiaCost(int amount) {
		super();
		this.amount = amount;
	}

	@Override
	public boolean check(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(rpg == null)
			return false;
		return !(rpg.getVault().getStygia() < amount);
	}

	@Override
	public void spend(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(rpg == null)
			return;
		rpg.getVault().removeStygia(amount);
	}

}
