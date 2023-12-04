package me.Vark123.EpicRPG.Klejnoty;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class GrindstoneUseEvent implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		String world = e.getPlayer().getWorld().getName();
		if(!(world.equalsIgnoreCase("F_RPG") || world.equalsIgnoreCase("Map120"))) 
			return;
		if(e.useInteractedBlock().equals(Result.DENY)) 
			return;
		
		Block b = e.getClickedBlock();
		if(!b.getType().equals(Material.GRINDSTONE))
			return;
		
		e.setUseInteractedBlock(Result.DENY);
		e.setUseItemInHand(Result.DENY);
		e.setCancelled(true);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rpgmenu "+e.getPlayer().getName()+" 7");
	}

}
