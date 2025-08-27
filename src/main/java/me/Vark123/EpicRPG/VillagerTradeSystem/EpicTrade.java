package me.Vark123.EpicRPG.VillagerTradeSystem;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Utils.Pair;

@Getter
@AllArgsConstructor
@Builder
@Setter
public class EpicTrade {

	private Pair<String, Integer> slot1;
	private Pair<String, Integer> slot2;
	private Pair<String, Integer> result;
	
	public Optional<ItemStack> getSlot1Item(){
		return getItem(slot1);
	}
	
	public Optional<ItemStack> getSlot2Item(){
		return getItem(slot2);
	}
	
	public Optional<ItemStack> getResultItem(){
		return getItem(result);
	}
	
	private Optional<ItemStack> getItem(Pair<String, Integer> info) {
		if(info == null)
			return Optional.empty();
		
		String mmId = info.getKey();
		ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(mmId, info.getValue());
		return Optional.ofNullable(it);
	}
	
}
