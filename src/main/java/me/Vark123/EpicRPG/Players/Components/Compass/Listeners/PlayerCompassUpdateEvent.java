package me.Vark123.EpicRPG.Players.Components.Compass.Listeners;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.Compass.EpicCompass;

public class PlayerCompassUpdateEvent implements Listener {

	private static final Map<Player, Date> cooldowns = new ConcurrentHashMap<>();
	private static final long COOLDOWN = 50L;
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.isCancelled())
			return;
		
		Player p = e.getPlayer();
		if(cooldowns.containsKey(p) && new Date().getTime() - cooldowns.get(p).getTime() < COOLDOWN)
			return;
		cooldowns.put(p, new Date());
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		EpicCompass compass = rpg.getCompass();
		if(!compass.isEnabled())
			return;
		
		compass.updateCompass();
	}
	
}
