package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class ShulkerModifierEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Shulker))
			return;
		
		BoundingBox box = victim.getBoundingBox();
		if(Math.abs(box.getMaxY()-box.getMinY()) >= 1.6)
			return;
		
		e.setDmg(e.getDmg() * 0.1);
	}

}
