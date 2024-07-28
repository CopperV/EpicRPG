package me.Vark123.EpicRPG.UpgradableSystem.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class UpgradableAnvilUseListener implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		String world = p.getWorld().getName();
		if(!(world.equalsIgnoreCase("F_RPG_Kampania"))) 
			return;
		if(!p.hasPermission("rpg.wytapiacz"))
			return;
		if(e.useInteractedBlock().equals(Result.DENY)) 
			return;
		
		Block b = e.getClickedBlock();
		Material m = b.getType();
		if(!(m.equals(Material.ANVIL)
				|| m.equals(Material.CHIPPED_ANVIL)
				|| m.equals(Material.DAMAGED_ANVIL)))
			return;
		
		e.setUseInteractedBlock(Result.DENY);
		e.setUseItemInHand(Result.DENY);
		e.setCancelled(true);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rpgmenu "+e.getPlayer().getName()+" 23");
	}

}
