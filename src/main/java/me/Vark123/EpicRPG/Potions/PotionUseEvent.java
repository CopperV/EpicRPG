package me.Vark123.EpicRPG.Potions;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PotionUseEvent extends Event implements Cancellable {

	@Setter
	private boolean cancelled;
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private ItemStack potion;
	private RpgPotionType type;

	public PotionUseEvent(Player player, ItemStack potion, RpgPotionType type) {
		super();
		this.player = player;
		this.potion = potion;
		this.type = type;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
