package me.Vark123.EpicRPG.Players;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class RpgReputation {

	
	public RpgReputation(RpgPlayer rpg) {
		
	}
	
	public RpgReputation(RpgPlayer rpg, ResultSet set) {
		try {
			set.first();
		} catch (SQLException e) {
			rpg.getPlayer().kickPlayer("Blad pobierania danych z bazy danych - zglos ten fakt administratorowi");
			e.printStackTrace();
		}
	}
	
	public RpgReputation(RpgPlayer rpg, YamlConfiguration fYml) {
		
	}
}
