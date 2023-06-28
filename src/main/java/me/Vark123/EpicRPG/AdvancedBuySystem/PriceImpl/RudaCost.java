package me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.AdvancedBuySystem.AdvancedBuyCost;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class RudaCost extends AdvancedBuyCost {

	private int amount;
	
	public RudaCost(int amount) {
		super();
		this.amount = amount;
	}

	@Override
	public boolean check(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		return !(rpg.getVault().getBrylkiRudy() < amount);
	}

	@Override
	public void spend(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		rpg.getVault().removeBrylkiRudy(amount);
	}

}
