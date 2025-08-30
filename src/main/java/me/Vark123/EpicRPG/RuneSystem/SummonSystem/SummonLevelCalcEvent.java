package me.Vark123.EpicRPG.RuneSystem.SummonSystem;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;

@Getter
public class SummonLevelCalcEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private Player owner;
	private ActiveMob summon;
	private ACastableRune rune;
	@Setter
	private double level = 0;

	public SummonLevelCalcEvent(Player owner, ActiveMob summon, ACastableRune rune) {
		this(owner, summon, rune, 0);
	}

	public SummonLevelCalcEvent(Player owner, ActiveMob summon, ACastableRune rune, double level) {
		super();
		this.owner = owner;
		this.summon = summon;
		this.rune = rune;
		this.level = level;
	}
	
	public void increaseLevel(int level) {
		this.level += level;
	}
	public void decreaseLevel(int level) {
		this.level -= level;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
