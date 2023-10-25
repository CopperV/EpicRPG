package me.Vark123.EpicRPG.Players;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RpgScoreboard;
import me.Vark123.EpicRPG.Core.ExpSystem;
import me.Vark123.EpicRPG.Core.Events.PlayerKlasaResetEvent;
import me.Vark123.EpicRPG.Core.Events.PlayerStatsResetEvent;
import me.Vark123.EpicRPG.Players.Components.RpgJewelry;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgReputation;
import me.Vark123.EpicRPG.Players.Components.RpgRzemiosla;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Players.Components.RpgVault;
import me.Vark123.EpicRPG.Players.Components.Compass.EpicCompass;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Utils.ChatPrintable;
import me.Vark123.EpicRPG.Utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

@Getter
public class RpgPlayer implements Serializable, ChatPrintable {

	private static final long serialVersionUID = 7700189518575363277L;

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
	private EpicCompass compass;
	
	public RpgPlayer(Player p) {
		this.player = p;
		this.info = new RpgPlayerInfo(this);
		this.stats = new RpgStats(this);
		this.rzemiosla = new RpgRzemiosla(this);
		this.modifiers = new RpgModifiers(this);
		this.skills = new RpgSkills(this);
		this.vault = new RpgVault(this);
		this.jewelry = new RpgJewelry(this);
		this.reputation = new RpgReputation(this);
		
		this.board = Bukkit.getScoreboardManager().getNewScoreboard();
		this.compass = new EpicCompass(this);
		
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
			this.jewelry = new RpgJewelry(this);
			this.reputation = new RpgReputation(this, set);
		} catch (SQLException e) {
			p.kickPlayer("Blad pobierania danych z bazy danych - zglos ten fakt administratorowi");
			e.printStackTrace();
		}
		
		this.board = Bukkit.getScoreboardManager().getNewScoreboard();
		this.compass = new EpicCompass(this);
		
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
		this.jewelry = new RpgJewelry(this);
		this.reputation = new RpgReputation(this, fYml);
		
		this.board = Bukkit.getScoreboardManager().getNewScoreboard();
		this.compass = new EpicCompass(this);
		
		createDisplay();
	}
	
	public void createScoreboard() {
		if(score != null && !score.isCancelled())
			return;
		RpgScoreboard.createScore(player);
		this.score = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), ()->{
			RpgScoreboard.updateScore(player);
		}, 0, 60);
	}
	
	private void createDisplay() {
		this.display = new BukkitRunnable() {
			
			@Override
			public void run() {
				displayUpdate();
			}
		}.runTaskTimer(Main.getInstance(), 0, 20*2);
	}
	
	public void displayUpdate() {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
				TextComponent.fromLegacyText("§4🗡 "+stats.getFinalObrazenia()
						+"  §c❤ "+((int)player.getHealth())+"/"
						+((int)player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
						+(player.getAbsorptionAmount() >= 1 ? "§6§l♰ §6"+((int)player.getAbsorptionAmount()):"")
						+"  §b✺ "+stats.getPresentMana()+"/"+stats.getFinalMana()
						+"  §2§l🛡 §2"+stats.getFinalOchrona()));
	}
	
	public void endTasks() {
		score.cancel();
		skills.endTasks();
		
		compass.getCompass().removeAll();
	}
	
	public boolean resetStats() {
		PlayerStatsResetEvent event = new PlayerStatsResetEvent(this);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return false;
		
		rzemiosla.reset();
		skills.reset();
		stats.reset();
		
		info.setPN(info.getLevel() * 10);
		
		ChangeStats.change(this);
		return true;
	}
	
	public boolean resetCharacter() {
		if(!resetStats())
			return false;
		PlayerKlasaResetEvent event = new PlayerKlasaResetEvent(this);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return false;
		info.setProffesion("§aobywatel");
		stats.setHealth(info.getLevel()*5 - 5 + 100);
		
		ChangeStats.change(this);
		
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(stats.getFinalHealth());
		return true;
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

	@Override
	public void print(CommandSender sender) {
		info.print(sender);
		stats.print(sender);
		rzemiosla.print(sender);
		skills.print(sender);
		reputation.print(sender);
		vault.print(sender);
	}
	
}
