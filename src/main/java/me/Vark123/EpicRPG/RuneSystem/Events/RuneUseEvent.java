package me.Vark123.EpicRPG.RuneSystem.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

@Getter
public class RuneUseEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Player player;
	private ItemStackRune rune;

	public RuneUseEvent(Player player, ItemStackRune rune) {
		super();
		this.player = player;
		this.rune = rune;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
