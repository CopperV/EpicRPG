package me.Vark123.EpicRPG.Players.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public class RpgPlayerManaUseEvent extends Event implements Cancellable {

	@Setter
	private boolean cancelled;
	private static final HandlerList handlers = new HandlerList();

	private RpgPlayer rpg;
	private Player player;
	@Setter
	private int mana;
	
	public RpgPlayerManaUseEvent(RpgPlayer rpg, int mana) {
		this.rpg = rpg;
		this.player = rpg.getPlayer();
		this.mana = mana;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
