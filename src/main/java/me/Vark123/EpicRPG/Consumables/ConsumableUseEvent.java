package me.Vark123.EpicRPG.Consumables;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ConsumableUseEvent extends Event implements Cancellable {

	@Setter
	private boolean cancelled;
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private ItemStack consumableItem;
	private AConsumable consumable;

	public ConsumableUseEvent(Player player, ItemStack consumableItem, AConsumable consumable) {
		super();
		this.player = player;
		this.consumableItem = consumableItem;
		this.consumable = consumable;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
