package me.Vark123.EpicRPG.FightSystem.EffectListeners.PostMisc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;

public class DollInfoEffectListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!victim.getName().equalsIgnoreCase("§b§oManekin treningowy"))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player)) {
			if(!MythicBukkit.inst().getMobManager().isMythicMob(damager))
				return;
			
			ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(damager);
			if(aMob == null || !aMob.getOwnerUUID().isPresent()
					|| Bukkit.getPlayer(aMob.getOwnerUUID().get()) == null)
				return;
			
			damager = Bukkit.getEntity(aMob.getOwnerUUID().get());
		}

		double damage = e.getFinalDamage();
		damager.sendMessage("§7[§c§lTRENING§7] §aZadales §e"+String.format("%.2f", damage)+" §aobrazen.");
	}

}
