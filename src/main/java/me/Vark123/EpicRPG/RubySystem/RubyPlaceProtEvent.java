package me.Vark123.EpicRPG.RubySystem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;

public class RubyPlaceProtEvent implements Listener {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(e.isCancelled())
			return;
		
		ItemStack item = e.getItemInHand();
		
		if(item==null || item.getType().equals(Material.AIR))
			return;
		ReadWriteNBT nbti = NBT.itemStackToNBT(item);
		
		if(!nbti.hasTag("RPGType") 
				|| !nbti.getString("RPGType").contains("ruby")) 
			return;
		
		if(e.getPlayer().isOp()) 
			return;
		
		e.setCancelled(true);
		e.setBuild(false);
	}

}
