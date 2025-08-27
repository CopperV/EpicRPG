package me.Vark123.EpicRPG.RuneSystem.SummonSystem;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;

public class SummonDismissListener implements Listener {

	@EventHandler
	private void onClick(PlayerInteractAtEntityEvent e) {
		if(e.isCancelled())
			return;
		
		Player player = e.getPlayer();
		Entity entity = e.getRightClicked();
		if(!player.isSneaking())
			return;
		
		AbstractEntity aEntity = BukkitAdapter.adapt(entity);
		if(!MythicBukkit.inst().getMobManager().isActiveMob(aEntity))
			return;
		
		ActiveMob summon = MythicBukkit.inst().getMobManager().getMythicMobInstance(aEntity);
		if(!summon.getOwnerUUID().isPresent() || !summon.getOwnerUUID().get().equals(player.getUniqueId()))
			return;
		
		SummonManager.get().removeSummon(summon);
	}
	
}
