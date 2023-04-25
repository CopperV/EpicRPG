package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPG.Chat.ChatMsgSendEvent;
import me.Vark123.EpicRPG.FightSystem.RpgDamageEvent;
import me.Vark123.EpicRPG.FightSystem.RpgDeathEvent;
import me.Vark123.EpicRPG.FightSystem.ShootEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerJoinEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerQuitEvent;

public class EventListenerManager {

	private static final Main inst = Main.getInstance();
	
	public static void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new ChatMsgSendEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new ShootEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RpgDamageEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RpgDeathEvent(), inst);
	}
	
}
