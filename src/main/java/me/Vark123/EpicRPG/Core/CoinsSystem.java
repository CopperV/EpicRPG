package me.Vark123.EpicRPG.Core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Core.Events.CoinsModifyEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class CoinsSystem {
	
	private static final CoinsSystem instance = new CoinsSystem();
	
	private CoinsSystem() {}
	
	public static CoinsSystem getInstance() {
		return instance;
	}
	
	public void addCoins(RpgPlayer rpg, int amount, String reason, boolean displayMessage) {
		CoinsModifyEvent event = new CoinsModifyEvent(rpg, amount, 1, reason);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		int _amount = (int) (event.getAmount()*event.getModifier());
		if(_amount == 0)
			return;

		RpgVault vault = rpg.getVault();
		vault.addDragonCoins(_amount);
		
		if(!displayMessage)
			return;
		rpg.getPlayer().sendMessage("§c§o+"+ amount +" Smoczych Monet §7[§c§o"+vault.getDragonCoins()+" Smoczych Monet§7]");
	}
	
	public void addMobCoins(RpgPlayer rpg, String mob) {
		addCoins(rpg, EpicRPGMobManager.getInstance().getMobCoins(mob), "mob", true);
	}
	
	public void addXpCoins(RpgPlayer rpg, double xp) {
		addCoins(rpg, (int) (xp/50), "quest", true);
	}
	
	@Deprecated
	public void addCoins(RpgPlayer rpg, int amount) {
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
