package me.Vark123.EpicRPG.Core.Listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.Main;
import me.clip.placeholderapi.PlaceholderAPI;

public class ExecutableItemUseListener implements Listener {

	private static final Set<UUID> cooldowns = new HashSet<>();
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) 
				&& !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		Player p = e.getPlayer();
		if(p.getInventory().getItemInMainHand() == null 
				|| p.getInventory().getItemInMainHand().getType().equals(Material.AIR))
			return;
		
		ItemStack item = p.getInventory().getItemInMainHand();
		if(item == null || item.getType().equals(Material.AIR))
			return;

		NBTItem nbtit = new NBTItem(item);
		if(!nbtit.hasTag("epic_command"))
			return;
		
		if(cooldowns.contains(p.getUniqueId()))
			return;

		String command = nbtit.getString("epic_command");
		command = PlaceholderAPI.setPlaceholders(p, command);
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
		if(item.getAmount() != 1)
			item.setAmount(item.getAmount()-1);
		else
			p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
		p.updateInventory();
		
		UUID uid = p.getUniqueId();
		cooldowns.add(uid);
		new BukkitRunnable() {
			@Override
			public void run() {
				cooldowns.remove(uid);
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 10);
	}
	
}
