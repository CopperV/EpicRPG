package me.Vark123.EpicRPG.RuneSystem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Utils.Utils;

public class RuneTimeCheckEvent implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(!(e.getAction().equals(Action.LEFT_CLICK_AIR)
				|| e.getAction().equals(Action.LEFT_CLICK_BLOCK)))
			return;
		
		Player p = e.getPlayer();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!Utils.isRune(item))
			return;
	
		RuneManager.getInstance().regenTimePass(p, item);
	}

}
