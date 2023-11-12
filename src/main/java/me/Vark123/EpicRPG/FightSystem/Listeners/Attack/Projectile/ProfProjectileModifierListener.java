package me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Projectile;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import net.md_5.bungee.api.ChatColor;

public class ProfProjectileModifierListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.PROJECTILE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Projectile))
			return;
		
		damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(!ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("mys"))
			return;
		
		e.increaseModifier(0.15);
	}

}
