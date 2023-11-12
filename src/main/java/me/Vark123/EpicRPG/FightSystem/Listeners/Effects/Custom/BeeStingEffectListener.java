package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom;

import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class BeeStingEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Bee))
			return;
		
		Bee bee = (Bee) damager;
		if(bee.hasStung())
			return;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				bee.setHasStung(false);
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 1);
		
	}

}
