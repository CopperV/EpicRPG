package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.MySQL.DBOperations;
import me.Vark123.EpicRPG.Placeholders.PlayerPlaceholders;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.Components.Scoreboard.ScoreboardPlaceholders;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin {

	private static Main instance;

	public static Economy eco;
	public static Permission perm;
	
	private static PlaceholderExpansion playerPlaceholders;
	private static PlaceholderExpansion scoreboardPlaceholders;
	
	private final String prefix = "§7[§bEpicRPG§7]";
	
	@Getter
	private InventoryManager manager;
	@Getter
	private ProtocolManager protocolManager;
	
	@Getter
	private BukkitTask saveTask;
	
	@Override
	public void onEnable() {
		instance = this;
		
		FileOperations.checkFiles();
		Config.get().init();
		DBOperations.init();
		
		checkEco();
		checkPerm();
		
		playerPlaceholders = new PlayerPlaceholders();
		playerPlaceholders.register();
		scoreboardPlaceholders = new ScoreboardPlaceholders();
		scoreboardPlaceholders.register();
		
		manager = new InventoryManager(instance);
		manager.invoke();
		protocolManager = ProtocolLibrary.getProtocolManager();
		
		EventListenerManager.registerEvents();
		CommandExecutorManager.setExecutors();
		
		createSaveTask();
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if(saveTask != null && !saveTask.isCancelled())
			saveTask.cancel();
		
		Bukkit.getOnlinePlayers().stream()
			.map(PlayerManager.getInstance()::getRpgPlayer)
			.forEach(DBOperations::savePlayer);
		
		DBOperations.close();
		FileOperations.saveBlackrockCompleted();
		FileOperations.saveBoosters();
		EpicRPGMobManager.getInstance().clear();
		
		playerPlaceholders.unregister();
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
	
	private void createSaveTask() {
		this.saveTask = new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled())
					return;
				
				Bukkit.getOnlinePlayers().stream()
					.map(PlayerManager.getInstance()::getRpgPlayer)
					.forEach(DBOperations::savePlayer);
			}
		}.runTaskTimerAsynchronously(this, 0, Config.get().getSaveInterval());
	}

	public static Main getInstance() {
		return instance;
	}
	
}
