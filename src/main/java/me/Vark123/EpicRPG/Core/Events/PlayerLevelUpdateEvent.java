package me.Vark123.EpicRPG.Core.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.Vark123.EpicRPG.Players.RpgPlayer;

public class PlayerLevelUpdateEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private RpgPlayer rpg;
	private int newLevel;

	public PlayerLevelUpdateEvent(RpgPlayer rpg, int newLevel) {
		this.rpg = rpg;
		this.newLevel = newLevel;
	}

	public RpgPlayer getRpg() {
		return rpg;
	}

	public int getNewLevel() {
		return newLevel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
