package me.Vark123.EpicRPG.Players;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;

public class RpgJewerly {

	
	public RpgJewerly(RpgPlayer rpg) {
		
	}
	
	public RpgJewerly(RpgPlayer rpg, ResultSet set) {
		try {
			set.first();
		} catch (SQLException e) {
			rpg.getPlayer().kickPlayer("Blad pobierania danych z bazy danych - zglos ten fakt administratorowi");
			e.printStackTrace();
		}
	}
	
	public RpgJewerly(RpgPlayer rpg, YamlConfiguration fYml) {
		
	}
}
