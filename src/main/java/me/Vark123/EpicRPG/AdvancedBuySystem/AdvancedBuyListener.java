package me.Vark123.EpicRPG.AdvancedBuySystem;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl.CoinsCost;
import me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl.MoneyCost;
import me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl.ReputationCost;
import me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl.RudaCost;
import me.Vark123.EpicRPG.AdvancedBuySystem.PriceImpl.StygiaCost;

public class AdvancedBuyListener implements Listener {

	@EventHandler
	public void onBuy(AdvancedBuyEvent e) {
		if(e.isCancelled())
			return;
		
		List<AdvancedBuyCost> costs = new LinkedList<>();
		e.getConditions().forEach((key, value) -> {
			switch(key.toLowerCase()) {
				case "money":
					double money = Double.parseDouble(value);
					costs.add(new MoneyCost(money));
					break;
				case "coins":
					int coins = Integer.parseInt(value);
					costs.add(new CoinsCost(coins));
					break;
				case "ruda":
					int ruda = Integer.parseInt(value);
					costs.add(new RudaCost(ruda));
					break;
				case "stygia":
					int stygia = Integer.parseInt(value);
					costs.add(new StygiaCost(stygia));
					break;
				case "reputation":
					String[] tab = value.split(":");
					String fraction = tab[0].toLowerCase();
					int level = Integer.parseInt(tab[1]);
					costs.add(new ReputationCost(fraction, level));
					break;
			}
		});
		
		Player p = e.getPlayer();
		costs.stream()
			.filter(cost -> !cost.check(p))
			.findAny()
			.ifPresent(cost -> {
				e.setCancelled(true);
			});
		
		if(e.isCancelled())
			return;
		
		costs.forEach(cost -> e.addBuyAction(_p -> cost.spend(p)));
	}
	
}
