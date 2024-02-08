package me.Vark123.EpicRPG.FightSystem.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
@AllArgsConstructor
public class CritCalculateEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();

	private RpgPlayer rpg;
	@Setter
	private int chance;
	
	public void addChance(int chance) {
		this.chance += chance;
	}
	
	public void removeChance(int chance) {
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
