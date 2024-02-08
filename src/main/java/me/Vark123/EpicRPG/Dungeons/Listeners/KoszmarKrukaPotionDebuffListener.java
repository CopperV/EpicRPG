package me.Vark123.EpicRPG.Dungeons.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Potions.RpgPlayerManaRegenEvent;

public class KoszmarKrukaPotionDebuffListener implements Listener {

	@EventHandler
	public void onHeal(RpgPlayerHealEvent e) {
		if(e.isCancelled()) {
			return;
		}
		
		Player p = e.getP();
		if(!p.getWorld().getName().toLowerCase().equalsIgnoreCase("dungeon13"))
			return;
		double modifier = 0.7;
		e.setHeal(e.getHealAmount()*modifier);
	}

	@EventHandler
	public void onHeal(RpgPlayerManaRegenEvent e) {
		if(e.isCancelled()) {
			return;
		}
		
		Player p = e.getPlayer();
		if(!p.getWorld().getName().toLowerCase().equalsIgnoreCase("dungeon13"))
			return;
		double modifier = 0.7;
		e.setRegen((int) (e.getRegen()*modifier));
	}

}
