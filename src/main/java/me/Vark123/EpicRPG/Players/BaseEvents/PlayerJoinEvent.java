package me.Vark123.EpicRPG.Players.BaseEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.RpgPlayer;

public class PlayerJoinEvent implements Listener {

	@EventHandler
	public void onJoin(org.bukkit.event.player.PlayerJoinEvent e) {
		Player p = e.getPlayer();
		RpgPlayer rpg;
	}
	
}
