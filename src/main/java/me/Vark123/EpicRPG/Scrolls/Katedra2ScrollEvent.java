package me.Vark123.EpicRPG.Scrolls;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Utils.Utils;

public class Katedra2ScrollEvent implements Listener {

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
				|| !it.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lwiedzminskie zlecenie - katedra"))
			return;
		NBTItem nbt = new NBTItem(it);
		if(!nbt.hasTag("soulbind")) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cPrzedmiot zbugowany!");
			p.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz go uzyc!");
			return;
		}
		if(!nbt.getString("soulbind").equalsIgnoreCase(p.getName())) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cZwoj jest przypisany do kogos innego!");
			p.sendMessage(Main.getInstance().getPrefix()+" §cNie mozesz go uzyc!");
			return;
		}
		if(!ScrollManager.getInstance().canPlayerClickScroll(p))
			return;

		Set<ProtectedRegion> regions = WorldGuard.getInstance()
				.getPlatform().getRegionContainer().createQuery()
				.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()))
				.getRegions();
		
		if(regions == null || regions.isEmpty()) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cNie jestes na teleporcie katedry!");
			return;
		}
		if(regions.size() == 1 
				&& ((ProtectedRegion)regions.toArray()[0]).getId().equalsIgnoreCase("__global__")) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cNie jestes na teleporcie katedry!");
			return;
		}
		
		regions.stream().filter(region -> {
			return region.getId().contains("katedra2_");
		}).findAny().ifPresent(region -> {
			ScrollManager.getInstance().setPlayerCooldown(p);
			Location loc = Bukkit.getWorld(region.getId()).getSpawnLocation();
			p.teleport(loc);
			p.sendMessage(Main.getInstance().getPrefix()+" §3§lTELEPORTACJA");
			Utils.takeItems(p, e.getHand(), 1);
		});
	}
	
}
