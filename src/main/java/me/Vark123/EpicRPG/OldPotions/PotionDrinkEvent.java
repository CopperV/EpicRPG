package me.Vark123.EpicRPG.OldPotions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.Utils.Utils;

@Deprecated
public class PotionDrinkEvent implements Listener {

	@EventHandler
	public void onHeadPotionDrink(PlayerInteractEvent e) {
		Action action = e.getAction();
		if (!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
			return;
		}
		ItemStack it = e.getItem();
		if (it == null || it.getType().equals(Material.AIR) || !it.getType().equals(Material.PLAYER_HEAD))
			return;
		if (!Utils.isMythicMobItem(it))
			return;
		
		Player p = e.getPlayer();
		if(!PotionManager.getInstance().canDrinkPotion(p))
			return;
		if(!PotionManager.getInstance().drinkPotion(p, e.getHand()))
			return;
		e.setUseInteractedBlock(Result.DENY);
		e.setUseItemInHand(Result.DENY);
	}
	
}
