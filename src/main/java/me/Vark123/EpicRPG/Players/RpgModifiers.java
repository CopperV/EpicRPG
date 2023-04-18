package me.Vark123.EpicRPG.Players;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;

public class RpgModifiers {

	public RpgModifiers(RpgPlayer rpg) {
		
	}
	
	public RpgModifiers(RpgPlayer rpg, ResultSet set) {
		try {
			set.first();
		} catch (SQLException e) {
			rpg.getPlayer().kickPlayer("Blad pobierania danych z bazy danych - zglos ten fakt administratorowi");
			e.printStackTrace();
		}
	}
	
	public RpgModifiers(RpgPlayer rpg, YamlConfiguration fYml) {
		
	}
}
