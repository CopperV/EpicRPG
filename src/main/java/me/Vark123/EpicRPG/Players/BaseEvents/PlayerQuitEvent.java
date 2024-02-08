package me.Vark123.EpicRPG.Players.BaseEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.MySQL.DBOperations;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class PlayerQuitEvent implements Listener {

	@EventHandler
	public void onQuit(org.bukkit.event.player.PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		new BukkitRunnable() {
			@Override
			public void run() {
				RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
				rpg.endTasks();
				DBOperations.savePlayer(rpg);
				FileOperations.savePlayerJewerly(rpg);
				FileOperations.savePlayerBackItem(rpg);
				PlayerManager.getInstance().removePlayer(p);
			}
		}.runTaskAsynchronously(Main.getInstance());

		if(p.isInsideVehicle()) {
			p.leaveVehicle();
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		new BukkitRunnable() {
			@Override
			public void run() {
				RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
				rpg.endTasks();
				DBOperations.savePlayer(rpg);
				FileOperations.savePlayerJewerly(rpg);
				FileOperations.savePlayerBackItem(rpg);
				PlayerManager.getInstance().removePlayer(p);
			}
		}.runTaskAsynchronously(Main.getInstance());
		
		if(p.isInsideVehicle()) {
			p.leaveVehicle();
		}
	}
	
}
