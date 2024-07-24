package me.Vark123.EpicRPG.Players.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public class RpgPlayerManaRegenEvent extends Event implements Cancellable {

	private boolean cancel;
	private static final HandlerList handlers = new HandlerList();
	
	private RpgPlayer rpg;
	private Player player;
	@Setter
	private int regen;
	
	public RpgPlayerManaRegenEvent(RpgPlayer rpg, int regenAmount) {
		this.rpg = rpg;
		this.player = rpg.getPlayer();
		this.regen = regenAmount;
	}
	
	@Override
	public boolean isCancelled() {
		return this.cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
