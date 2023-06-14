package me.Vark123.EpicRPG.Scrolls;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.Utils;

public class KlasaResetScrollEvent implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;
		
		Player p = e.getPlayer();
		ItemStack it = e.getItem();
		
		if(it == null
				|| !it.getType().equals(Material.PAPER))
			return;
		if(!it.hasItemMeta()
				|| !it.getItemMeta().hasDisplayName()
				|| !it.getItemMeta().getDisplayName().contains("§6§lZwoj Zmiany Klasy"))
			return;
		if(!ScrollManager.getInstance().canPlayerClickScroll(p)) 
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(!rpg.resetCharacter())
			return;
		p.sendTitle("§a§l§oZresetowales klase", " ", 5, 10, 15);
		ScrollManager.getInstance().setPlayerCooldown(p);
		Utils.takeItems(p, e.getHand(), 1);
	}

}
