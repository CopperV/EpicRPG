package me.Vark123.EpicRPG.Players;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Config;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RpgScoreboard;
import me.Vark123.EpicRPG.Core.ExpSystem;
import me.Vark123.EpicRPG.Core.Events.PlayerKlasaResetEvent;
import me.Vark123.EpicRPG.Core.Events.PlayerStatsResetEvent;
import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.Players.Components.RpgJewelry;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgReputation;
import me.Vark123.EpicRPG.Players.Components.RpgRzemiosla;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Players.Components.RpgVault;
import me.Vark123.EpicRPG.Players.Components.Compass.EpicCompass;
import me.Vark123.EpicRPG.Players.Components.Markers.EpicMarker;
import me.Vark123.EpicRPG.Players.Components.Scoreboard.EpicScoreboard;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Utils.ChatPrintable;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public class RpgPlayer implements Serializable, ChatPrintable {

	private static final long serialVersionUID = 7700189518575363277L;

	private Player player;
	
	@Deprecated
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
	
	@Setter
	private ItemStack backItem;
	
	private EpicCompass compass;
	private EpicScoreboard scoreboard;
	private EpicMarker marker;
	
	@Getter(value = AccessLevel.NONE)
	private EnemyHpBarInfo hpBarInfo;
	
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
		this.backItem = FileOperations.loadPlayerBackItem(p);
		
		this.compass = new EpicCompass(this);
		this.scoreboard = new EpicScoreboard(this);
		this.marker = new EpicMarker(this);
//		createDisplay();
//		updateBarExp();
//		updateBarLevel();
		
		this.hpBarInfo = new EnemyHpBarInfo(p);
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
			this.backItem = FileOperations.loadPlayerBackItem(p);
		} catch (SQLException e) {
			p.kickPlayer("Blad pobierania danych z bazy danych - zglos ten fakt administratorowi");
			e.printStackTrace();
		}
		
		this.compass = new EpicCompass(this);
		this.scoreboard = new EpicScoreboard(this);
		this.marker = new EpicMarker(this);
//		createDisplay();
//		updateBarExp();
//		updateBarLevel();
		
		this.hpBarInfo = new EnemyHpBarInfo(p);
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
		this.backItem = FileOperations.loadPlayerBackItem(p);
		
		this.compass = new EpicCompass(this);
		this.scoreboard = new EpicScoreboard(this);
		this.marker = new EpicMarker(this);
