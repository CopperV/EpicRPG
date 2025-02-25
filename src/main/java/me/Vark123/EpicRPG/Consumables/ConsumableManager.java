package me.Vark123.EpicRPG.Consumables;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
public class ConsumableManager {

	public static long CONSUME_COOLDOWN = 1450L;
	
	@Accessors(fluent = true)
	@Getter()
	private static final ConsumableManager inst = new ConsumableManager();
	
	private final Map<String, AConsumable> consumables = new ConcurrentHashMap<>();
	
	private ConsumableManager() {
	}
	
	public void registerConsumable(String mmId, AConsumable consumable) {
		consumables.put(mmId, consumable);
	}
	
	public boolean hasConsumable(String mmId) {
		return consumables.containsKey(mmId);
	}
	
	public Optional<AConsumable> getConsumable(String mmId) {
		return Optional.ofNullable(consumables.get(mmId));
	}
	
	public Optional<AConsumable> getConsumable(ItemStack it) {
		if(!MythicBukkit.inst().getItemManager().isMythicItem(it))
			return Optional.empty();
		
		String mmId = MythicBukkit.inst().getItemManager().getMythicTypeFromItem(it);
		return Optional.ofNullable(consumables.get(mmId));
	}
	
}
