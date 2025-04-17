package me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Calculators.IDamageCalculator.DamageCalculatorResult;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;

public class CiosKrytycznyEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	private void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgSkills skills = rpg.getSkills();
		if(!skills.hasCiosKrytyczny())
			return;
		
		if(e.getArgs() == null || e.getArgs().length < 1 
				|| !(e.getArgs()[0] instanceof DamageCalculatorResult))
			return;
		if(!((DamageCalculatorResult) e.getArgs()[0]).isCrit)
			return;

		
		e.increaseModifier(0.1);
	}

}
