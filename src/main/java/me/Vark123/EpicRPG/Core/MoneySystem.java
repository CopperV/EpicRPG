package me.Vark123.EpicRPG.Core;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Core.Events.MoneyModifyEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class MoneySystem {

	private static final MoneySystem instance = new MoneySystem();
	
	private MoneySystem() {}
	
	public static MoneySystem getInstance() {
		return instance;
	}
	
	public void addMoney(RpgPlayer rpg, double amount, String reason, boolean displayMessage) {
		MoneyModifyEvent event = new MoneyModifyEvent(rpg, amount, 1, reason);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		double _amount = event.getAmount()*event.getModifier();
		if(Math.abs(_amount) < 0.01)
			return;

		RpgVault vault = rpg.getVault();
		vault.addMoney(_amount);
		
		if(!displayMessage)
			return;
		rpg.getPlayer().sendMessage("§e§o+"+ String.format("%.2f", _amount) +"$ §7[§e§o"+String.format("%.2f", _amount)+"$§7]");
	}
	
	public void addMobMoney(RpgPlayer rpg, String mob) {
		addMoney(rpg, EpicRPGMobManager.getInstance().getMobMoney(mob), "mob", true);
	}
	
}
