package me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Calculators.IDamageCalculator.DamageCalculatorResult;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class WywarEffectListener implements Listener {

	@EventHandler
	private void onDef(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;

		Entity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		double modifier = 0;
		switch(modifiers.getPotionWytrzymalosc()) {
			case 1:
				modifier = 0.1;
				break;
			case 2:
				modifier = 0.25;
				break;
			case 3:
				modifier = 0.4;
				break;
		}
		
		e.decreaseModifier(modifier);
	}
	
	@EventHandler
	private void onProjAtt(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player player = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgModifiers modifiers = rpg.getModifiers();
		double modifier = 0;
		
		if(e.getArgs() != null && e.getArgs().length > 0
				&& e.getArgs()[0] instanceof DamageCalculatorResult) {
			
			DamageCalculatorResult damageInfo = (DamageCalculatorResult) e.getArgs()[0];
			boolean crit = damageInfo.isCrit;
			
			if(crit)
				switch(modifiers.getPotionZrecznosc()) {
					case 1:
						modifier += 0.15;
						break;
					case 2:
						modifier += 0.3;
						break;
					case 3:
						modifier += 0.45;
						break;
			}
		}
		switch(modifiers.getPotionZdolnosci()) {
			case 1:
				modifier += 0.1;
				break;
			case 2:
				modifier += 0.25;
				break;
			case 3:
				modifier += 0.4;
				break;
		}
		
		e.increaseModifier(modifier);
	}
	
	@EventHandler
	private void onMeleeAtt(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MELEE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player player = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgModifiers modifiers = rpg.getModifiers();
		double modifier = 0;
		
		if(e.getArgs() != null && e.getArgs().length > 0
				&& e.getArgs()[0] instanceof DamageCalculatorResult) {
			
			DamageCalculatorResult damageInfo = (DamageCalculatorResult) e.getArgs()[0];
			boolean crit = damageInfo.isCrit;
			
			if(crit)
				switch(modifiers.getPotionZrecznosc()) {
					case 1:
						modifier += 0.15;
						break;
					case 2:
						modifier += 0.3;
						break;
					case 3:
						modifier += 0.45;
						break;
			}
		}
		switch(modifiers.getPotionSila()) {
			case 1:
				modifier += 0.1;
				break;
			case 2:
				modifier += 0.25;
				break;
			case 3:
				modifier += 0.4;
				break;
		}
		
		e.increaseModifier(modifier);
	}
	
	@EventHandler
	private void onMagicAtt(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MAGIC))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player player = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgModifiers modifiers = rpg.getModifiers();
		double modifier = 0;
		switch(modifiers.getPotionInteligencja()) {
			case 1:
				modifier += 0.1;
				break;
			case 2:
				modifier += 0.25;
				break;
			case 3:
				modifier += 0.4;
				break;
		}
		
		e.increaseModifier(modifier);
	}
	
}
