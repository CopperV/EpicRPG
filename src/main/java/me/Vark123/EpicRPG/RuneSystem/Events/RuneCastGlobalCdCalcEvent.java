package me.Vark123.EpicRPG.RuneSystem.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;

@Getter
public class RuneCastGlobalCdCalcEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private RpgPlayer rpgPlayer;
	private EpicRune rune;
	
	@Setter
	private double modifier = 1;
	
	public RuneCastGlobalCdCalcEvent(RpgPlayer rpgPlayer, EpicRune rune) {
		this(rpgPlayer, rune, false);
	}
	
	public RuneCastGlobalCdCalcEvent(RpgPlayer rpgPlayer, EpicRune rune, boolean async) {
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
	
	public long getFinalGlobalCd() {
		return (long) (RuneManager.get().RUNE_GLOBAL_CD_DEFAULT_VALUE * modifier);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
