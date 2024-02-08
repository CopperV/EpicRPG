package me.Vark123.EpicRPG.Core.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.Core.Events.CoinsModifyEvent;
import me.Vark123.EpicRPG.Core.Events.ExpModifyEvent;
import me.Vark123.EpicRPG.Core.Events.RudaModifyEvent;
import me.Vark123.EpicRPG.Core.Events.StygiaModifyEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class VipBoostControlListener implements Listener {

	@EventHandler
	public void onExp(ExpModifyEvent e) {
		if(e.isCancelled())
			return;
		
		RpgPlayer rpg = e.getRpg();
		Player p = rpg.getPlayer();
		if(!p.hasPermission("rpg.vip"))
			return;
		double modifier = e.getModifier();
		switch(e.getReason().toLowerCase()) {
			case "mob":
				modifier += 0.5;
				break;
			case "quest":
				modifier += 0.01;
				break;
		}
		e.setModifier(modifier);
	}

	@EventHandler
	public void onCoins(CoinsModifyEvent e) {
		if(e.isCancelled())
			return;
		
		RpgPlayer rpg = e.getRpg();
		Player p = rpg.getPlayer();
		if(!p.hasPermission("rpg.vip"))
			return;
		double modifier = e.getModifier();
		switch(e.getReason().toLowerCase()) {
			case "mob":
			case "quest":
				modifier += 0.2;
				break;
		}
		e.setModifier(modifier);
	}

	@EventHandler
	public void onStygia(StygiaModifyEvent e) {
		if(e.isCancelled())
			return;
		
		RpgPlayer rpg = e.getRpg();
		Player p = rpg.getPlayer();
		if(!p.hasPermission("rpg.vip"))
			return;
		double modifier = e.getModifier();
		switch(e.getReason().toLowerCase()) {
			case "mob":
				modifier += 0.5;
				break;
			case "quest":
				modifier += 0.25;
				break;
		}
		e.setModifier(modifier);
	}

	@EventHandler
	public void onRuda(RudaModifyEvent e) {
		if(e.isCancelled())
			return;
		
		RpgPlayer rpg = e.getRpg();
		Player p = rpg.getPlayer();
		if(!p.hasPermission("rpg.vip"))
			return;
		double modifier = e.getModifier();
		switch(e.getReason().toLowerCase()) {
			case "mob":
				modifier += 0.3;
				break;
			case "quest":
				modifier += 0.15;
				break;
		}
		e.setModifier(modifier);
	}
	
}
