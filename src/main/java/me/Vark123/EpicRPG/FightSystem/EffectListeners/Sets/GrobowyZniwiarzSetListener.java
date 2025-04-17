package me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class GrobowyZniwiarzSetListener implements Listener {

	@EventHandler
	public void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		double modifier = 0;
		switch(info.getSetCounts().getOrDefault("Grobowy_Zniwiarz", 0)) {
			case 4:
				modifier = 0.15;
				break;
			case 3:
				modifier = 0.1;
				break;
			case 2:
				modifier = 0.05;
				break;
		}
		e.increaseModifier(modifier);
	}

}
