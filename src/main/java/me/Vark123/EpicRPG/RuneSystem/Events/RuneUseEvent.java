package me.Vark123.EpicRPG.RuneSystem.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

@Getter
public class RuneUseEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private RpgPlayer rpgPlayer;
	private EpicRune rune;

	public RuneUseEvent(RpgPlayer rpgPlayer, EpicRune rune) {
		super();
		this.rpgPlayer = rpgPlayer;
		this.rune = rune;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
