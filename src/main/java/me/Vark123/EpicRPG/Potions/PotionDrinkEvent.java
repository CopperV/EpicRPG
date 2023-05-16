package me.Vark123.EpicRPG.Potions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;

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
		NBTItem nbt = new NBTItem(it);
		if (!nbt.hasTag("MYTHIC_TYPE"))
			return;
		
		Player p = e.getPlayer();
		if(!PotionManager.getInstance().canDrinkPotion(p))
			return;
		e.setUseInteractedBlock(Result.DENY);
		e.setUseItemInHand(Result.DENY);
		PotionManager.getInstance().drinkPotion(p, e.getHand());
	}
	
}
