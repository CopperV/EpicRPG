package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDeathEvent;
import me.Vark123.EpicRPG.Utils.Utils;

public class EntityDeathListener implements Listener {

	@EventHandler
	public void onMobDeath(MythicMobDeathEvent e) {
		Entity victim = e.getEntity();
		if(!(victim instanceof LivingEntity))
			return;
		
		handleDeathEvent((LivingEntity) victim);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player victim = e.getEntity();
		handleDeathEvent(victim);
	}
	
	private void handleDeathEvent(LivingEntity victim) {
		
		EntityDamageEvent event = Utils.getLastDamageCause(victim);
		if(event == null || !(event instanceof EntityDamageByEntityEvent))
			return;		
		
		Entity killer = ((EntityDamageByEntityEvent) event).getDamager();
		if(killer instanceof AbstractArrow arrow && arrow.getShooter() instanceof Entity)
			killer = (Entity) arrow.getShooter();
		
		if(MythicBukkit.inst().getMobManager().isMythicMob(killer)) {
			ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(killer);
			if(aMob.getOwnerUUID().isPresent())
				killer = Bukkit.getEntity(aMob.getOwnerUUID().get());
		}
		if(!(killer instanceof LivingEntity))
			return;


		EpicDeathEvent e = new EpicDeathEvent((LivingEntity) killer, (LivingEntity) victim);
		Bukkit.getPluginManager().callEvent(e);
	}
	
}
