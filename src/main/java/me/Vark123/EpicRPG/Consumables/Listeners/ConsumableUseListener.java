package me.Vark123.EpicRPG.Consumables.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.Consumables.ConsumableManager;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.Utils;

public class ConsumableUseListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onUse(PlayerInteractEvent e) {
		Action action = e.getAction();
		if (!(action.equals(Action.RIGHT_CLICK_AIR) 
				|| action.equals(Action.RIGHT_CLICK_BLOCK))) {
			return;
		}
		
		Player p = e.getPlayer();
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		ItemStack it = e.getItem();
		if(!Utils.canUseItem(it, p))
			return;
		
		ConsumableManager.inst().getConsumable(it).ifPresent(consumable -> {
			if(!consumable.canConsume(rpg))
				return;
			
			consumable.consume(rpg, e.getHand());
			e.setUseInteractedBlock(Result.DENY);
			e.setUseItemInHand(Result.DENY);
		});
	}

}
