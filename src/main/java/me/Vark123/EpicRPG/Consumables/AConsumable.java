package me.Vark123.EpicRPG.Consumables;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.Utils;

public abstract class AConsumable {
	
	private final Map<UUID, Date> consumeCooldowns = new ConcurrentHashMap<>();

	public boolean canConsume(RpgPlayer rpg) {
		Date now = new Date();
		Player p = rpg.getPlayer();
		UUID uid = p.getUniqueId();
		
		if(consumeCooldowns.containsKey(uid) && consumeCooldowns.get(uid).after(now))
			return false;
		
		return true;
	}
	
	public void consume(RpgPlayer rpg, EquipmentSlot slot) {
		Player p = rpg.getPlayer();
		Utils.takeItems(p, slot, 1);
		
		Date cd = new Date(new Date().getTime() + ConsumableManager.CONSUME_COOLDOWN);
		consumeCooldowns.put(p.getUniqueId(), cd);
	}
	
}
