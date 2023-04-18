package me.Vark123.EpicRPG.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.bukkit.configuration.file.FileConfiguration;

import me.Vark123.EpicRPG.Main;

public class DBOperations {

	private static boolean isInit = false;
	private static Connection c = null;
	
	public static void init() {
		if(isInit)
			return;
		FileConfiguration fc = Main.getInstance().getConfig();
		Properties prop = new Properties();
		prop.setProperty("user", fc.getString("DB.user"));
		prop.setProperty("password", fc.getString("DB.passwd"));
		prop.setProperty("autoReconnect", "true");
		try {
			c = DriverManager.getConnection("jdbc:mysql://"+fc.getString("DB.ip")+"/"+fc.getString("DB.database")+"?useSSL=false&autoReconnect=true&failOverReadOnly=false&maxReconnects=10",prop);
		} catch (SQLException e) {
			System.out.println("§c§lBlad laczenia z baza danych: "+e.getMessage());
			return;
		}
		isInit = true;
	}
	
	public static void close() {
		if(!isInit)
			return;
		try {
			c.close();
		} catch (SQLException e) {
			System.out.println("Blad zamykania polaczenia: "+e.getMessage());
		}
	}
	
}
