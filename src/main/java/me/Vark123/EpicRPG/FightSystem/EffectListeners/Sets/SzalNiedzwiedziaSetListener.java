package me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class SzalNiedzwiedziaSetListener implements Listener {

	@EventHandler
	public void onDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player player))
			return;
		
		Location sourceLoc = victim.getLocation().clone();
		double radius = 12;
		
		int amount = sourceLoc.getWorld().getNearbyEntities(sourceLoc, radius, radius, radius, entity -> {
			if(entity.getLocation().distanceSquared(sourceLoc) > radius * radius)
				return false;

			if(!MythicBukkit.inst().getMobManager().isMythicMob(entity))
				return false;
			
			ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
			if(aMob.isDead() || aMob.getType().getIsInvincible()
					|| (aMob.hasFaction() && (aMob.getFaction().equals("ALLY") || aMob.getFaction().equals("SUMMONS"))))
				return false;
			
			if(!aMob.getEntity().isDamageable())
				return false;
			
			return true;
		}).size();
		
		if(amount < 8)
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		if(info.getSetCounts().getOrDefault("Szal_Niedzwiedzia", 0) < 3)
			return;
		e.decreaseModifier(0.12);
	}
	
	@EventHandler
	public void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;
		
		Location sourceLoc = damager.getLocation().clone();
		double radius = 12;
		
		int amount = sourceLoc.getWorld().getNearbyEntities(sourceLoc, radius, radius, radius, entity -> {
			if(entity.getLocation().distanceSquared(sourceLoc) > radius * radius)
				return false;

			if(!MythicBukkit.inst().getMobManager().isMythicMob(entity))
				return false;
			
			ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
			if(aMob.isDead() || aMob.getType().getIsInvincible()
					|| (aMob.hasFaction() && (aMob.getFaction().equals("ALLY") || aMob.getFaction().equals("SUMMONS"))))
				return false;
			
			if(!aMob.getEntity().isDamageable())
				return false;
			
			return true;
		}).size();
		
		if(amount < 8)
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Szal_Niedzwiedzia", 0) < 4)
			return;
		
		e.increaseModifier(0.15);
	}
	
}
