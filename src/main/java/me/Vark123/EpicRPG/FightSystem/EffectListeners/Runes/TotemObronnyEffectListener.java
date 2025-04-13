package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class TotemObronnyEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity victim = e.getVictim();
		if(victim == null)
			return;
		
		if(!Utils.hasEntityBuff(victim, EpicModifierTypes.TOTEM_OBRONNY))
			return;
		
		e.decreaseModifier(0.2);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity victim = e.getVictim();
		LivingEntity damager = e.getDamager();
		if(damager == null || !(damager instanceof LivingEntity)
				|| victim.equals(damager))
			return;
		
		if(!Utils.hasEntityBuff(victim, EpicModifierTypes.TOTEM_OBRONNY))
			return;
		
		double dmg = e.getFinalDamage() * 0.3;
		
		DamageUtils.applyDirectDamageEffect(victim, damager, dmg, e.getDamageSource().getDamageType(), DamageCause.CUSTOM);
		
	}

}
