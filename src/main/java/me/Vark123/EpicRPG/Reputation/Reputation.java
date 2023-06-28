package me.Vark123.EpicRPG.Reputation;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Main;

public class Reputation {
	
	private String fraction;
	private String displayFraction;
	private ReputationLevels reputationLevel;
	private int reputationAmount;
	private Player player;
	
	public Reputation(String fraction, String displayFraction, ReputationLevels reputationLevel,
			int reputationAmount, Player player) {
		this.fraction = fraction;
		this.displayFraction = displayFraction;
		this.reputationLevel = reputationLevel;
		this.reputationAmount = reputationAmount;
		this.player = player;
	}

	public String getFraction() {
		return fraction;
	}

	public String getDisplayFraction() {
		return displayFraction;
	}

	public ReputationLevels getReputationLevel() {
		return reputationLevel;
	}

	public int getReputationAmount() {
		return reputationAmount;
	}

	public Player getPlayer() {
		return player;
	}
	
	public void addSmartReputation(int amount) {
		if(reputationLevel.getAmount() == 0)
			return;
		
		this.reputationAmount += amount;
		if(this.reputationAmount < reputationLevel.getAmount())
			return;
		
		//UPGRADE
		ReputationLevels oldLevel = this.reputationLevel;
		ReputationLevels nextLevel;
		do {
//			Bukkit.broadcastMessage(reputationLevel.getName()+" "+reputationLevel.getId()+" "+reputationLevel.getAmount()+" / "+this.reputationAmount);
			nextLevel = ReputationLevels.getReputationLevelById(this.reputationLevel.getId()+1);
			if(nextLevel == null) {
//				Bukkit.broadcastMessage("null");
				this.reputationAmount = ReputationLevels.getReputationLevelById(reputationLevel.getId()-1).getAmount();
				player.sendMessage(Main.getInstance().getPrefix()+" §6§lReputacja §r"+displayFraction+" §a"+oldLevel.getName()+" -> "+reputationLevel.getName());
				return;
			}
//			Bukkit.broadcastMessage(nextLevel.getName()+" "+nextLevel.getId());
			this.reputationLevel = nextLevel;
		} while(this.reputationAmount >= nextLevel.getAmount());
		
		player.sendMessage(Main.getInstance().getPrefix()+" §6§lReputacja §r"+displayFraction+" §a"+oldLevel.getName()+" -> "+nextLevel.getName());
	}
	
	public void removeSmartReputation(int amount) {
		if(reputationAmount == 0)
			return;
		
		this.reputationAmount -= amount;
		if(reputationAmount < 0)
			reputationAmount = 0;
		
		ReputationLevels downLevel = ReputationLevels.getReputationLevelById(reputationLevel.getId()-1);
		if(downLevel == null)
			return;
		
		if(reputationAmount > downLevel.getAmount())
			return;
		
		//DOWNGRADE
		ReputationLevels oldLevel = this.reputationLevel;
		ReputationLevels newLevel = ReputationLevels.getReputationLevelById(reputationLevel.getId());
		do {
			newLevel = ReputationLevels.getReputationLevelById(newLevel.getId()-1);
			if(newLevel == null) {
				break;
			}
//			Bukkit.broadcastMessage(this.reputationAmount+" "+this.reputationLevel.getId()+" "+newLevel.getId()+" "+newLevel.getName()+" "+newLevel.getAmount());
			this.reputationLevel = ReputationLevels.getReputationLevelById(newLevel.getId()+1);
		} while (this.reputationAmount < newLevel.getAmount());
		if(this.reputationLevel.getId() > 0) {
			this.reputationLevel = ReputationLevels.getReputationLevelById(this.reputationLevel.getId()-1);
		}
		
		player.sendMessage(Main.getInstance().getPrefix()+" §6§lReputacja §r"+displayFraction+" §a"+oldLevel.getName()+" -> "+reputationLevel.getName());
	}

}
