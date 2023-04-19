package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPG.Players.BaseEvents.PlayerJoinEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerQuitEvent;

public class EventListenerManager {

	private static final Main inst = Main.getInstance();
	
	public static void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitEvent(), inst);
	}
	
}
