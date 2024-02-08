package me.Vark123.EpicRPG.Core.CPS;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Vark123.EpicRPG.Core.CPS.CPSManager.ClickType;

public class CPSClickListener implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		ClickType click;
		switch(e.getAction()) {
			case LEFT_CLICK_AIR:
			case LEFT_CLICK_BLOCK:
				click = ClickType.LEFT;
				break;
			case RIGHT_CLICK_AIR:
			case RIGHT_CLICK_BLOCK:
				click = ClickType.RIGHT;
				break;
			case PHYSICAL:
				return;
			default:
				return;
		}
		
		Player p = e.getPlayer();
		CPSManager.get().addClick(p, click);
	}

}
