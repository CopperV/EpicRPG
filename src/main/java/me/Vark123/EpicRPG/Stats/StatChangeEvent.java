package me.Vark123.EpicRPG.Stats;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
@AllArgsConstructor
public class StatChangeEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();

	private RpgPlayer rpgPlayer;
	private StatTypes stat;
	@Setter
	private int value;
	@Setter
	private double multiplier;
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public void increaceStatValue(int mod) {
		value += mod;
	}
	
	public void decreaceStatValue(int mod) {
		value += mod;
	}
	
	public void increaceStatMultiplier(double mod) {
		multiplier += mod;
	}
	
	public void decreaceStatMultiplier(double mod) {
		multiplier += mod;
	}
	
	public int getFinalValue() {
		return (int) (value * multiplier);
	}
	
	public static class StatChangeEventManager {
		private StatChangeEvent event;
		private IntConsumer statSetter;
		
		public StatChangeEventManager(RpgPlayer rpg, StatTypes statType, IntSupplier statGetter, IntConsumer statSetter) {
			this.event = new StatChangeEvent(rpg, statType, statGetter.getAsInt(), 1);
			this.statSetter = statSetter;
		}
		
		public void invoke() {
			Bukkit.getPluginManager().callEvent(event);
			
			statSetter.accept(event.getFinalValue());
		}
	}
	
}
