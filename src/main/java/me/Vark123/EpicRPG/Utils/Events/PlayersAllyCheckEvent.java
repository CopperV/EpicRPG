package me.Vark123.EpicRPG.Utils.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PlayersAllyCheckEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private Player player1;
	private Player player2;
	
	@Setter
	private boolean allies = false;

	public PlayersAllyCheckEvent(Player player1, Player player2) {
		super();
		this.player1 = player1;
		this.player2 = player2;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
