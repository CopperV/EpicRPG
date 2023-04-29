package me.Vark123.EpicRPG.HealthSystem;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.Vark123.EpicRPG.Players.RpgPlayer;

public class RpgPlayerHealEvent extends Event implements Cancellable {

	private boolean cancel;
	private static final HandlerList handlers = new HandlerList();
	
	private RpgPlayer rpg;
	private Player p;
	private double heal;
	
	public RpgPlayerHealEvent(RpgPlayer rpg, double healAmount) {
		this.rpg = rpg;
		this.p = rpg.getPlayer();
		this.heal = healAmount;
	}
	
	@Override
	public boolean isCancelled() {
		return this.cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public RpgPlayer getRpg() {
		return rpg;
	}

	public Player getP() {
		return p;
	}

	public double getHealAmount() {
		return heal;
	}

	public void setHeal(double heal) {
		this.heal = heal;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
