package me.Vark123.EpicRPG.Players.Components;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;

import me.Vark123.EpicRPG.Players.RpgPlayer;

public class RpgPlayerInfo {

	private RpgPlayer rpg;
	
	private int level = 1;
	private int exp = 0;
	private int nextLevel;
	private int pn = 10;
	private String proffesion = "§aobywatel";
	
	private boolean drop = false;
	private boolean tutorial = true;
	
	public RpgPlayerInfo(RpgPlayer rpg) {
		this.rpg = rpg;
		this.nextLevel = 500;
//		this.nextLevel = RpgSystem.getNextLevelExp(level);
	}
	
	public RpgPlayerInfo(RpgPlayer rpg, ResultSet set) throws SQLException {
		this.rpg = rpg;
		set.first();
		this.level = set.getInt("player_stats.level");
		this.exp = set.getInt("player_stats.exp");
		this.nextLevel = set.getInt("player_stats.nextLevel");
//		this.nextLevel = RpgSystem.getNextLevelExp(level);
		this.pn = set.getInt("player_stats.pn");
		this.proffesion = set.getString("player_stats.klasa");
		
		this.drop = set.getBoolean("player_info.item_drop");
		this.tutorial = set.getBoolean("player_info.tutorial");
		if(tutorial) this.drop = false;
	}
	
	public RpgPlayerInfo(RpgPlayer rpg, YamlConfiguration fYml) {
		this.rpg = rpg;
		this.level = fYml.getInt("level");
		this.exp = fYml.getInt("exp");
		this.nextLevel = fYml.getInt("nextLevel");
//		this.nextLevel = RpgSystem.getNextLevelExp(level);
		this.proffesion = fYml.getString("proffesion");
		this.pn = fYml.getInt("PN");
		
		if(fYml.contains("drop"))
			this.drop = fYml.getBoolean("drop");
		else
			this.drop = true;
		
		if(fYml.contains("tutorial")) {
			this.tutorial = fYml.getBoolean("tutorial");
			if(tutorial) this.drop = false;
		}else {
			this.tutorial = true;
			this.drop = false;
		}
	}

	public RpgPlayer getRpg() {
		return rpg;
	}

	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public int getNextLevel() {
		return nextLevel;
	}

	public int getPn() {
		return pn;
	}

	public String getProffesion() {
		return proffesion;
	}
	
	public String getShortProf() {
		return proffesion.substring(0, 5);
	}

	public boolean isDrop() {
		return drop;
	}

	public boolean isTutorial() {
		return tutorial;
	}
	
	public void addXP(int xp) {
		this.exp+=xp;
	}
	
	public void setXP(int xp) {
		this.exp=xp;
	}
	
	public void addLevel(int lvl) {
		this.level += lvl;
	}
	
	public void setNextLevel(int xp) {
		this.nextLevel = xp;
	}

	public void addPN(int pn) {
		this.pn += pn;
	}
	
	public void removePN(int pn) {
		this.pn -= pn;
	}

	public void setDrop(boolean drop) {
		this.drop = drop;
	}

	public void setTutorial(boolean tutorial) {
		this.tutorial = tutorial;
	}
}
