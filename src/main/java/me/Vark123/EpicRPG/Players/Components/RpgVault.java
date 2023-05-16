package me.Vark123.EpicRPG.Players.Components;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class RpgVault {

	private RpgPlayer rpg;
	
	private int stygia;
	private int dragonCoins;
	private int brylkiRudy;
	
	public RpgVault(RpgPlayer rpg) {
		this.rpg = rpg;
	}
	
	public RpgVault(RpgPlayer rpg, ResultSet set) throws SQLException {
		this.rpg = rpg;
		stygia = set.getInt("player_stats.p_stygia");
		dragonCoins = set.getInt("player_stats.p_coins");
		brylkiRudy = set.getInt("player_stats.p_brylki");
		set.first();
	}
	
	public RpgVault(RpgPlayer rpg, YamlConfiguration fYml) {
		this.rpg = rpg;
		if(fYml.contains("stygia")) {
			stygia = fYml.getInt("stygia");
		}
		if(fYml.contains("coins")) {
			dragonCoins = fYml.getInt("coins");
		}
		if(fYml.contains("brylki")) {
			brylkiRudy = fYml.getInt("brylki");
		}
		
	}

	public RpgPlayer getRpg() {
		return rpg;
	}
	
	public double getMoney() {
		return Main.eco.getBalance(rpg.getPlayer());
	}
	
	public boolean hasEnoughMoney(double money) {
		return money<=Main.eco.getBalance(rpg.getPlayer());
	}
	
	public void removeMoney(double money) {
		Main.eco.withdrawPlayer(rpg.getPlayer(), money);
	}
	
	public void addMoney(double money) {
		Main.eco.depositPlayer(rpg.getPlayer(), money);
	}

	public int getStygia() {
		return stygia;
	}
	
	public boolean hasEnoughStygia(int stygia) {
		return stygia<=this.stygia;
	}
	
	public void removeStygia(int stygia) {
		this.stygia -= stygia;
	}
	
	public void addStygia(int stygia) {
		this.stygia += stygia;
	}

	public int getDragonCoins() {
		return dragonCoins;
	}
	
	public boolean hasEnoughDragonCoins(int coins) {
		return coins <= this.dragonCoins;
	}
	
	public void addDragonCoins(int coins) {
		this.dragonCoins += coins;
	}
	
	public void removeDragonCoins(int coins) {
		this.dragonCoins -= coins;
	}

	public int getBrylkiRudy() {
		return brylkiRudy;
	}
	
	public boolean hasEnoughBrylkiRudy(int brylkiRudy) {
		return brylkiRudy <= this.brylkiRudy;
	}
	
	public void addBrylkiRudy(int brylkiRudy) {
		this.brylkiRudy += brylkiRudy;
	}
	
	public void removeBrylkiRudy(int brylkiRudy) {
		this.brylkiRudy -= brylkiRudy;
	}
	
}
