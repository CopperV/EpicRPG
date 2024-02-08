package me.Vark123.EpicRPG.Players.BaseEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Core.Events.RpgPlayerJoinEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class PlayerJoinEvent implements Listener {

	@EventHandler
	public void onJoin(org.bukkit.event.player.PlayerJoinEvent e) {
//		Player p = e.getPlayer();
//		RpgPlayer rpg = PlayerManager.getInstance().loadPlayer(p);
//		
//		if(rpg.getInfo().getLevel() == 1) {
//			new BukkitRunnable() {
//				
//				@Override
//				public void run() {
//					p.getInventory().clear();
//					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), ()->{
//						String name = p.getName();
//						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s "+name+" TutorialStart1");
//						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s "+name+" TutorialStart2");
//						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s "+name+" SferaCorristo");
//						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s "+name+" Strzala");
//					}, 20);
//				}
//			}.runTaskLaterAsynchronously(Main.getInstance(), 20*3);
//		}

		
		new BukkitRunnable() {
			@Override
			public void run() {
				Player p = e.getPlayer();
				RpgPlayer rpg = PlayerManager.getInstance().loadPlayer(p);
				
				RpgPlayerJoinEvent event = new RpgPlayerJoinEvent(rpg, true);
				new BukkitRunnable() {
					@Override
					public void run() {
						Bukkit.getPluginManager().callEvent(event);
						if(event.isCancelled()) {
							p.kickPlayer("§cBlad podczas wczytywania danych. Zglos blad administratorowi");
							return;
						}
						
						
						if(rpg.getInfo().getLevel() == 1) {
							new BukkitRunnable() {
								@Override
								public void run() {
									p.getInventory().clear();
									Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), ()->{
										String name = p.getName();
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s "+name+" TutorialStart1");
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s "+name+" TutorialStart2");
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s "+name+" SferaCorristo");
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give -s "+name+" Strzala");
									}, 20);
								}
							}.runTaskLaterAsynchronously(Main.getInstance(), 20*3);
						}
					}
				}.runTaskAsynchronously(Main.getInstance());
				
			}
		}.runTask(Main.getInstance());
		
	}
	
}
