package me.Vark123.EpicRPG.RubySystem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicComponentAPI.EpicComponent;

public class RubyPlaceProtEvent implements Listener {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(e.isCancelled())
			return;
		
		ItemStack item = e.getItemInHand();
		
		if(item==null || item.getType().equals(Material.AIR))
			return;
		EpicComponent compi = new EpicComponent(item);
		
		if(!compi.hasKey("rpgtype") 
				|| !compi.getString("rpgtype").contains("ruby")) 
			return;
		
		if(e.getPlayer().isOp()) 
			return;
		
		e.setCancelled(true);
		e.setBuild(false);
	}

}