//		createDisplay();
//		updateBarExp();
//		updateBarLevel();
		
		this.hpBarInfo = new EnemyHpBarInfo(p);
	}
	
	public void createScoreboard() {
		RpgScoreboard.createScoreboard(player);
//		if(score != null && !score.isCancelled())
//			return;
//		this.board = Bukkit.getScoreboardManager().getNewScoreboard();
//		RpgScoreboard.createScore(player);
//		this.score = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), ()->{
//			RpgScoreboard.updateScore(player);
//		}, 0, 60);
	}
	
	public void removeScoreboard() {
		RpgScoreboard.removeScoreboard(player);
	}

	@SuppressWarnings("unused")
	@Deprecated
	private void createDisplay() {
//		this.display = new BukkitRunnable() {
//			
//			@Override
//			public void run() {
//				displayUpdate();
//			}
//		}.runTaskTimer(Main.getInstance(), 0, 20*2);
	}
	
	@Deprecated
	public void displayUpdate() {
//		StringBuilder actionMessage = new StringBuilder();
//		actionMessage.append("§4🗡 "+stats.getFinalObrazenia());
//		if(player.hasPotionEffect(PotionEffectType.WITHER)) {
//			actionMessage.append("  §8☠ ");
//			int lenght = String.valueOf((int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()).length();
//			for(int i = 0; i < lenght; ++i)
//				actionMessage.append("✘");
//			actionMessage.append("/");
//			for(int i = 0; i < lenght; ++i)
//				actionMessage.append("✘");
//		} else {
//			actionMessage.append("  §c❤ " + ((int) player.getHealth()) + "/"
//					+ ((int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
//			if(player.getAbsorptionAmount() >= 1)
//				actionMessage.append("  §6§l♰ §6"+((int)player.getAbsorptionAmount()));
//		}
//		actionMessage.append(getGamemodeInfo());
//		actionMessage.append("  §b✺ "+stats.getPresentMana()+"/"+stats.getFinalMana());
//		actionMessage.append("  §2§l🛡 §2"+stats.getFinalOchrona());
//		
//		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
//				TextComponent.fromLegacyText(actionMessage.toString()));
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private String getGamemodeInfo() {
//		if(!player.hasPermission("group.builder"))
//			return "";
//		
//		int gm;
//		switch(player.getGameMode()) {
//			case ADVENTURE:
//				gm = 2;
//				break;
//			case CREATIVE:
//				gm = 1;
//				break;
//			case SPECTATOR:
//				gm = 3;
//				break;
//			case SURVIVAL:
//				gm = 0;
//				break;
//			default:
//				return "";
//		}
//		
//		return "  §e☤ "+gm;
		return "";
	}
	
	public void endTasks() {
//		score.cancel();
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
		
		info.setPN(info.getLevel() * Config.get().getPnPerLevel());
		
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
	
	@Deprecated
	public void updateBarLevel() {
		player.setLevel(info.getLevel());
	}
	
	@Deprecated
	public void updateBarExp() {
		float prevLvlExp = ExpSystem.getInstance().getNextLevelExp(info.getLevel() - 1);
		float presLvlExp = info.getNextLevel();
		if(prevLvlExp > presLvlExp)
			prevLvlExp = 0;
		float exp = info.getExp();
		exp = (float) Utils.normalizeValue(prevLvlExp, presLvlExp, exp);
		exp = (float) Utils.scaleValue(prevLvlExp, presLvlExp, 0, 1, exp);
		player.setExp(exp);
	}
	
	public void updateHp() {
		stats.setFinalHealth(stats.getPotionHealth() + stats.getHealth());
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(stats.getFinalHealth());
	}
	
	public void updateEnemyHpBar(LivingEntity entity, double damage) {
		double newHp = entity.getHealth() - damage;
		hpBarInfo.updateHpBar(entity, newHp, 20*3);
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
	
	private class EnemyHpBarInfo {
		private BossBar hpInfo;
		private BukkitTask hpShowTask;
		
		public EnemyHpBarInfo(Player owner) {
			hpInfo = Bukkit.createBossBar(" ", BarColor.WHITE, BarStyle.SOLID);
			hpInfo.setVisible(false);
			hpInfo.addPlayer(owner);
			hpInfo.setProgress(1);
		}
		
		public void updateHpBar(LivingEntity entity, double newHp, int duration) {
			if(hpShowTask != null && !hpShowTask.isCancelled())
				hpShowTask.cancel();
			
			double maxHp = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			double percent = newHp / maxHp;
			
			double clampedPercent = Utils.normalizeValue(0, 1, percent);
			newHp = Math.max(newHp, 0);
			
			int armor = 0;
			if(entity instanceof Player) {
				RpgPlayer victimRpg = PlayerManager.getInstance().getRpgPlayer((Player) entity);
				armor = victimRpg.getStats().getFinalOchrona();
			} else if(MythicBukkit.inst().getMobManager().isMythicMob(entity)) {
				ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
				armor = (int) aMob.getArmor();
			}
			
			hpInfo.setVisible(true);
			hpInfo.setProgress(clampedPercent);
			hpInfo.setTitle(entity.getName()+" "
					+ "§c"+(int)Math.ceil(newHp)+" §4❤"
					+ (armor > 0 ? " §a"+Math.round(armor)+" §2🛡" : ""));
			hpShowTask = new BukkitRunnable() {
				@Override
				public void run() {
					hpInfo.setVisible(false);
				}
			}.runTaskLater(Main.getInstance(), duration);
		}
		
		
	}
	
}
