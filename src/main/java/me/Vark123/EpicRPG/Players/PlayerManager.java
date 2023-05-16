package me.Vark123.EpicRPG.Players;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.MySQL.DBOperations;
import me.Vark123.EpicRPG.Stats.ChangeStats;

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
		final RpgPlayer rpg;
		
		if(DBOperations.playerExists(p)) {
			rpg = new RpgPlayer(p, DBOperations.getPlayer(p));
		} else {
			rpg = new RpgPlayer(p);
			DBOperations.savePlayer(rpg);
		}
		
		ChangeStats.change(rpg);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), ()->{
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(rpg.getStats().getFinalHealth());
			p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			if(rpg.getSkills().hasHungerless()) 
				p.setFoodLevel(18);
			else
				p.setFoodLevel(20);
			rpg.getStats().addPresentManaSmart(rpg.getStats().getFinalMana());
		});
		
		p.setHealthScale(20);
		
		addPlayer(rpg);
		rpg.createScoreboard();
		
		return rpg;
	}
	
	public boolean playerExists(Player p) {
		
		return playerContainer.containsKey(p.getUniqueId());
	}
	
}
