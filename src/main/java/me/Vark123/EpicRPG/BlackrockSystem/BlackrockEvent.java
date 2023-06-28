package me.Vark123.EpicRPG.BlackrockSystem;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BlackrockEvent extends Event implements Cancellable {
	
	boolean cancelled;
	private static final HandlerList handlers = new HandlerList();
	
	private Player p;
	private BlackRockOperations operation;
	
	public BlackrockEvent(Player p, BlackRockOperations operation) {
		this.p = p;
		this.operation = operation;
		cancelled = false;
	}
	
	public BlackrockEvent(Player p, BlackRockOperations operation, boolean async) {
		super(async);
		this.p = p;
		this.operation = operation;
		cancelled = false;
	}

	public Player getPlayer() {
		return p;
	}

	public BlackRockOperations getOperation() {
		return operation;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
