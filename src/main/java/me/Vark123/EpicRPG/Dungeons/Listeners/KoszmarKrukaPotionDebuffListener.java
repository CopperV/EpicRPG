package me.Vark123.EpicRPG.Dungeons.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.Events.RpgPlayerManaRegenEvent;

public class KoszmarKrukaPotionDebuffListener implements Listener {

	@EventHandler
	public void onHeal(RpgPlayerHealEvent e) {
		if(e.isCancelled()) {
			return;
		}
		
		Player p = e.getP();
		String world = p.getWorld().getName().toLowerCase();
		if(!world.contains("dungeon13_"))
			return;
		double modifier;
		if(world.contains("heroic"))
			modifier = 0.6;
		else if(world.contains("mythic"))
			modifier = 0.5;
		else
			modifier = 0.7;
		e.setHeal(e.getHealAmount()*modifier);
	}

	@EventHandler
	public void onHeal(RpgPlayerManaRegenEvent e) {
		if(e.isCancelled()) {
			return;
		}
		
		Player p = e.getPlayer();
		String world = p.getWorld().getName().toLowerCase();
		if(!world.toLowerCase().contains("dungeon13_"))
			return;
		double modifier;
		if(world.contains("heroic"))
			modifier = 0.6;
		else if(world.contains("mythic"))
			modifier = 0.5;
		else
			modifier = 0.7;
		e.setRegen((int) (e.getRegen()*modifier));
	}

}
