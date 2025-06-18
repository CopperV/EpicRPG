package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public class EpicCritCalculateEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();

	private RpgPlayer rpgPlayer;
	@Setter
	private double chance = 0;

	public EpicCritCalculateEvent(RpgPlayer rpgPlayer) {
		super();
		this.rpgPlayer = rpgPlayer;
	}
	
	public void addChance(double chance) {
		this.chance += chance;
	}
	
	public void removeChance(double chance) {
		this.chance -= chance;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
