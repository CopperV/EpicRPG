package me.Vark123.EpicRPG.Players.Components;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;

import me.Vark123.EpicRPG.Players.RpgPlayer;

public class RpgRzemiosla {
	
	private RpgPlayer rpg;
	
	private boolean alchemia;
	private boolean kowalstwo;
	private boolean platnerstwo;
	private boolean luczarstwo;
	private boolean jubilerstwo;
	
	public RpgRzemiosla(RpgPlayer rpg) {
		this.rpg = rpg;
	}
	
	public RpgRzemiosla(RpgPlayer rpg, ResultSet set) throws SQLException {
		this.rpg = rpg;
		set.first();
		this.alchemia = set.getBoolean("player_rzemioslo.alchemia");
		this.platnerstwo = set.getBoolean("player_rzemioslo.platnerstwo");
		this.kowalstwo = set.getBoolean("player_rzemioslo.kowalstwo");
		this.luczarstwo = set.getBoolean("player_rzemioslo.luczarstwo");
		this.jubilerstwo = set.getBoolean("player_rzemioslo.jubilerstwo");
	}
	
	public RpgRzemiosla(RpgPlayer rpg, YamlConfiguration fYml) {
		this.rpg = rpg;
		this.alchemia = fYml.getBoolean("alchemia");
		this.platnerstwo = fYml.getBoolean("platnerstwo");
		this.kowalstwo = fYml.getBoolean("kowalstwo");
		this.luczarstwo = fYml.getBoolean("luczarstwo");
		this.jubilerstwo = fYml.getBoolean("jubilerstwo");
	}

	public RpgPlayer getRpg() {
		return rpg;
	}

	public boolean hasAlchemia() {
		return alchemia;
	}

	public void setAlchemia(boolean alchemia) {
		this.alchemia = alchemia;
	}

	public boolean hasKowalstwo() {
		return kowalstwo;
	}

	public void setKowalstwo(boolean kowalstwo) {
		this.kowalstwo = kowalstwo;
	}

	public boolean hasPlatnerstwo() {
		return platnerstwo;
	}

	public void setPlatnerstwo(boolean platnerstwo) {
		this.platnerstwo = platnerstwo;
	}

	public boolean hasLuczarstwo() {
		return luczarstwo;
	}

	public void setLuczarstwo(boolean luczarstwo) {
		this.luczarstwo = luczarstwo;
	}

	public boolean hasJubilerstwo() {
		return jubilerstwo;
	}

	public void setJubilerstwo(boolean jubilerstwo) {
		this.jubilerstwo = jubilerstwo;
	}
	
}
