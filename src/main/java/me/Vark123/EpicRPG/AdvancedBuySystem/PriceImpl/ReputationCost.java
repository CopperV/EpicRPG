package me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.AdvancedBuySystem.AdvancedBuyCost;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgReputation;
import me.Vark123.EpicRPG.Reputation.ReputationLevels;

public class ReputationCost extends AdvancedBuyCost {

	private String fraction;
	private int level;
	
	public ReputationCost(String fraction, int level) {
		this.fraction = fraction;
		this.level = level;
	}
	
	@Override
	public boolean check(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(rpg == null)
			return false;
		RpgReputation rep = rpg.getReputation();
		if(!rep.getReputacja().containsKey(fraction.toLowerCase()))
			return false;
		ReputationLevels level = rep.getReputacja().get(fraction.toLowerCase()).getReputationLevel();
		return !(level.getId() < this.level);
	}

	@Override
	public void spend(Player p) {}

}
