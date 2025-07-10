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
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public class RycerzSwiatlosciSetListener implements Listener {
	
	@EventHandler
	public void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MAGIC))
			return;
		
		Object[] args = e.getArgs();
		if(args == null 
				|| args.length < 2
				|| !(args[1] instanceof EpicRune))
			return;
		
		EpicRune rune = (EpicRune) args[1];
		String type = rune.getMagicType();
		if(!type.equalsIgnoreCase("swiatlo"))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Rycerz_Swiatlosci", 0) < 2)
			return;
		
		e.increaseModifier(0.02);
	}
	
}
