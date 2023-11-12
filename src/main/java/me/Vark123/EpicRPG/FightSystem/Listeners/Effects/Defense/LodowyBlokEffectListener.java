package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Defense;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.Runes.LodowyBlok;

public class LodowyBlokEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		
		if(victim instanceof Player) {
			Player p = (Player) victim;
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			RpgModifiers modifier = rpg.getModifiers();
			if(!modifier.hasLodowyBlok())
				return;
			if(!LodowyBlok.getEffected().contains(p))
				return;
			
			victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1, 2);
			e.setCancelled(true);
			return;
		}
		
		if(damager instanceof Player) {
			Player p = (Player) damager;
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			RpgModifiers modifier = rpg.getModifiers();
			if(!modifier.hasLodowyBlok())
				return;
			if(!LodowyBlok.getEffected().contains(p))
				return;

			LodowyBlok.getEffected().remove(p);
			return;
		}
		
	}

}
