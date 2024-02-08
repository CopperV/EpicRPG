package me.Vark123.EpicRPG.Players.Components;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Potions.RpgPlayerManaRegenEvent;
import me.Vark123.EpicRPG.Utils.ChatPrintable;
import me.Vark123.EpicRPG.Utils.TableGenerator;
import me.Vark123.EpicRPG.Utils.TableGenerator.Receiver;

@Data
public class RpgStats implements Serializable, ChatPrintable {

	private static final long serialVersionUID = -7990732324947933468L;

	@Setter(value = AccessLevel.NONE)
	private RpgPlayer rpg;
	
	private int ochrona;
	private int obrazenia;
	private int sila = 3;
	private int zrecznosc = 3;
	private int wytrzymalosc = 3;
	private int zdolnosciMysliwskie = 3;
	private int walka = 3;
	private int mana = 7;
	private int inteligencja = 3;
	private int health = 100;

	@Setter(value = AccessLevel.NONE)
	private int potionOchrona;
	@Setter(value = AccessLevel.NONE)
	private int potionObrazenia;
	@Setter(value = AccessLevel.NONE)
	private int potionSila;
	@Setter(value = AccessLevel.NONE)
	private int potionZrecznosc;
	@Setter(value = AccessLevel.NONE)
	private int potionWytrzymalosc;
	@Setter(value = AccessLevel.NONE)
	private int potionZdolnosciMysliwskie;
	@Setter(value = AccessLevel.NONE)
	private int potionWalka;
	@Setter(value = AccessLevel.NONE)
	private int potionMana;
	@Setter(value = AccessLevel.NONE)
	private int potionInteligencja;
	@Setter(value = AccessLevel.NONE)
	private int potionHealth;

	private int finalOchrona;
	private int finalObrazenia;
	private int finalSila;
	private int finalZrecznosc;
	private int finalWytrzymalosc;
	private int finalZdolnosciMysliwskie;
	private int finalWalka;
	private int finalMana;
	private int finalInteligencja;
	private int finalHealth;

	@Setter(value = AccessLevel.NONE)
	private int presentMana = 7;
	private int krag;

	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BukkitTask regenHpTask;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private BukkitTask regenManaTask;
	
	public RpgStats(RpgPlayer rpg) {
		this.rpg = rpg;
	}
	
	public RpgStats(RpgPlayer rpg, ResultSet set) throws SQLException {
		this.rpg = rpg;
		set.first();
		
		this.sila = set.getInt("player_stats.p_str");
		this.zrecznosc = set.getInt("player_stats.p_zr");
		this.wytrzymalosc = set.getInt("player_stats.p_wytrz");
		this.zdolnosciMysliwskie = set.getInt("player_stats.p_zd");
		this.walka = set.getInt("player_stats.p_walka");
		this.mana = set.getInt("player_stats.p_mana");
		this.inteligencja = set.getInt("player_stats.p_int");
		this.health = set.getInt("player_info.health");
		
		this.potionSila = set.getInt("player_stats.potion_str");
		this.potionWytrzymalosc = set.getInt("player_stats.potion_wytrz");
		this.potionZrecznosc = set.getInt("player_stats.potion_zr");
		this.potionZdolnosciMysliwskie = set.getInt("player_stats.potion_zd");
		this.potionMana = set.getInt("player_stats.potion_mana");
		this.potionInteligencja = set.getInt("player_stats.potion_int");
		this.potionWalka = set.getInt("player_stats.potion_walka");
		this.potionHealth = set.getInt("player_info.p_health");

		this.krag = set.getInt("player_stats.p_krag");
		this.presentMana = set.getInt("player_stats.p_mana");
		boolean checkHP = set.getBoolean("player_info.check_hp");
		if(!checkHP) {
			int hpPerLevel = 5;
			if(rpg.getInfo().getProffesion().equalsIgnoreCase("§5mag"))
				hpPerLevel = 6;
			int maxHp = rpg.getInfo().getLevel()*hpPerLevel - hpPerLevel + 100;
			if(maxHp != health)
				health = maxHp;
		}
	}
	
