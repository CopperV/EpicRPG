package me.Vark123.EpicRPG.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgReputation;
import me.Vark123.EpicRPG.Players.Components.RpgRzemiosla;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class DBOperations {

	private static boolean isInit = false;
	private static Connection c = null;
	
	public static void init() {
		if(isInit)
			return;
		FileConfiguration fc = Main.getInstance().getConfig();
		Properties prop = new Properties();
		prop.setProperty("user", fc.getString("DB.user"));
		prop.setProperty("password", fc.getString("DB.passwd"));
		prop.setProperty("autoReconnect", "true");
		try {
			c = DriverManager.getConnection("jdbc:mysql://"+fc.getString("DB.ip")+"/"+fc.getString("DB.database")
				+"?useSSL=false&"
				+ "autoReconnect=true&"
				+ "failOverReadOnly=false&"
				+ "maxReconnects=10&"
				+ "enabledTLSProtocols=TLSv1.2",prop);
		} catch (SQLException e) {
			System.out.println("§c§lBlad laczenia z baza danych: "+e.getMessage());
			return;
		}
		isInit = true;
	}
	
	public static void close() {
		if(!isInit)
			return;
		try {
			c.close();
		} catch (SQLException e) {
			System.out.println("Blad zamykania polaczenia: "+e.getMessage());
		}
	}
	
	public static boolean playerExists(Player player) {
		String uuid = player.getUniqueId().toString();
		String polecenie = "SELECT nick FROM players WHERE UUID LIKE '"+uuid+"';";
		try {
			ResultSet result = c.createStatement().executeQuery(polecenie);
			if(result.next())
				return true;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static ResultSet getPlayer(Player player) {
		ResultSet toReturn = null;
		
		String polecenie = "SELECT players.nick, player_stats.klasa, player_stats.ranga, player_stats.level, player_stats.exp, player_stats.nextLevel, player_stats.pn," + 
				"player_stats.p_str, player_stats.p_wytrz, player_stats.p_zr, player_stats.p_zd, player_stats.p_int, player_stats.p_mana, player_stats.p_walka, player_stats.p_krag," + 
				"player_stats.potion_str, player_stats.potion_wytrz, player_stats.potion_zr, player_stats.potion_zd, player_stats.potion_int, player_stats.potion_mana, player_stats.potion_walka," +
				"player_stats.p_stygia, player_stats.p_coins, player_stats.p_brylki, player_stats.p_event," + 
				"player_info.health, player_info.p_health, player_info.item_drop, player_info.tutorial, player_info.check_hp," + 
				"player_rzemioslo.alchemia, player_rzemioslo.kowalstwo, player_rzemioslo.platnerstwo, player_rzemioslo.luczarstwo, player_rzemioslo.jubilerstwo," + 
				"player_skills.manaReg, player_skills.unlimitArr, player_skills.foodless, player_skills.slugaBeliara, player_skills.magKrwi, player_skills.ciosKrytyczny, "+
				"player_skills.magnetyzm, player_skills.silaZywiolow, player_skills.polnocnyBarbarzynca, player_skills.rozprucie, " + 
				"player_reputation.archolos_id, player_reputation.archolos_amount, player_reputation.klan_id, player_reputation.klan_amount, player_reputation.witcher_id, player_reputation.witcher_amount " + 
				"FROM players " + 
				"INNER JOIN player_stats ON players.id = player_stats.player_id " + 
				"INNER JOIN player_info ON players.id = player_info.player_id " + 
				"INNER JOIN player_rzemioslo ON players.id = player_rzemioslo.player_id " + 
				"INNER JOIN player_skills ON players.id = player_skills.player_id " + 
				"LEFT JOIN player_reputation ON players.id = player_reputation.player_id " + 
				"WHERE players.nick LIKE \""+player.getName()+"\"";
		try {
			Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if(!stmt.executeQuery(polecenie).next()) return null;
			toReturn = stmt.executeQuery(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad pobierania danych "+player.getName()+" z bazy danych");
			e.printStackTrace();
			return null;
		}
		return toReturn;
	}
	
	public static void savePlayer(RpgPlayer rpg) {
		UUID id = rpg.getPlayer().getUniqueId();
		String polecenie;
		polecenie = "SELECT players.UUID from players WHERE players.UUID = \""+id.toString()+"\";";
		ResultSet set;
		try {
			set = c.createStatement().executeQuery(polecenie);
			if(set.next()==true) {
				updatePlayer(rpg, id);
			}
			else {
				insertPlayer(rpg, id);
			}
		} catch (SQLException e) {
			System.out.println("Blad sprawdzenia gracza "+rpg.getPlayer().getName());
			e.printStackTrace();
			return;
		}
	}
	
	private static void updatePlayer(RpgPlayer rpg, UUID id) {
		RpgStats stats = rpg.getStats();
		RpgPlayerInfo playerInfo = rpg.getInfo();
		RpgRzemiosla rzemiosla = rpg.getRzemiosla();
		RpgSkills skills = rpg.getSkills();
		RpgVault vault = rpg.getVault();
		RpgReputation rep = rpg.getReputation();
		String nick = rpg.getPlayer().getName();
		String polecenie = "SELECT id FROM players WHERE players.UUID=\""+id.toString()+"\";";
		int db_id = -1;
		try {
			ResultSet set = c.createStatement().executeQuery(polecenie);
			while(set.next()) {
				db_id = set.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych");
			e.printStackTrace();
			return;
		}
		if(db_id == -1) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych");
			return;
		}
		
		polecenie = "UPDATE players "
				+ "SET nick=\""+nick+"\" "
				+ "WHERE players.UUID=\""+id.toString()+"\";";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych");
			e.printStackTrace();
			return;
		}
		
		polecenie = "UPDATE player_info "
				+ "SET health="+stats.getHealth()+","
				+ "p_health="+stats.getPotionHealth()+","
				+ "item_drop="+playerInfo.isDrop()+","
				+ "tutorial="+playerInfo.isTutorial()+","
				+ "check_hp="+true+" "
				+ "WHERE player_id="+db_id+";";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych");
			e.printStackTrace();
			return;
		}
		
		polecenie = "UPDATE player_rzemioslo "
				+ "SET alchemia="+rzemiosla.hasAlchemia()+","
				+ "kowalstwo="+rzemiosla.hasKowalstwo()+","
				+ "platnerstwo="+rzemiosla.hasPlatnerstwo()+","
				+ "luczarstwo="+rzemiosla.hasLuczarstwo()+", "
				+ "jubilerstwo="+rzemiosla.hasJubilerstwo()+" "
				+ "WHERE player_id="+db_id+";";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych");
			e.printStackTrace();
			return;
		}
		
		polecenie = "UPDATE player_skills "
				+ "SET manaReg="+skills.hasManaReg()+","
				+ "unlimitArr="+skills.hasUnlimitedArrows()+","
				+ "foodless="+skills.hasHungerless()+","
				+ "slugaBeliara="+skills.hasSlugaBeliara()+","
				+ "magKrwi="+skills.hasMagKrwi()+","
				+ "ciosKrytyczny="+skills.hasCiosKrytyczny()+", "
				+ "magnetyzm="+skills.hasMagnetyzm()+", "
				+ "silaZywiolow="+skills.hasSilaZywiolow()+", "
				+ "polnocnyBarbarzynca="+skills.hasPolnocnyBarbarzynca()+", "
				+ "rozprucie="+skills.hasRozprucie()+" "
				+ "WHERE player_id="+db_id+";";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych");
			e.printStackTrace();
			return;
		}
		
		polecenie = "UPDATE player_stats "
				+ "SET klasa=\""+playerInfo.getProffesion()+"\","
				+ "level="+playerInfo.getLevel()+","
				+ "exp="+playerInfo.getExp()+","
				+ "nextLevel="+playerInfo.getNextLevel()+","
				+ "pn="+playerInfo.getPn()+","
				+ "p_str="+stats.getSila()+","
				+ "p_wytrz="+stats.getWytrzymalosc()+","
				+ "p_zr="+stats.getZrecznosc()+","
				+ "p_zd="+stats.getZdolnosci()+","
				+ "p_mana="+stats.getMana()+","
				+ "p_int="+stats.getInteligencja()+","
				+ "p_walka="+stats.getWalka()+","
				+ "p_krag="+stats.getKrag()+","
				+ "potion_str="+stats.getPotionSila()+","
				+ "potion_wytrz="+stats.getPotionWytrzymalosc()+","
				+ "potion_zr="+stats.getPotionZrecznosc()+","
				+ "potion_zd="+stats.getPotionZdolnosci()+","
				+ "potion_mana="+stats.getPotionMana()+","
				+ "potion_int="+stats.getPotionInteligencja()+","
				+ "potion_walka="+stats.getPotionWalka()+","
				+ "p_stygia="+vault.getStygia()+","
				+ "p_coins="+vault.getDragonCoins()+","
				+ "p_brylki="+vault.getBrylkiRudy()+" "
				+ "WHERE player_id="+db_id+";";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych");
			e.printStackTrace();
			return;
		}
		
		polecenie = "INSERT INTO player_reputation (player_id,archolos_id,archolos_amount,klan_id,klan_amount,witcher_id,witcher_amount) "
				+ "VALUES ("+db_id+","
				+rep.getReputacja().get("archolos").getReputationLevel().getId()+","+rep.getReputacja().get("archolos").getReputationAmount()+","
				+rep.getReputacja().get("klan").getReputationLevel().getId()+","+rep.getReputacja().get("klan").getReputationAmount()+","
				+rep.getReputacja().get("witcher").getReputationLevel().getId()+","+rep.getReputacja().get("witcher").getReputationAmount()+") "
				+ "ON DUPLICATE KEY UPDATE "
				+ "archolos_id="+rep.getReputacja().get("archolos").getReputationLevel().getId()+","
				+ "archolos_amount="+rep.getReputacja().get("archolos").getReputationAmount()+","
				+ "klan_id="+rep.getReputacja().get("klan").getReputationLevel().getId()+","
				+ "klan_amount="+rep.getReputacja().get("klan").getReputationAmount()+","
				+ "witcher_id="+rep.getReputacja().get("witcher").getReputationLevel().getId()+","
				+ "witcher_amount="+rep.getReputacja().get("witcher").getReputationAmount()+";";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych");
			e.printStackTrace();
			return;
		}
		
	}
	
	private static void insertPlayer(RpgPlayer rpg, UUID id) {
		RpgStats stats = rpg.getStats();
		RpgReputation reputation = rpg.getReputation();
		RpgPlayerInfo playerInfo = rpg.getInfo();
		RpgRzemiosla rzemiosla = rpg.getRzemiosla();
		RpgSkills skills = rpg.getSkills();
		RpgVault vault = rpg.getVault();
		String nick = rpg.getPlayer().getName();
		
		String polecenie = "INSERT INTO players (UUID,nick) VALUES "
				+ "('"+id.toString()+"','"+nick+"');";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych 1");
			e.printStackTrace();
			return;
		}
		
		polecenie = "SELECT id FROM players WHERE players.UUID=\""+id.toString()+"\";";
		
		int db_id = -1;
		try {
			ResultSet set = c.createStatement().executeQuery(polecenie);
			while(set.next()) {
				db_id = set.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych 2");
			e.printStackTrace();
			return;
		}
		if(db_id == -1) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych 3");
			return;
		}
		
		polecenie = "INSERT INTO player_info (player_id,health,item_drop,tutorial) VALUES "
				+ "("+db_id+","+stats.getHealth()+","+playerInfo.isDrop()+","+playerInfo.isTutorial()+");";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych 4");
			e.printStackTrace();
			return;
		}
		
		polecenie = "INSERT INTO player_rzemioslo (player_id,alchemia,kowalstwo,platnerstwo,luczarstwo,jubilerstwo) VALUES "
				+ "("+db_id+","+rzemiosla.hasAlchemia()+","+rzemiosla.hasKowalstwo()+","+rzemiosla.hasPlatnerstwo()+","+rzemiosla.hasLuczarstwo()+","+rzemiosla.hasJubilerstwo()+");";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych 4");
			e.printStackTrace();
			return;
		}
		
		polecenie = "INSERT INTO player_skills (player_id,manaReg,unlimitArr,foodless,slugaBeliara,magKrwi,ciosKrytyczny,magnetyzm,silaZywiolow,polnocnyBarbarzynca,rozprucie) VALUES "
				+"("+db_id+","+skills.hasManaReg()+","+skills.hasUnlimitedArrows()+","+skills.hasHungerless()+","+skills.hasSlugaBeliara()+","+skills.hasMagKrwi()+","+skills.hasCiosKrytyczny()+","+skills.hasMagnetyzm()+","
				+skills.hasSilaZywiolow()+","+skills.hasPolnocnyBarbarzynca()+","+skills.hasRozprucie()+");";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych 5");
			e.printStackTrace();
			return;
		}
		
		polecenie = "INSERT INTO player_stats (player_id,ranga,klasa,level,exp,nextLevel,pn,"
				+ "p_str,p_wytrz,p_zr,p_zd,p_mana,p_int,p_walka,p_krag,"
				+ "potion_str,potion_wytrz,potion_zr,potion_zd,potion_mana,potion_int,potion_walka,"
				+ "p_stygia,p_coins,p_brylki) VALUES "
				+"("+db_id+",\"§2Gracz\",\""+playerInfo.getProffesion()+"\","+playerInfo.getLevel()+","+playerInfo.getExp()+","+playerInfo.getNextLevel()+","+playerInfo.getPn()+","
				+stats.getSila()+","+stats.getWytrzymalosc()+","+stats.getZrecznosc()+","+stats.getZdolnosci()+","+stats.getMana()+","+stats.getInteligencja()+","+stats.getWalka()+","+stats.getKrag()+","
				+stats.getPotionSila()+","+stats.getPotionWytrzymalosc()+","+stats.getPotionZrecznosc()+","+stats.getPotionZdolnosci()+","+stats.getPotionMana()+","+stats.getPotionInteligencja()+","+stats.getPotionWalka()+","
				+vault.getStygia()+","+vault.getDragonCoins()+","+vault.getBrylkiRudy()+");";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych 6");
			e.printStackTrace();
			return;
		}
		
		polecenie = "INSERT INTO player_reputation (player_id,archolos_id,archolos_amount,klan_id,klan_amount,witcher_id,witcher_amount) "
				+ "VALUES ("+db_id+","
				+reputation.getReputacja().get("archolos").getReputationLevel().getId()+","+reputation.getReputacja().get("archolos").getReputationAmount()+","
				+reputation.getReputacja().get("klan").getReputationLevel().getId()+","+reputation.getReputacja().get("klan").getReputationAmount()+","
				+reputation.getReputacja().get("witcher").getReputationLevel().getId()+","+reputation.getReputacja().get("witcher").getReputationAmount()+");";
		try {
			c.createStatement().executeUpdate(polecenie);
		} catch (SQLException e) {
			System.out.println("Blad zapisu gracza "+nick+" do bazy danych 7");
			e.printStackTrace();
			return;
		}
		
	}
	
}
