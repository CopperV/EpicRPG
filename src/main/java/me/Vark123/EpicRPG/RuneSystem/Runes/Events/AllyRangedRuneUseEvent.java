package me.Vark123.EpicRPG.RuneSystem.Runes.Events;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.RuneSystem.ARune;

@Getter
@AllArgsConstructor
public class AllyRangedRuneUseEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();

	private Player caster;
	private ARune rune;
	private Collection<Player> affectedPlayers;
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
