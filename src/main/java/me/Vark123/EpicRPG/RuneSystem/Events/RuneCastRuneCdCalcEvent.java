package me.Vark123.EpicRPG.RuneSystem.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

@Getter
public class RuneCastRuneCdCalcEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private RpgPlayer rpgPlayer;
	private EpicRune rune;
	
	@Setter
	private double modifier = 1;
	
	public RuneCastRuneCdCalcEvent(RpgPlayer rpgPlayer, EpicRune rune) {
		this(rpgPlayer, rune, false);
	}
	
	public RuneCastRuneCdCalcEvent(RpgPlayer rpgPlayer, EpicRune rune, boolean async) {
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
	
	public long getFinalCd() {
		return (long) (rune.getRegenTime() * 1000 * modifier);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
