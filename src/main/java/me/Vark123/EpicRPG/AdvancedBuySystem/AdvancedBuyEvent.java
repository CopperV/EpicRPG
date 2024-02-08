package me.Vark123.EpicRPG.AdvancedBuySystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AdvancedBuyEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	@Setter
	private boolean cancelled;
	
	private Player player;
	private List<IBuyAction> actions = new LinkedList<>();
	private Map<String, String> conditions;
	
	public AdvancedBuyEvent(Player player, Map<String, String> conditions) {
		super();
		this.player = player;
		this.conditions = conditions;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public void addBuyAction(IBuyAction action) {
		actions.add(action);
	}

}
