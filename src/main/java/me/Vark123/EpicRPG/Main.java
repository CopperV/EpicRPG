package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.MySQL.DBOperations;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin {

	private static Main instance;

	public static Economy eco;
	public static Permission perm;
	
	@Override
	public void onEnable() {
		instance = this;
		
		EventListenerManager.registerEvents();
		CommandExecutorManager.setExecutors();
		
		FileOperations.checkFiles();
		DBOperations.init();
		
		checkEco();
		checkPerm();
		
		// TODO Auto-generated method stub
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		DBOperations.close();
		EpicRPGMobManager.getInstance().clear();
		// TODO Auto-generated method stub
		super.onDisable();
	}
	
	private boolean checkEco() {
		RegisteredServiceProvider<Economy> ecop = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if(ecop == null) {
			return false;
		}
		eco = ecop.getProvider();
		if(eco == null) {
			return false;
		}
		return true;
	}
	
	private boolean checkPerm() {
		RegisteredServiceProvider<Permission> ecop = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
		if(ecop == null) {
			return false;
		}
		perm = ecop.getProvider();
		if(perm == null) {
			return false;
		}
		return true;
	}

	public static Main getInstance() {
		return instance;
	}
	
}
