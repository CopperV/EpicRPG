package me.Vark123.EpicRPG.Core.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Vark123.EpicRPG.Main;

public class PlayerUseDisabledBlockEvent implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		
		Block block = e.getClickedBlock();
		Material m = block.getType();
		if(!(m.equals(Material.ENCHANTING_TABLE)
				|| m.equals(Material.ANVIL)
				|| m.equals(Material.CHIPPED_ANVIL)
				|| m.equals(Material.DAMAGED_ANVIL)
				|| m.equals(Material.SMITHING_TABLE)))
			return;
		
		if(e.getPlayer().isOp())
			return;

		e.setUseInteractedBlock(Result.DENY);
		e.setUseItemInHand(Result.DENY);
		e.setCancelled(true);
		e.getPlayer().sendMessage(Main.getInstance().getPrefix()+" §bZiomek, to jest RPG - nie mozesz tego tutaj robic!");
		return;
	}

}
