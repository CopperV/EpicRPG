package me.Vark123.EpicRPG.Players.Components;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Reputation.Reputation;
import me.Vark123.EpicRPG.Reputation.ReputationContainer;
import me.Vark123.EpicRPG.Reputation.ReputationLevels;
import me.Vark123.EpicRPG.Utils.ChatPrintable;

@Getter
public class RpgReputation implements Serializable, ChatPrintable {
	
	private static final long serialVersionUID = -8529352962442361007L;

	private RpgPlayer rpg;

	private Map<String, Reputation> reputacja;
	
	public RpgReputation(RpgPlayer rpg) {
		this.rpg = rpg;
		this.reputacja = createBaseReputation();
	}
	
	public RpgReputation(RpgPlayer rpg, ResultSet set) throws SQLException {
		this.rpg = rpg;
		reputacja = new ConcurrentHashMap<>();
		set.first();
		//ARCHOLOS
		int archolos_level = set.getInt("player_reputation.archolos_id");
		int archolos_amount = set.getInt("player_reputation.archolos_amount");
		//KLAN
		int klan_level = set.getInt("player_reputation.klan_id");
		int klan_amount = set.getInt("player_reputation.klan_amount");
		//WITCHER
		int witcher_level = set.getInt("player_reputation.witcher_id");
		int witcher_amount = set.getInt("player_reputation.witcher_amount");
		//GENERATE MAP
		reputacja.put("archolos", new Reputation(
				"archolos", ReputationContainer.getInstance().getContainer().get("archolos").getDisplay(), 
				ReputationLevels.getReputationLevelById(archolos_level), archolos_amount, rpg.getPlayer()));
		reputacja.put("klan", new Reputation(
				"klan", ReputationContainer.getInstance().getContainer().get("klan").getDisplay(), 
				ReputationLevels.getReputationLevelById(klan_level), klan_amount, rpg.getPlayer()));
		reputacja.put("witcher", new Reputation(
				"witcher", ReputationContainer.getInstance().getContainer().get("witcher").getDisplay(), 
				ReputationLevels.getReputationLevelById(witcher_level), witcher_amount, rpg.getPlayer()));
	}
	
	public RpgReputation(RpgPlayer rpg, YamlConfiguration fYml) {
		this.rpg = rpg;
		this.reputacja = createBaseReputation();
	}
	
	public Map<String, Reputation> createBaseReputation(){
		Map<String, Reputation> toReturn = new ConcurrentHashMap<>();
		ReputationContainer.getInstance().getContainer().forEach((id, base) -> {
			toReturn.put(id, 
					new Reputation(base.getName(), base.getDisplay(), 
							ReputationLevels.getReputationLevelById(0), 0, rpg.getPlayer()));
		});
		return toReturn;
	}

	@Override
	public void print(CommandSender sender) {
		sender.sendMessage("§6§l========================= ");
		reputacja.values().forEach(rep -> {
			sender.sendMessage("    §2Reputacja "+rep.getDisplayFraction()+"§2: §a"+rep.getReputationLevel().getName()
					+" §7(§a"+rep.getReputationAmount()+"§7/§a"+rep.getReputationLevel().getAmount()+"§7)");
		});
	}
	
}
