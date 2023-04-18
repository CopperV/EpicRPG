package me.Vark123.EpicRPG.Players;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class RpgPlayer {

	private Player player;
	
	private BukkitTask display;
	private BukkitTask score;
	
	private RpgPlayerInfo info;
	private RpgStats stats;
	private RpgRzemiosla rzemiosla;
	private RpgModifiers modifiers;
	private RpgSkills skills;
	private RpgVault vault;
	private RpgJewerly jewerly;
	private RpgReputation reputation;
	
	public RpgPlayer(Player p) {
		this.player = p;
		this.info = new RpgPlayerInfo(this);
		this.stats = new RpgStats(this);
		this.rzemiosla = new RpgRzemiosla(this);
		this.modifiers = new RpgModifiers(this);
		this.skills = new RpgSkills(this);
		this.vault = new RpgVault(this);
		//TODO
		this.jewerly = new RpgJewerly(this);
		this.reputation = new RpgReputation(this);
		createScoreboard();
		createDisplay();
	}
	
	public RpgPlayer(Player p, ResultSet set) {
		this.player = p;
		try {
			set.first();
			this.info = new RpgPlayerInfo(this, set);
			this.stats = new RpgStats(this, set);
			this.rzemiosla = new RpgRzemiosla(this, set);
			this.modifiers = new RpgModifiers(this, set);
			this.skills = new RpgSkills(this, set);
			this.vault = new RpgVault(this, set);
			//TODO
			this.jewerly = new RpgJewerly(this, set);
			this.reputation = new RpgReputation(this, set);
		} catch (SQLException e) {
			p.kickPlayer("Blad pobierania danych z bazy danych - zglos ten fakt administratorowi");
			e.printStackTrace();
		}
		createScoreboard();
		createDisplay();
	}
	
	public RpgPlayer(Player p, YamlConfiguration fYml) {
		this.player = p;
		this.info = new RpgPlayerInfo(this, fYml);
		this.stats = new RpgStats(this, fYml);
		this.rzemiosla = new RpgRzemiosla(this, fYml);
		this.modifiers = new RpgModifiers(this, fYml);
		this.skills = new RpgSkills(this, fYml);
		this.vault = new RpgVault(this, fYml);
		//TODO
		this.jewerly = new RpgJewerly(this, fYml);
		this.reputation = new RpgReputation(this, fYml);
		createScoreboard();
		createDisplay();
	}
	
	//TODO
	private void createScoreboard() {
		
	}
	
	//TODO
	private void createDisplay() {
		
	}
	
	//TODO
	public void displayUpdate() {
		
	}
	
	//TODO
	public void endTasks() {
		
	}
	
	//TODO
	public void resetStats() {
		
	}
	
	//TODO
	public void resetCharacter() {
		resetStats();
	}
	
	//TODO
	public void updateBarLevel() {
		
	}
	
	//TODO
	public void updateBarExp() {
		
	}

	public Player getPlayer() {
		return player;
	}

	public BukkitTask getDisplay() {
		return display;
	}

	public BukkitTask getScore() {
		return score;
	}

	public RpgPlayerInfo getInfo() {
		return info;
	}

	public RpgStats getStats() {
		return stats;
	}

	public RpgRzemiosla getRzemiosla() {
		return rzemiosla;
	}

	public RpgModifiers getModifiers() {
		return modifiers;
	}

	public RpgSkills getSkills() {
		return skills;
	}

	public RpgVault getVault() {
		return vault;
	}

	public RpgJewerly getJewerly() {
		return jewerly;
	}

	public RpgReputation getReputation() {
		return reputation;
	}
	
}
