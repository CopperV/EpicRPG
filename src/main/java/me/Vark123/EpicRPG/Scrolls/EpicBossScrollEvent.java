package me.Vark123.EpicRPG.Scrolls;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;

public class EpicBossScrollEvent implements Listener {

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
				|| !it.getItemMeta().getDisplayName().contains("§oZwoj teleportacji"))
			return;
		if(!ScrollManager.getInstance().canPlayerClickScroll(p)) 
			return;
		
		String zwoj = it.getItemMeta().getDisplayName().split(" - ")[1].toLowerCase();
		switch(zwoj) {
			case "sanktuarium":
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), ()->{
					World w = Bukkit.getWorld("Map70");
					p.teleport(w.getSpawnLocation());
				});
				break;
			case "twierdza":
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), ()->{
					World w = Bukkit.getWorld("Ragnaros2");
					p.teleport(w.getSpawnLocation());
				});
				break;
			case "zrodlo zywiolow":
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), ()->{
					World w = Bukkit.getWorld("FINAL_RPG");
					p.teleport(w.getSpawnLocation());
				});
				break;
			case "arena ragnarosa":
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), ()->{
					World w = Bukkit.getWorld("shazzrah_epic");
					p.teleport(w.getSpawnLocation());
				});
				break;
			case "leze onyxii":
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), ()->{
					World w = Bukkit.getWorld("onyxia_epic");
					p.teleport(w.getSpawnLocation());
				});
				break;
			case "kaplica kultystow":
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), ()->{
					World w = Bukkit.getWorld("brutus_epic");
					p.teleport(w.getSpawnLocation());
				});
				break;
		}
		ScrollManager.getInstance().setPlayerCooldown(p);
		Utils.takeItems(p, e.getHand(), 1);
	}

}
