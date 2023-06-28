package me.Vark123.EpicRPG.Players.BaseEvents;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Stats.CheckStats;

public class PlayerArrowWeaponUseEvent implements Listener {
	
	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;
		
		Player p = e.getPlayer();
		ItemStack toCheck = e.getItem();
		if(toCheck == null
				|| toCheck.getType().equals(Material.AIR))
			return;
		if(!(toCheck.getType().equals(Material.BOW)
				|| toCheck.getType().equals(Material.CROSSBOW)))
			return;

		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(CheckStats.check(rpg, toCheck))
			return;
		e.getPlayer().sendMessage("§cNie mozesz uzywac tej broni");
		e.setCancelled(true);
		e.setUseInteractedBlock(Result.DENY);
		e.setUseItemInHand(Result.DENY);
		return;
	}

}
