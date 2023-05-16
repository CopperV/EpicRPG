package me.Vark123.EpicRPG.RuneSystem.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.Runes.LodowyBlok;

public class LodowyBlokDisableMoveEvent implements Listener {
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation()))
			return;
		if(e.isCancelled())
			return;
		Player p = e.getPlayer();
		if(p.hasMetadata("NPC"))
			return;
		
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(!rpg.getModifiers().hasLodowyBlok())
			return;
		
		if(!LodowyBlok.getEffected().contains(p))
			return;
		
		LodowyBlok.getEffected().remove(p);
		return;
	}

}
