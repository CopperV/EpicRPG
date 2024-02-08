package me.Vark123.EpicRPG.Core.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public class RpgPlayerJoinEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	@Setter
	private boolean cancelled;
	
	private RpgPlayer rpg;
	
	public RpgPlayerJoinEvent(RpgPlayer rpg) {
		this(rpg, false);
	}
	
	public RpgPlayerJoinEvent(RpgPlayer rpg, boolean async) {
		super(async);
		this.rpg = rpg;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	

}
