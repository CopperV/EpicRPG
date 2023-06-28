package me.Vark123.EpicRPG.BlackrockSystem.Events;

import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.BlackrockSystem.BlackRockOperations;
import me.Vark123.EpicRPG.BlackrockSystem.BlackrockEvent;
import me.Vark123.EpicRPG.BlackrockSystem.BlackrockManager;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class BlackrockEntryEvent implements Listener {

	@EventHandler
	public void onEntry(BlackrockEvent e) {
		if(e.isCancelled())
			return;
		if(!e.getOperation().equals(BlackRockOperations.ENTRY))
			return;
		
		Player p = e.getPlayer();
		if(BlackrockManager.getInstance().getEntryCooldowns().containsKey(p)) {
			long timeNow = new Date().getTime();
			long timeOld = BlackrockManager.getInstance().getEntryCooldowns().get(p).getTime();
			if((timeNow - timeOld) < BlackrockManager.getInstance().ENTRY_COOLDOWN) {
				e.setCancelled(true);
				return;
			}
		}
		BlackrockManager.getInstance().getEntryCooldowns().put(p, new Date());
		
		if(BlackrockManager.getInstance().isCompletedDailyBlackrock(p)) {
			e.setCancelled(true);
			return;
		}
		
		if(!PlayerManager.getInstance().playerExists(p)) {
			e.setCancelled(true);
			return;
		}
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(!rpg.getVault()
				.hasEnoughDragonCoins(BlackrockManager.getInstance().BLACKROCK_ENTRY_PRICE)) {
			e.setCancelled(true);
			return;
		}
		
		rpg.getVault().removeDragonCoins(BlackrockManager.getInstance().BLACKROCK_ENTRY_PRICE);
		
	}
	
}
