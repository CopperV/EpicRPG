package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.RuneSystem.Runes.Prowokacja;

public class EntityTauntEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!Prowokacja.getTargets().containsKey(victim))
			return;
		
		ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(victim);
		if(mob == null) 
			return;
		
		if(mob.hasThreatTable())
			mob.getThreatTable().Taunt(BukkitAdapter.adapt(Prowokacja.getTargets().get(victim)));
		else
			mob.setTarget(BukkitAdapter.adapt(Prowokacja.getTargets().get(victim)));
		
	}

}
