package me.Vark123.EpicRPG.Core.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.Vark123.EpicRPG.Players.RpgPlayer;

public class PlayerKlasaResetEvent extends Event implements Cancellable{

	private boolean cancelled;
	private RpgPlayer rpg;
	private static final HandlerList handlers = new HandlerList();
	
	public PlayerKlasaResetEvent(RpgPlayer rpg) {
		this.rpg = rpg;
	}
	
	public PlayerKlasaResetEvent(RpgPlayer rpg, boolean async) {
		super(async);
		this.rpg = rpg;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

	public RpgPlayer getRpg() {
		return rpg;
	}

}
