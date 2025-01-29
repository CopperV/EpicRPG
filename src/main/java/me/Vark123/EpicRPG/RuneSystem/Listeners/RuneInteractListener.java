package me.Vark123.EpicRPG.RuneSystem.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;
import me.Vark123.EpicRPG.Utils.Utils;

public class RuneInteractListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;
		
		if(!e.getHand().equals(EquipmentSlot.HAND))
			return;
		
		Player p = e.getPlayer();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		ItemStack originalRune = p.getInventory().getItemInMainHand();
		if(!Utils.isRune(originalRune))
			return;
	
		ReadableNBT nbt = NBT.readNbt(originalRune);
		nbt = nbt.getCompound("PublicBukkitValues");
		String mmType = nbt.getString("mythicmobs:type");
		ItemStack rune = MythicBukkit.inst().getItemManager().getItemStack(mmType);
				
		if(RuneManager.get().tryCastRune(rpg, rune)) {
			e.setUseInteractedBlock(Result.DENY);
			e.setUseItemInHand(Result.DENY);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getHand().equals(EquipmentSlot.HAND))
			return;
		
		if(!MythicBukkit.inst().getMobManager().isMythicMob(e.getRightClicked()))
			return;
		
		Player p = e.getPlayer();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		ItemStack originalRune = p.getInventory().getItemInMainHand();
		if(!Utils.isRune(originalRune))
			return;
	
		String mmType = Utils.getMythicMobItemType(originalRune);
		ItemStack rune = MythicBukkit.inst().getItemManager().getItemStack(mmType);
		
		if(RuneManager.get().tryCastRune(rpg, rune)) {
			e.setCancelled(true);
		}
	}
	
}
