package me.Vark123.EpicRPG.FightSystem.Modifiers;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageModifyEvent implements Listener {

	@EventHandler(priority=EventPriority.MONITOR)
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		Map<EventPriority, List<DamageModifier>> modifiers = DamageModifierManager.getInstance().getModifiers();
		
		MutableDouble modDouble = new MutableDouble(e.getDamage());
		modifiers.keySet().stream().takeWhile(p -> {
			return !e.isCancelled();
		}).forEachOrdered(p -> {
			modifiers.get(p).stream().takeWhile(mod -> {
				return !e.isCancelled();
			}).forEachOrdered(mod -> {
				modDouble.setValue(mod.modifyDamage(e.getDamager(), e.getEntity(), modDouble.doubleValue(), e.getCause()));
				if(modDouble.doubleValue() < 0)
					e.setCancelled(true);
			});
		});
		
		e.setDamage(modDouble.doubleValue());
	}
	
}
