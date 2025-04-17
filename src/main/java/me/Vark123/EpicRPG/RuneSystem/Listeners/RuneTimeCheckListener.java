package me.Vark123.EpicRPG.RuneSystem.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;
import me.Vark123.EpicRPG.Utils.Utils;

public class RuneTimeCheckListener implements Listener {

	@EventHandler
	private void onClick(PlayerInteractEvent e) {
		if(!(e.getAction().equals(Action.LEFT_CLICK_AIR)
				|| e.getAction().equals(Action.LEFT_CLICK_BLOCK)))
			return;
		
		Player p = e.getPlayer();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		
		ItemStack originalRune = p.getInventory().getItemInMainHand();
		if(!Utils.isRune(originalRune))
			return;
		
		if(RuneManager.get().isRegenTimePassed(p, originalRune)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onHit(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getCause().equals(DamageCause.ENTITY_ATTACK))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player p))
			return;
		
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		
		ItemStack originalRune = p.getInventory().getItemInMainHand();
		if(!Utils.isRune(originalRune))
			return;
		
		if(RuneManager.get().isRegenTimePassed(p, originalRune)) {
			e.setCancelled(true);
		}
	}
	
}
