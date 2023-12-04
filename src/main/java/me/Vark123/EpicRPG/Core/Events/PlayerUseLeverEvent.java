package me.Vark123.EpicRPG.Core.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;

public class PlayerUseLeverEvent implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		
		Block block = e.getClickedBlock();
		if(!block.getType().equals(Material.LEVER))
			return;
		
		Player p = e.getPlayer();
		if(p.isOp() || p.getWorld().getName().toLowerCase().contains("plots"))
			return;
		
		BlockData data = block.getBlockData();
		if(!(data instanceof Powerable))
			return;

		e.setCancelled(true);
		e.setUseInteractedBlock(Result.DENY);
		e.setUseItemInHand(Result.DENY);
		
		Powerable powerData = (Powerable) data;
		if(powerData.isPowered()) {
			return;
		}
		
		powerData.setPowered(true);
		block.setBlockData(powerData);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				powerData.setPowered(false);
				block.setBlockData(powerData);
			}
		}.runTaskLater(Main.getInstance(), 20*60);
	}
}
