package me.Vark123.EpicRPG.Gems;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicComponentAPI.EpicComponent;

public class GemPlaceProtEvent implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onPlace(BlockPlaceEvent e) {
		if(e.isCancelled())
			return;
		
		ItemStack item = e.getItemInHand();
		
		if(item==null || item.getType().equals(Material.AIR))
			return;
		EpicComponent comp = new EpicComponent(item, MythicBukkit.inst());
		
		if(!comp.hasKey("rpgtype") 
				|| !comp.getString("rpgtype").equalsIgnoreCase("gem")) 
			return;
		
		if(e.getPlayer().isOp()) 
			return;
		
		e.setCancelled(true);
		e.setBuild(false);
	}

}
