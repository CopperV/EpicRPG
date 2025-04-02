package me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;

public class ShulkerEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	private void onDamage(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Shulker))
			return;
		
		BoundingBox box = victim.getBoundingBox();
		if(Math.abs(box.getMaxY()-box.getMinY()) >= 1.6)
			return;
		
		e.setDamage(e.getDamage() * 0.1);
	}

}
