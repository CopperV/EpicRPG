package me.Vark123.EpicRPG;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Files.FileOperations;

@Getter
public final class Config {

	private static final Config inst = new Config();
	
	private DBConnection connectionData;
	private int saveInterval;

	private double maxCPS;
	private int measureDuration;
	
	private Config() {
		
	}
	
	public static final Config get() {
		return inst;
	}
	
	public void init() {
		File f = FileOperations.getConfig();
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
	
		String host = fYml.getString("DB.ip", "127.0.0.1")+":"+fYml.getString("DB.port","3306");
		String database = fYml.getString("DB.database");
		String user = fYml.getString("DB.user");
		String password = fYml.getString("DB.passwd");
		this.connectionData = new DBConnection(host, database, user, password);
	
		this.saveInterval = fYml.getInt("DB.save-interval", 300);
		
		this.maxCPS = fYml.getDouble("CPS.max", 12);
		this.measureDuration = fYml.getInt("CPS.measure-duration", 3);
	}
	
	@Getter
	@AllArgsConstructor
	public class DBConnection {
		private String host;
		private String database;
		private String user;
		private String password;
	}
	
}
