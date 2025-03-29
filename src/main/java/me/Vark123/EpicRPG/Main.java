package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.github.retrooper.packetevents.PacketEvents;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import me.Vark123.EpicInventory.Pagination.InventoryManager;
import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.MySQL.DBOperations;
import me.Vark123.EpicRPG.Placeholders.MiscPlaceholders;
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
	private static PlaceholderExpansion miscPlaceholders;
	
	private final String prefix = "§7[§bEpicRPG§7]";
	
	@Getter
	private InventoryManager manager;
	
	@Getter
	private BukkitTask saveTask;

	@Override
	public void onLoad() {
		PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
		PacketEvents.getAPI().load();
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		PacketEvents.getAPI().init();
		
		manager = new InventoryManager(instance);
		manager.invoke();
		
		FileOperations.checkFiles();
		Config.get().init();
		DBOperations.init();
		
		checkEco();
		checkPerm();
		
		playerPlaceholders = new PlayerPlaceholders();
		playerPlaceholders.register();
		scoreboardPlaceholders = new ScoreboardPlaceholders();
		scoreboardPlaceholders.register();
		miscPlaceholders = new MiscPlaceholders();
		miscPlaceholders.register();
		
		EventListenerManager.registerEvents();
		CommandExecutorManager.setExecutors();
		
		RpgScoreboard.startAutoUpdate(instance);
		
		createSaveTask();
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		PacketEvents.getAPI().terminate();
		
		if(saveTask != null && !saveTask.isCancelled())
			saveTask.cancel();
		
//		Bukkit.getOnlinePlayers().stream()
//			.map(PlayerManager.getInstance()::getRpgPlayer)
//			.forEach(DBOperations::savePlayer);
		
		PlayerManager.getInstance().getPlayerContainer().values()
			.stream()
			.forEach(rpg -> {
				rpg.endTasks();
				DBOperations.savePlayer(rpg);
				FileOperations.savePlayerJewerly(rpg);
				FileOperations.savePlayerBackItem(rpg);
				DBOperations.savePlayer(rpg);
				rpg.getPlayer().kickPlayer("Restart serwera");
			});

		PlayerManager.getInstance().getPlayerContainer().clear();
		DBOperations.close();
		FileOperations.saveBlackrockCompleted();
		FileOperations.saveBoosters();
		EpicRPGMobManager.getInstance().clear();
		
		playerPlaceholders.unregister();
		scoreboardPlaceholders.unregister();
		miscPlaceholders.unregister();
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
