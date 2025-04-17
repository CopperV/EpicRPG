package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public class EpicMegaCritCalculateEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();

	private RpgPlayer rpgPlayer;
	private Entity victim;
	@Setter
	private double chance = 0;

	public EpicMegaCritCalculateEvent(RpgPlayer rpgPlayer, Entity victim) {
		super();
		this.rpgPlayer = rpgPlayer;
		this.victim = victim;
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
