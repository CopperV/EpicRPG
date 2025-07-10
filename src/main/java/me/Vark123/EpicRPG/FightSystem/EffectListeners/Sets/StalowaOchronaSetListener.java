package me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class StalowaOchronaSetListener implements Listener {

	@EventHandler
	public void onDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player player))
			return;

		Entity damager = e.getDamager();
		if(damager == null)
			return;
		
		if(!MythicBukkit.inst().getMobManager().isActiveMob(damager.getUniqueId()))
			return;

		ActiveMob aMob = MythicBukkit.inst().getMobManager().getActiveMob(damager.getUniqueId()).get();
		if(aMob.hasThreatTable()) {
			if(!aMob.getThreatTable().getTopThreatHolder().getBukkitEntity().getUniqueId().equals(player.getUniqueId()))
				return;
		} else {
			if(aMob.getNewTarget() == null ||
					!aMob.getNewTarget().getBukkitEntity().getUniqueId().equals(player.getUniqueId()))
				return;
		}
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		if(info.getSetCounts().getOrDefault("Stalowa_Ochrona", 0) < 2)
			return;
		e.decreaseModifier(0.01);
	}
	
}
