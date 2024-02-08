package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageManager;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.CustomProjectileEntityDamageEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.Pair;

public class CustomProjectileDamageListener implements Listener {

	@EventHandler
	public void onDamage(CustomProjectileEntityDamageEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		Entity victim = e.getEntity();
		double dmg = e.getDamage();
		Pair<Double, Boolean> damageInfo = new Pair<>(dmg, false);
		EpicDamageType damageType = EpicDamageType.PROJECTILE;
		
		damageInfo = DamageManager.getInstance()
				.getProjectileCalculator()
				.calc(damager, victim, dmg, Double.valueOf(dmg));
		
		EpicAttackEvent attackEvent = new EpicAttackEvent(damager, victim, damageType, 
				damageInfo.getKey(), 1, damageInfo);
		Bukkit.getPluginManager().callEvent(attackEvent);
		
		if(attackEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		if(attackEvent.getDmg() <= 0) {
			e.setCancelled(true);
			return;
		}
		
		dmg = attackEvent.getDmg() * attackEvent.getModifier();
		if(dmg <= 0) {
			e.setCancelled(true);
			return;
		}
		
		//DEFENSE STATEMENT
		if(victim instanceof Player) {
			Pair<Double, Boolean> defenseInfo = DamageManager.getInstance()
					.getDefenseCalculator().calc(damager, victim, dmg);
			
			EpicDefenseEvent defenseEvent = new EpicDefenseEvent(damager, victim, damageType,
					defenseInfo.getKey(), 1, defenseInfo);
			Bukkit.getPluginManager().callEvent(defenseEvent);
			
			if(defenseEvent.isCancelled()) {
				e.setCancelled(true);
				return;
			}
			
			dmg = defenseEvent.getDmg() * defenseEvent.getModifier();
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) victim);
			int level = rpg.getInfo().getLevel();
			if(dmg < ((level / 10.) + 2))
				dmg = (level / 10.) + 2;
		}
		
		EpicEffectEvent effectEvent = new EpicEffectEvent(damager, victim, damageType,
				dmg, 1, damageInfo);
		Bukkit.getPluginManager().callEvent(effectEvent);
		
		if(effectEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		
		if(effectEvent.getDmg() <= 0) {
			e.setCancelled(true);
			return;
		}
		
		dmg = effectEvent.getFinalDamage();
		if(dmg <= 0) {
			e.setCancelled(true);
			return;
		}
		
		e.setDamage(dmg);
		
	}
	
}
