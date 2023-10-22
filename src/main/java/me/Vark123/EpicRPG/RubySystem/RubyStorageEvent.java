package me.Vark123.EpicRPG.RubySystem;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RubyStorageEvent extends Event implements Cancellable {

	private boolean cancelled;
	private static final HandlerList handlers = new HandlerList();
	
	private Player p;
	
	private double use;
	private double store;
	
	private boolean useResources = true;
	@Setter(value = AccessLevel.NONE)
	private boolean usingHp;

	public RubyStorageEvent(Player p, double use, double store, boolean usingHp) {
		super();
		this.p = p;
		this.use = use;
		this.store = store;
		this.usingHp = usingHp;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
}
