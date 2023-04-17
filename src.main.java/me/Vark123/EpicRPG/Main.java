package me.Vark123.EpicRPG;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static Main instance;
	
	@Override
	public void onEnable() {
		instance = this;
		EventListenerManager.registerEvents();
		CommandExecutorManager.setExecutors();
		// TODO Auto-generated method stub
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}

	public static Main getInstace() {
		return instance;
	}
	
}
