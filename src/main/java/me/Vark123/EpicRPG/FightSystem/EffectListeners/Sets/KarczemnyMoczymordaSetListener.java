package me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Utils.Utils;

public class KarczemnyMoczymordaSetListener implements Listener {
	
	@EventHandler
	public void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MELEE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;

		if(Utils.hasWeapon(player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Klusownik", 0) < 3)
			return;
		
		e.increaseModifier(0.05);
	}
	
}
