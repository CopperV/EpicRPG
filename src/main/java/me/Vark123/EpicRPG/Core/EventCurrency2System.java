package me.Vark123.EpicRPG.Core;

import org.bukkit.Bukkit;

import me.Vark123.EpicRPG.Core.Events.EventCurrency2ModifyEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class EventCurrency2System {
	
	private static final EventCurrency2System instance = new EventCurrency2System();
	
	private EventCurrency2System() {}
	
	public static EventCurrency2System getInstance() {
		return instance;
	}
	
	public void addCurrency(RpgPlayer rpg, int amount, String reason) {
		EventCurrency2ModifyEvent event = new EventCurrency2ModifyEvent(rpg, amount, 1, reason);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		int _amount = (int) (event.getAmount()*event.getModifier());
		if(_amount == 0)
			return;

		RpgVault vault = rpg.getVault();
		vault.addEventCurrency2(_amount);

		rpg.getPlayer().sendMessage("§a§o+"+ _amount +" Palemek §7[§a§o"+vault.getEventCurrency2()+" Palemek§7]");
	}

}