	public RpgStats(RpgPlayer rpg, YamlConfiguration fYml) {
		this.rpg = rpg;
		
		this.sila = fYml.getInt("sila");
		this.zrecznosc = fYml.getInt("zrecznosc");
		this.wytrzymalosc = fYml.getInt("wytrzymalosc");
		this.zdolnosciMysliwskie = fYml.getInt("zdolnosci");
		this.walka = fYml.getInt("walka");
		this.mana = fYml.getInt("mana");
		this.inteligencja = fYml.getInt("inteligencja");
		this.health = fYml.getInt("health");
		
		this.potionSila = fYml.getInt("potion_sila");
		this.potionWytrzymalosc = fYml.getInt("potion_wytrzymalosc");
		this.potionZrecznosc = fYml.getInt("potion_zrecznosc");
		this.potionZdolnosciMysliwskie = fYml.getInt("potion_zdolnosci");
		this.potionMana = fYml.getInt("potion_mana");
		this.potionInteligencja = fYml.getInt("potion_inteligencja");
		this.potionWalka = fYml.getInt("potion_mana");
		this.potionHealth = fYml.getInt("potion_health");
		
		this.presentMana = fYml.getInt("present_mana");
		this.krag = fYml.getInt("krag");
	}
	
	public void createRegenHpTask(int seconds, double hp) {
		if(regenHpTask != null && !regenHpTask.isCancelled()) {
			regenHpTask.cancel();
		}
		regenHpTask = new BukkitRunnable() {
			int sec = seconds;
			@Override
			public void run() {
				if(sec <= 0) {
					Player p = rpg.getPlayer();
					p.sendMessage(Main.getInstance().getPrefix()+" §eEfekt czasowej mikstury zycia skonczyl sie!");
					p.spawnParticle(Particle.HEART, p.getLocation().add(0,1,0), 25, .6, .6, .6, 0.2);
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 1.25f);
					this.cancel();
					return;
				}
				--sec;
				RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hp);
				Bukkit.getPluginManager().callEvent(event);
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
	
	public void createRegenManaTask(int seconds, int mana) {
		if(regenManaTask != null && !regenManaTask.isCancelled()) {
			regenManaTask.cancel();
		}
		regenManaTask = new BukkitRunnable() {
			int sec = seconds;
			@Override
			public void run() {
				if(sec <= 0) {
					Player p = rpg.getPlayer();
					p.sendMessage(Main.getInstance().getPrefix()+" §eEfekt czasowej mikstury many skonczyl sie!");
					p.spawnParticle(Particle.NAUTILUS, p.getLocation().add(0,1,0), 25, .6, .6, .6, 0.2);
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1.25f);
					this.cancel();
					return;
				}
				--sec;
				addPresentManaSmart(mana);
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
	
	public void addSila(int amount) {
		this.sila += amount;
	}
	public void addZrecznosc(int amount) {
		this.zrecznosc += amount;
	}
	public void addWytrzymalosc(int amount) {
		this.wytrzymalosc += amount;
	}
	public void addZdolnosci(int amount) {
		this.zdolnosciMysliwskie += amount;
	}
	public void addWalka(int amount) {
		this.walka += amount;
	}
	public void addMana(int amount) {
		this.mana += amount;
	}
	public void addInteligencja(int amount) {
		this.inteligencja += amount;
	}

	public void addPotionOchrona(int potionOchrona) {
		this.potionOchrona += potionOchrona;
	}

	public void addPotionObrazenia(int potionObrazenia) {
		this.potionObrazenia += potionObrazenia;
	}

	public void addPotionSila(int potionSila) {
		this.potionSila += potionSila;
	}

	public void addPotionZrecznosc(int potionZrecznosc) {
		this.potionZrecznosc += potionZrecznosc;
	}

	public void addPotionWytrzymalosc(int potionWytrzymalosc) {
		this.potionWytrzymalosc += potionWytrzymalosc;
	}

	public void addPotionZdolnosci(int potionZdolnosci) {
		this.potionZdolnosciMysliwskie += potionZdolnosci;
	}

	public void addPotionWalka(int potionWalka) {
		this.potionWalka += potionWalka;
	}

	public void addPotionMana(int potionMana) {
		this.potionMana += potionMana;
	}

	public void addPotionInteligencja(int potionInteligencja) {
		this.potionInteligencja += potionInteligencja;
	}

	public void addPotionHealth(int potionHealth) {
		this.potionHealth += potionHealth;
	}
	
	public void addPresentMana(int presentMana) {
		this.presentMana += presentMana;
	}
	
	public void removePresentMana(int presentMana) {
		this.presentMana -= presentMana;
	}
	
	public void addPresentManaSmart(int presentMana) {
		RpgPlayerManaRegenEvent event = new RpgPlayerManaRegenEvent(rpg, presentMana);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		presentMana = event.getRegen();
		if(presentMana + this.presentMana > this.finalMana) {
			this.presentMana = this.finalMana;
		}else {
			this.presentMana += presentMana;
		}
	}
	
	public void removePresentManaSmart(int presentMana) {
		if(this.presentMana - presentMana < 0) {
			this.presentMana = 0;
		}else {
			this.presentMana -= presentMana;
		}
	}
	
	public void resetMana() {
		this.presentMana = this.finalMana;
	}

	public void addKrag(int krag) {
		this.krag += krag;
	}

	public void reset() {
		this.sila = 3;
		this.zrecznosc = 3;
		this.wytrzymalosc = 3;
		this.zdolnosciMysliwskie = 3;
		this.walka = 0;
		this.mana = 7;
		this.presentMana = 7;
		this.inteligencja = 3;
		this.krag = 0;
	}
	
	@Override
	public void print(CommandSender sender) {
		int kryt = finalWalka;
		int bonusKryt = rpg.getInfo().getShortProf().equalsIgnoreCase("mys") ? 50 : 0;
		if(rpg.getSkills().hasCiosKrytyczny())
			bonusKryt += 25;
		kryt += bonusKryt;
		
		double percent1 = (double) kryt / 500. * 100.;
		double percent2 = (double) kryt / 2500. * 100.;
		
		TableGenerator generator = new TableGenerator(TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT);
		generator.addRow("", "§2Obrazenia: §a"+obrazenia+"§7/§a"+potionObrazenia+"§7/§a"+finalObrazenia, 
				"§2Ochrona: §a"+ochrona+"§7/§a"+potionOchrona+"§7/§a"+finalOchrona);
		generator.addRow("", "§2Sila: §a"+sila+"§7/§a"+potionSila+"§7/§a"+finalSila, 
				"§2Wytrzymalosc: §a"+wytrzymalosc+"§7/§a"+potionWytrzymalosc+"§7/§a"+finalWytrzymalosc);
		generator.addRow("", "§2Zrecznosc: §a"+zrecznosc+"§7/§a"+potionZrecznosc+"§7/§a"+finalZrecznosc, 
				"§2Zdolnosci mysliwskie: §a"+zdolnosciMysliwskie+"§7/§a"+potionZdolnosciMysliwskie+"§7/§a"+finalZdolnosciMysliwskie);
		generator.addRow("", "§2Inteligencja: §a"+inteligencja+"§7/§a"+potionInteligencja+"§7/§a"+finalInteligencja, 
				"§2Mana: §a"+presentMana+" §7/ §7(§a"+mana+"§7/§a"+potionMana+"§7/§a"+finalMana+"§7)");
		generator.addRow("", "§2Walka: §a"+walka+"§7/§a"+potionWalka+"§7/§a"+finalWalka, 
				"§2Zycie: §a"+health+"§7/§a"+potionHealth+"§7/§a"+finalHealth);
		generator.addRow("", "§2Krytyk §7[§2PVE§7]: §a"+String.format("%.1f",percent1)+"%",
				"§2Krytyk §7[§2PVP§7]: §a"+String.format("%.1f",percent2)+"%");
		generator.addRow("", "§2Krag: §a"+krag);
		List<String> lines = generator.generate(Receiver.CLIENT, true, true);
		
		
		sender.sendMessage("§6§l=========================");
		for(String s : lines) {
			sender.sendMessage(s);
		}
	}
	
}
