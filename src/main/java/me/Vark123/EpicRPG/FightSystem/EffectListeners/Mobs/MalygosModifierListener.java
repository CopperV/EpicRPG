package me.Vark123.EpicRPG.FightSystem.EffectListeners.Mobs;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import net.md_5.bungee.api.ChatColor;

public class MalygosModifierListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	private void onDamage(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamageSource().getCausingEntity();
		if(damager == null)
			return;
		
		String name = ChatColor.stripColor(victim.getName());
		if(name.equals("Malygos - Legendarny Boss") && e.getFinalDamage() > 10_000) {
			e.setDamage(e.getDamage()*0.5);
			return;
		}
		
		if(name.equals("Malygos - Legendary Boss")) {
			ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(victim);
			if(aMob.getStance().equals("phase3") && e.getFinalDamage() < 12_000)
				e.setDamage(e.getDamage()*0.5);
			if(aMob.getStance().equals("phase4"))
				e.setDamage(e.getDamage() * Math.random() * 0.5 + 0.5);
		}
	}
	
}
