package me.Vark123.EpicRPG.Core;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPG.Core.Events.RudaModifyEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class RudaSystem {

	private static final RudaSystem instance = new RudaSystem();
	
	private RudaSystem() {}
	
	public static RudaSystem getInstance() {
		return instance;
	}
	
	public void addRuda(RpgPlayer rpg, int amount, String reason, boolean displayMessage) {
		RudaModifyEvent event = new RudaModifyEvent(rpg, amount, 1, reason);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		int _amount = (int) (event.getAmount()*event.getModifier());
		if(_amount == 0)
			return;

		RpgVault vault = rpg.getVault();
		vault.addBrylkiRudy(_amount);
		
		if(!displayMessage)
			return;
		rpg.getPlayer().sendMessage("§9§o+"+ _amount +" brylek rudy §7[§3§o"+vault.getBrylkiRudy()+" brylek rudy§7]");
	}
	
}
