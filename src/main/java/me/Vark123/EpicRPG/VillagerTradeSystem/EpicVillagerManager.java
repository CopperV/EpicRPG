package me.Vark123.EpicRPG.VillagerTradeSystem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import lombok.Getter;

@Getter
public final class EpicVillagerManager {

	private static final EpicVillagerManager inst = new EpicVillagerManager();
	
	private final Map<String, EpicVillagerTrader> villagers = new LinkedHashMap<>();
	
	private EpicVillagerManager() {
		
	}
	
	public static final EpicVillagerManager get() {
		return inst;
	}
	
	public void registerVillager(EpicVillagerTrader villager) {
		villagers.put(villager.getId(), villager);
	}
	
	public Optional<EpicVillagerTrader> getVillager(String id){
		return Optional.ofNullable(villagers.get(id));
	}
	
	public void openTrader(Player player, String villagerId) {
		getVillager(villagerId).ifPresent(villager -> {
			Merchant merchant = Bukkit.createMerchant(villager.getDisplay());
			merchant.setRecipes(villager.getTrades().stream()
					.map(trade -> {
						var oSlot1 = trade.getSlot1Item();
						var oSlot2 = trade.getSlot2Item();
						var oResult = trade.getResultItem();
						if(oResult.isEmpty() || (oSlot1.isEmpty() && oSlot2.isEmpty()))
							return null;
						
						ItemStack it1 = oSlot1.isPresent() ? oSlot1.get() : null;
						ItemStack it2 = oSlot2.isPresent() ? oSlot2.get() : null;
						ItemStack result = oResult.get();
						
						if(it1 == null) {
							it1 = it2;
							it2 = null;
						}
						
						MerchantRecipe mTrade = new MerchantRecipe(result, Integer.MAX_VALUE);
						mTrade.addIngredient(it1);
						if(it2 != null)
							mTrade.addIngredient(it2);
						return mTrade;
					})
					.filter(trade -> trade != null)
					.collect(Collectors.toList()));
			player.openMerchant(merchant, false);
		});
	}
	
}
