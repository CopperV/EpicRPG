package me.Vark123.EpicRPG.Core.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public class EventCurrency2ModifyEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	@Setter
	private boolean cancelled;
	
	private RpgPlayer rpg;
	@Setter
	private int amount;
	@Setter
	private double modifier;
	private String reason;

	public EventCurrency2ModifyEvent(RpgPlayer rpg, int amount, double modifier, String reason) {
		super();
		this.rpg = rpg;
		this.amount = amount;
		this.modifier = modifier;
		this.reason = reason;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
