package me.Vark123.EpicRPG.Core.Listeners;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
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
		if(!nbtit.hasTag("epic_command") && !nbtit.hasTag("epic_commands"))
			return;
		
		if(cooldowns.contains(p.getUniqueId()))
			return;
		
		List<String> commands = new LinkedList<>();
		if(nbtit.hasTag("epic_command")) {
			String command = nbtit.getString("epic_command");
			command = PlaceholderAPI.setPlaceholders(p, command);
			commands.add(command);
		}
		if(nbtit.hasTag("epic_commands")) {
			if(cooldowns.contains(p.getUniqueId()))
				return;
			ReadWriteNBT cmdsNBT = nbtit.getCompound("epic_commands");
			commands.addAll(cmdsNBT.getKeys().stream()
				.map(key -> cmdsNBT.getString(key))
				.map(cmd -> PlaceholderAPI.setPlaceholders(p, cmd))
				.collect(Collectors.toList()));
		}
		
		commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command));
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
