package me.Vark123.EpicRPG;

import org.bukkit.plugin.java.JavaPlugin;

import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.MySQL.DBOperations;

public class Main extends JavaPlugin {

	private static Main instance;
	
	@Override
	public void onEnable() {
		instance = this;
		
		EventListenerManager.registerEvents();
		CommandExecutorManager.setExecutors();
		
		FileOperations.checkFiles();
		DBOperations.init();
		// TODO Auto-generated method stub
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		DBOperations.close();
		// TODO Auto-generated method stub
		super.onDisable();
	}

	public static Main getInstance() {
		return instance;
	}
	
}
