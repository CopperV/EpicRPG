package me.Vark123.EpicRPG.Players;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RpgScoreboard;
import me.Vark123.EpicRPG.Core.ExpSystem;
import me.Vark123.EpicRPG.Players.Components.RpgJewelry;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgReputation;
import me.Vark123.EpicRPG.Players.Components.RpgRzemiosla;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Players.Components.RpgVault;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Utils.Utils;

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
	private RpgJewelry jewelry;
	private RpgReputation reputation;
	
	private Scoreboard board;
	
	public RpgPlayer(Player p) {
		this.player = p;
		this.info = new RpgPlayerInfo(this);
		this.stats = new RpgStats(this);
		this.rzemiosla = new RpgRzemiosla(this);
		this.modifiers = new RpgModifiers(this);
		this.skills = new RpgSkills(this);
		this.vault = new RpgVault(this);
		//TODO
		this.jewelry = new RpgJewelry(this);
		this.reputation = new RpgReputation(this);
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		
//		createScoreboard();
		createDisplay();
	}
	
	public RpgPlayer(Player p, ResultSet set) {
		this.player = p;
		try {
			set.first();
			this.info = new RpgPlayerInfo(this, set);
			this.stats = new RpgStats(this, set);
			this.rzemiosla = new RpgRzemiosla(this, set);
			this.modifiers = new RpgModifiers(this);
			this.skills = new RpgSkills(this, set);
			this.vault = new RpgVault(this, set);
			//TODO
			this.jewelry = new RpgJewelry(this);
			this.reputation = new RpgReputation(this, set);
		} catch (SQLException e) {
			p.kickPlayer("Blad pobierania danych z bazy danych - zglos ten fakt administratorowi");
			e.printStackTrace();
		}
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		
//		createScoreboard();
		createDisplay();
	}
	
	public RpgPlayer(Player p, YamlConfiguration fYml) {
		this.player = p;
		this.info = new RpgPlayerInfo(this, fYml);
		this.stats = new RpgStats(this, fYml);
		this.rzemiosla = new RpgRzemiosla(this, fYml);
		this.modifiers = new RpgModifiers(this);
		this.skills = new RpgSkills(this, fYml);
		this.vault = new RpgVault(this, fYml);
		//TODO
		this.jewelry = new RpgJewelry(this);
		this.reputation = new RpgReputation(this, fYml);
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		
//		createScoreboard();
		createDisplay();
	}
	
	//TODO
	public void createScoreboard() {
		if(score != null && !score.isCancelled())
			return;
		RpgScoreboard.createScore(player);
		this.score = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), ()->{
			RpgScoreboard.updateScore(player);
		}, 0, 60);
	}
	
	//TODO
	private void createDisplay() {
		
	}
	
	//TODO
	public void displayUpdate() {
		
	}
	
	//TODO
	public void endTasks() {
		score.cancel();
		skills.endTasks();
	}
	
	//TODO
	public void resetStats() {
		
		ChangeStats.change(this);
	}
	
	//TODO
	public void resetCharacter() {
		resetStats();
	}
	
	public void updateBarLevel() {
		player.setLevel(info.getLevel());
	}
	
	public void updateBarExp() {
		float prevLvlExp = ExpSystem.getInstance().getNextLevelExp(info.getLevel() - 1);
		float presLvlExp = info.getNextLevel();
		float exp = info.getExp();
		exp = (float) Utils.normalizeValue(prevLvlExp, presLvlExp, exp);
		exp = (float) Utils.scaleValue(prevLvlExp, presLvlExp, 0, 1, exp);
		player.setExp(exp);
	}
	
	public void updateHp() {
		stats.setFinalHealth(stats.getPotionHealth() + stats.getHealth());
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(stats.getFinalHealth());
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

	public RpgJewelry getJewelry() {
		return jewelry;
	}

	public RpgReputation getReputation() {
		return reputation;
	}

	public Scoreboard getBoard() {
		return board;
	}
	
}
