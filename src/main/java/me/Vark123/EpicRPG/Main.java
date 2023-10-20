package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.MySQL.DBOperations;
import me.Vark123.EpicRPG.Placeholders.PlayerPlaceholders;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin {

	private static Main instance;

	public static Economy eco;
	public static Permission perm;
	
	private static PlaceholderExpansion playerPlaceholders;
	
	private final String prefix = "§7[§bEpicRPG§7]";
	
	@Getter
	private InventoryManager manager;
	@Getter
	private ProtocolManager protocolManager;
	
	@Override
	public void onEnable() {
		instance = this;
		
		FileOperations.checkFiles();
		DBOperations.init();
		
		checkEco();
		checkPerm();
		
		playerPlaceholders = new PlayerPlaceholders();
		playerPlaceholders.register();
		
		manager = new InventoryManager(instance);
		manager.invoke();
		protocolManager = ProtocolLibrary.getProtocolManager();
		
		EventListenerManager.registerEvents();
		CommandExecutorManager.setExecutors();
		// TODO Auto-generated method stub
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		DBOperations.close();
		FileOperations.saveBlackrockCompleted();
		FileOperations.saveBoosters();
		EpicRPGMobManager.getInstance().clear();
		
		playerPlaceholders.unregister();
		// TODO Auto-generated method stub
		super.onDisable();
	}
	
	public final String getPrefix() {
		return prefix;
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
