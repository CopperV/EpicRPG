package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Melee;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class ThreatTableModifyEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getDamager() == null)
			return;
		if(!e.getDamageType().equals(EpicDamageType.MELEE))
			return;
		
		Entity victim = e.getVictim();
		
		MythicBukkit.inst().getMobManager().getActiveMob(victim.getUniqueId()).ifPresent(mob -> {
			if(!mob.hasThreatTable())
				return;
			mob.getThreatTable().threatGain(BukkitAdapter.adapt(e.getDamager()), e.getFinalDamage());
		});
		
	}

}
