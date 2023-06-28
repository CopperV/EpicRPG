package me.Vark123.EpicRPG.Players.BaseEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class PlayerDropEvent implements Listener{

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(e.isCancelled())
			return;
		
		Player p = e.getPlayer();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(rpg.getInfo().isDrop())
			return;
		
		e.setCancelled(true);
	}

}
