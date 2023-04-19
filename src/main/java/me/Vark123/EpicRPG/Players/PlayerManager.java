package me.Vark123.EpicRPG.Players;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import org.bukkit.entity.Player;

@Singleton
public class PlayerManager {

	private final static PlayerManager instance = new PlayerManager();
	
	private final Map<UUID, RpgPlayer> playerContainer;
	
	private PlayerManager() {
		playerContainer = new ConcurrentHashMap<>();
	}
	
	public static PlayerManager getInstance() {
		return instance;
	}
	
	public void addPlayer(RpgPlayer rpg) {
		UUID id = rpg.getPlayer().getUniqueId();
		playerContainer.put(id, rpg);
	}
	
	public RpgPlayer getRpgPlayer(Player p) {
		return playerContainer.getOrDefault(p.getUniqueId(), null);
	}
	
	public void removePlayer(Player p) {
		playerContainer.remove(p.getUniqueId());
	}
	
	public RpgPlayer loadPlayer(Player p) {
		RpgPlayer rpg;
		
		
		
		addPlayer(rpg);
		return rpg;
	}
	
	public boolean playerExists(Player p) {
		
		return false;
	}
	
}
