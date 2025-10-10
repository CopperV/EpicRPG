package me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class PolnocnyBarbarzyncaSetListener implements Listener {
	
	@EventHandler
	public void onSetAttack1(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;
		
		LivingEntity victim = e.getVictim();
		double health = victim.getHealth();
		double maxHealth = victim.getAttribute(Attribute.MAX_HEALTH).getValue();
		if(maxHealth * 0.5 <= health)
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Polnocny_Barbarzynca", 0) < 2)
			return;
		
		e.increaseModifier(0.05);
	}
	
	@EventHandler
	public void onSetAttack2(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;
		
		double health = player.getHealth();
		double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
		if(maxHealth * 0.5 <= health)
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Polnocny_Barbarzynca", 0) < 3)
			return;
		
		e.increaseModifier(0.08);
	}

	@EventHandler
	public void onDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player player))
			return;
		
		double health = player.getHealth();
		double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
		if(maxHealth * 0.5 <= health)
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		if(info.getSetCounts().getOrDefault("Polnocny_Barbarzynca", 0) < 4)
			return;
		e.decreaseModifier(0.08);
	}
	
}
