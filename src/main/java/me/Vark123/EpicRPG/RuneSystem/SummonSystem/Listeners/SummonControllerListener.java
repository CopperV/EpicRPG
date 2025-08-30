package me.Vark123.EpicRPG.RuneSystem.SummonSystem.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonControllerManager;
import me.Vark123.EpicRPG.Utils.ComboClick;
import me.Vark123.EpicRPG.Utils.Utils;

public class SummonControllerListener implements Listener {
	
	private SummonControllerManager manager = SummonControllerManager.getInst();
	
	@EventHandler(priority = EventPriority.LOW)
	public void onClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		ItemStack it = e.getItem();
		if (it == null || it.getType().equals(Material.AIR) || !Utils.canUseItem(it, player))
			return;

		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		if (!comp.hasKey("summon_controller") || !Utils.isMythicMobItem(it))
			return;

		ComboClick click = (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.PHYSICAL)) ? 
				ComboClick.LEFT :
				ComboClick.RIGHT;
		if(manager.updateCombo(player, it, click)) {
			manager.finishCombo(player, it);
		}
		
		e.setUseInteractedBlock(Result.DENY);
		e.setUseItemInHand(Result.DENY);
		e.setCancelled(true);
	}
	
}
