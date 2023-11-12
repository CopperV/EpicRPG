package me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Melee;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import net.md_5.bungee.api.ChatColor;

public class ProfMeleeModifierListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MELEE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		boolean crit = e.getCalculatedDamage().getValue();
		double modifier = 0;
		
		if(crit 
				&& ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("mys"))
			modifier = 0.15;
		else if(!crit 
				&& ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("woj"))
			modifier = 0.15;
		
		e.increaseModifier(modifier);
	}

}
