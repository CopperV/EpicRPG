package me.Vark123.EpicRPG.Players.BaseEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class PlayerRespawnEvent implements Listener {

	@EventHandler
	public void onRespawn(org.bukkit.event.player.PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		rpg.getStats().resetMana();
	}

}
