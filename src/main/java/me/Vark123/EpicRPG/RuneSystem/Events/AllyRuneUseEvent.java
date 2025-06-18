package me.Vark123.EpicRPG.RuneSystem.Events;

import java.util.Collection;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

@Getter
@AllArgsConstructor
public class AllyRuneUseEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Player caster;
	private EpicRune rune;
	private ACastableRune castableRune;
	private Collection<LivingEntity> affectedEntities;
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
