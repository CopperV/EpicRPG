package me.Vark123.EpicRPG.RuneSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

@Getter
public class RuneCastCostCalcEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private RpgPlayer rpgPlayer;
	private EpicRune rune;
	
	@Setter
	private double modifier = 1;
	
	@Setter
	private boolean cancelled;
	
	public RuneCastCostCalcEvent(RpgPlayer rpgPlayer, EpicRune rune) {
		this(rpgPlayer, rune, false);
	}
	
	public RuneCastCostCalcEvent(RpgPlayer rpgPlayer, EpicRune rune, boolean async) {
		super(async);
		
		this.rpgPlayer = rpgPlayer;
		this.rune = rune;
	}
	
	public void increaseModifier(double amount) {
		modifier += amount;
	}
	
	public void decreaseModifier(double amount) {
		modifier -= amount;
	}
	
	public int getFinalCost() {
		return (int) (rune.getPrice() * modifier);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
