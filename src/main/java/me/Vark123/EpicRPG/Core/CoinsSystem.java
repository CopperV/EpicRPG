package me.Vark123.EpicRPG.Core;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class CoinsSystem {
	
	private static final CoinsSystem instance = new CoinsSystem();
	
	private CoinsSystem() {}
	
	public static CoinsSystem getInstance() {
		return instance;
	}
	
	public void addMobCoins(RpgPlayer rpg, String mob) {
		RpgPlayerInfo info = rpg.getInfo();
		if(info.getLevel() < 50)
			return;
		addCoins(rpg, EpicRPGMobManager.getInstance().getMobCoins(mob));
	}
	
	public void addXpCoins(RpgPlayer rpg, double xp) {
		RpgPlayerInfo info = rpg.getInfo();
		if(info.getLevel() < 50)
			return;
		addCoins(rpg, (int) (xp/50));
	}
	
	private void addCoins(RpgPlayer rpg, int amount) {
		if(amount <= 0)
			return;
		RpgPlayerInfo info = rpg.getInfo();
		RpgVault vault = rpg.getVault();
		if(info.getLevel() == ExpSystem.getInstance().MAX_LEVEL)
			amount *= 1.5;
		
		Player p = rpg.getPlayer();
		if(p.hasPermission("rpg.vip")) {
			amount *= 1.2;
		}
		
		vault.addDragonCoins(amount);
		p.sendMessage("§c§o+"+ amount +" Smoczych Monet §7[§c§o"+vault.getDragonCoins()+" Smoczych Monet§7]");
	}

}
