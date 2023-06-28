package me.Vark123.EpicRPG.BlackrockSystem;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class BlackrockManager {

	private static final BlackrockManager instance = new BlackrockManager();
	
	@Getter(value = AccessLevel.NONE)
	public final int BLACKROCK_ENTRY_PRICE;
	@Getter(value = AccessLevel.NONE)
	public final int ENTRY_COOLDOWN;
	
	public final Set<Player> completedPlayers;
	public final Map<Player, Date> entryCooldowns;
	
	public BlackrockManager() {
		this.BLACKROCK_ENTRY_PRICE = 125_000;
		this.ENTRY_COOLDOWN = 1_000;
		this.completedPlayers = new HashSet<>();
		this.entryCooldowns = new HashMap<>();
	}
	
	public static final BlackrockManager getInstance() {
		return instance;
	}
	
	public boolean isCompletedDailyBlackrock(Player p) {
		return completedPlayers.contains(p);
	}
	
	public void completeDailyBlackrock(Player p) {
		completedPlayers.add(p);
	}
	
	public void removePlayerDailyBlackrock(Player p) {
		completedPlayers.remove(p);
	}
	
}
