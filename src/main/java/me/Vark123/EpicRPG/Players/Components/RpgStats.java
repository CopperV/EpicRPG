package me.Vark123.EpicRPG.Players.Components;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class RpgStats {

	private RpgPlayer rpg;
	
	private int ochrona;
	private int obrazenia;
	private int sila = 3;
	private int zrecznosc = 3;
	private int wytrzymalosc = 3;
	private int zdolnosci = 3;
	private int walka = 3;
	private int mana = 7;
	private int inteligencja = 3;
	private int health = 100;

	private int potionOchrona;
	private int potionObrazenia;
	private int potionSila;
	private int potionZrecznosc;
	private int potionWytrzymalosc;
	private int potionZdolnosci;
	private int potionWalka;
	private int potionMana;
	private int potionInteligencja;
	private int potionHealth;

	private int finalOchrona;
	private int finalObrazenia;
	private int finalSila;
	private int finalZrecznosc;
	private int finalWytrzymalosc;
	private int finalZdolnosci;
	private int finalWalka;
	private int finalMana;
	private int finalInteligencja;
	private int finalHealth;
	
	private int presentMana = 7;
	private int krag;
	
	private BukkitTask regenHpTask;
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
		this.zdolnosci = set.getInt("player_stats.p_zd");
		this.walka = set.getInt("player_stats.p_walka");
		this.mana = set.getInt("player_stats.p_mana");
		this.inteligencja = set.getInt("player_stats.p_int");
		this.health = set.getInt("player_info.health");
		
		this.potionSila = set.getInt("player_stats.potion_str");
		this.potionWytrzymalosc = set.getInt("player_stats.potion_wytrz");
		this.potionZrecznosc = set.getInt("player_stats.potion_zr");
		this.potionZdolnosci = set.getInt("player_stats.potion_zd");
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
		this.zdolnosci = fYml.getInt("zdolnosci");
		this.walka = fYml.getInt("walka");
		this.mana = fYml.getInt("mana");
		this.inteligencja = fYml.getInt("inteligencja");
		this.health = fYml.getInt("health");
		
		this.potionSila = fYml.getInt("potion_sila");
		this.potionWytrzymalosc = fYml.getInt("potion_wytrzymalosc");
		this.potionZrecznosc = fYml.getInt("potion_zrecznosc");
		this.potionZdolnosci = fYml.getInt("potion_zdolnosci");
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
			
			@Override
			public void run() {
				if(seconds <= 0) {
					Player p = rpg.getPlayer();
					p.sendMessage(Main.getInstance().getPrefix()+" §eEfekt czasowej mikstury zycia skonczyl sie!");
					p.spawnParticle(Particle.HEART, p.getLocation().add(0,1,0), 25, .6, .6, .6, 0.2);
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 1.25f);
					this.cancel();
					return;
				}
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
			
			@Override
			public void run() {
				if(seconds <= 0) {
					Player p = rpg.getPlayer();
					p.sendMessage(Main.getInstance().getPrefix()+" §eEfekt czasowej mikstury many skonczyl sie!");
					p.spawnParticle(Particle.NAUTILUS, p.getLocation().add(0,1,0), 25, .6, .6, .6, 0.2);
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1.25f);
					this.cancel();
					return;
				}
				addPresentManaSmart(mana);
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

	public RpgPlayer getRpg() {
		return rpg;
	}

	public int getOchrona() {
		return ochrona;
	}

	public int getObrazenia() {
		return obrazenia;
	}

	public int getSila() {
		return sila;
	}

	public int getZrecznosc() {
		return zrecznosc;
	}

	public int getWytrzymalosc() {
		return wytrzymalosc;
	}

	public int getZdolnosci() {
		return zdolnosci;
	}

	public int getWalka() {
		return walka;
	}

	public int getMana() {
		return mana;
	}

	public int getInteligencja() {
		return inteligencja;
	}

	public int getHealth() {
		return health;
	}

	public int getPotionOchrona() {
		return potionOchrona;
	}

	public int getPotionObrazenia() {
		return potionObrazenia;
	}

	public int getPotionSila() {
		return potionSila;
	}

	public int getPotionZrecznosc() {
		return potionZrecznosc;
	}

	public int getPotionWytrzymalosc() {
		return potionWytrzymalosc;
	}

	public int getPotionZdolnosci() {
		return potionZdolnosci;
	}

	public int getPotionWalka() {
		return potionWalka;
	}

	public int getPotionMana() {
		return potionMana;
	}

	public int getPotionInteligencja() {
		return potionInteligencja;
	}

	public int getPotionHealth() {
		return potionHealth;
	}

	public int getFinalOchrona() {
		return finalOchrona;
	}

	public int getFinalObrazenia() {
		return finalObrazenia;
	}

	public int getFinalSila() {
		return finalSila;
	}

	public int getFinalZrecznosc() {
		return finalZrecznosc;
	}

	public int getFinalWytrzymalosc() {
		return finalWytrzymalosc;
	}

	public int getFinalZdolnosci() {
		return finalZdolnosci;
	}

	public int getFinalWalka() {
		return finalWalka;
	}

	public int getFinalMana() {
		return finalMana;
	}

	public int getFinalInteligencja() {
		return finalInteligencja;
	}

	public int getFinalHealth() {
		return finalHealth;
	}

	public int getPresentMana() {
		return presentMana;
	}

	public int getKrag() {
		return krag;
	}

	public void setOchrona(int ochrona) {
		this.ochrona = ochrona;
	}

	public void setObrazenia(int obrazenia) {
		this.obrazenia = obrazenia;
	}

	public void setSila(int sila) {
		this.sila = sila;
	}

	public void setZrecznosc(int zrecznosc) {
		this.zrecznosc = zrecznosc;
	}

	public void setWytrzymalosc(int wytrzymalosc) {
		this.wytrzymalosc = wytrzymalosc;
	}

	public void setZdolnosci(int zdolnosci) {
		this.zdolnosci = zdolnosci;
	}

	public void setWalka(int walka) {
		this.walka = walka;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public void setInteligencja(int inteligencja) {
		this.inteligencja = inteligencja;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setFinalOchrona(int finalOchrona) {
		this.finalOchrona = finalOchrona;
	}

	public void setFinalObrazenia(int finalObrazenia) {
		this.finalObrazenia = finalObrazenia;
	}

	public void setFinalSila(int finalSila) {
		this.finalSila = finalSila;
	}

	public void setFinalZrecznosc(int finalZrecznosc) {
		this.finalZrecznosc = finalZrecznosc;
	}

	public void setFinalWytrzymalosc(int finalWytrzymalosc) {
		this.finalWytrzymalosc = finalWytrzymalosc;
	}

	public void setFinalZdolnosci(int finalZdolnosci) {
		this.finalZdolnosci = finalZdolnosci;
	}

	public void setFinalWalka(int finalWalka) {
		this.finalWalka = finalWalka;
	}

	public void setFinalMana(int finalMana) {
		this.finalMana = finalMana;
	}

	public void setFinalInteligencja(int finalInteligencja) {
		this.finalInteligencja = finalInteligencja;
	}

	public void setFinalHealth(int finalHealth) {
		this.finalHealth = finalHealth;
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
		this.potionZdolnosci += potionZdolnosci;
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

	public void addKrag(int krag) {
		this.krag += krag;
	}
	
}
