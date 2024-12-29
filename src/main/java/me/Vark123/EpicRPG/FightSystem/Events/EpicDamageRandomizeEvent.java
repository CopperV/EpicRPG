package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public class EpicDamageRandomizeEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();

	private RpgPlayer rpgPlayer;
	@Setter
	private double modifier = 0;

	public EpicDamageRandomizeEvent(RpgPlayer rpgPlayer, double initialModifier) {
		super();
		this.rpgPlayer = rpgPlayer;
		this.modifier = initialModifier;
	}
	
	public void increaseModifier(double modifier) {
		this.modifier += modifier;
	}
	
	public void decreaseModifier(double modifier) {
		this.modifier -= modifier;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
