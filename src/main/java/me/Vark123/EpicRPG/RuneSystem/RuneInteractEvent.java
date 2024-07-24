package me.Vark123.EpicRPG.RuneSystem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.Utils;

public class RuneInteractEvent implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;
		
		Player p = e.getPlayer();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!Utils.isRune(item))
			return;
	
		NBTItem nbt = new NBTItem(item);
		String mmType = nbt.getString("MYTHIC_TYPE");
		item = MythicBukkit.inst().getItemManager().getItemStack(mmType);
		
		RuneManager.getInstance().castRune(rpg, item);
	}

}
