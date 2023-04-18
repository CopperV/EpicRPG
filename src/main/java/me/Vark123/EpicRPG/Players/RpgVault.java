package me.Vark123.EpicRPG.Players;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class RpgVault {

	
	public RpgVault(RpgPlayer rpg) {
		
	}
	
	public RpgVault(RpgPlayer rpg, ResultSet set) {
		try {
			set.first();
		} catch (SQLException e) {
			rpg.getPlayer().kickPlayer("Blad pobierania danych z bazy danych - zglos ten fakt administratorowi");
			e.printStackTrace();
		}
	}
	
	public RpgVault(RpgPlayer rpg, YamlConfiguration fYml) {
		
	}
}
