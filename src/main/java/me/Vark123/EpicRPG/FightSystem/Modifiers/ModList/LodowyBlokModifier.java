package me.Vark123.EpicRPG.FightSystem.Modifiers.ModList;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class LodowyBlokModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		
		if(victim instanceof Player) {
//			Bukkit.broadcastMessage("Victim: "+victim.getName());
			if(!PlayerManager.getInstance().playerExists((Player) victim))
				return damage;
			
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) victim);
			RpgModifiers modifier = rpg.getModifiers();
			if(!modifier.hasLodowyBlok())
				return damage;

			//TODO
//			if(!LodowyBlok.getEffected().contains((Player) victim))
//				return damage;
			
			victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1, 2);
			return -1;
		}

		if(damager instanceof Player) {
			Player p = (Player) damager;
//			Bukkit.broadcastMessage("Damager: "+damager.getName());
			if(!PlayerManager.getInstance().playerExists(p))
				return damage;

			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			RpgModifiers modifier = rpg.getModifiers();
			if(!modifier.hasLodowyBlok())
				return damage;

			//TODO
//			if(!LodowyBlok.getEffected().contains(p))
//				return damage;
//			
//			LodowyBlok.getEffected().remove(p);
			return damage;
		}
		
		return damage;
	}
	
}
