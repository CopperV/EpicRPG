package me.Vark123.EpicRPG.Players.Components;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.ChatPrintable;
import me.Vark123.EpicRPG.Utils.TableGenerator;
import me.Vark123.EpicRPG.Utils.TableGenerator.Receiver;

@Getter
public class RpgVault implements Serializable, ChatPrintable {

	private static final long serialVersionUID = -2277804457326795470L;

	private RpgPlayer rpg;
	
	private int stygia;
	private int dragonCoins;
	private int brylkiRudy;
	
	private int eventCurrency;
	private int eventCurrency2;
	
	public RpgVault(RpgPlayer rpg) {
		this.rpg = rpg;
	}
	
	public RpgVault(RpgPlayer rpg, ResultSet set) throws SQLException {
		this.rpg = rpg;
		stygia = set.getInt("player_stats.p_stygia");
		dragonCoins = set.getInt("player_stats.p_coins");
		brylkiRudy = set.getInt("player_stats.p_brylki");
		this.eventCurrency = set.getInt("player_stats.p_event");
		this.eventCurrency2 = set.getInt("player_stats.p_event2");
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
	
	public boolean hasEnoughStygia(int stygia) {
		return stygia<=this.stygia;
	}
	
	public void removeStygia(int stygia) {
		this.stygia -= stygia;
	}
	
	public void addStygia(int stygia) {
		this.stygia += stygia;
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
	
	public boolean hasEnoughBrylkiRudy(int brylkiRudy) {
		return brylkiRudy <= this.brylkiRudy;
	}
	
	public void addBrylkiRudy(int brylkiRudy) {
		this.brylkiRudy += brylkiRudy;
	}
	
	public void removeBrylkiRudy(int brylkiRudy) {
		this.brylkiRudy -= brylkiRudy;
	}
	
	public int getEventCurrency() {
		return this.eventCurrency;
	}
	
	public boolean hasEnoughEventCurrency(int eventCurrency) {
		return eventCurrency <= this.eventCurrency;
	}
	
	public void addEventCurrency(int eventCurrency) {
		this.eventCurrency += eventCurrency;
	}
	
	public void removeEventCurrency(int eventCurrency) {
		this.eventCurrency -= eventCurrency;
	}
	
	public boolean hasEnoughEventCurrency2(int eventCurrency) {
		return eventCurrency2 <= this.eventCurrency;
	}
	
	public void addEventCurrency2(int eventCurrency) {
		this.eventCurrency2 += eventCurrency;
	}
	
	public void removeEventCurrency2(int eventCurrency) {
		this.eventCurrency2 -= eventCurrency;
	}

	@Override
	public void print(CommandSender sender) {
		TableGenerator generator = new TableGenerator(TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT);
		generator.addRow("", "§2Kasa: §e"+String.format("%.2f", getMoney())+" $", "§2Smocze monety: §4"+dragonCoins);
		generator.addRow("", "§2Stygia: §3"+stygia, "§2Brylki rudy: §9"+brylkiRudy);
		generator.addRow("", "§2Karnet: §c"+eventCurrency, "§2Palemki: §a"+eventCurrency2);
		List<String> lines = generator.generate(Receiver.CLIENT, true, true);
		
		sender.sendMessage("§6§l========================= ");
		for(String s : lines) {
			sender.sendMessage(s);
		}
	}
	
}
