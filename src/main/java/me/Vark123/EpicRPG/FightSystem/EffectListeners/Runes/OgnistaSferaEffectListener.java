package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class OgnistaSferaEffectListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity victim = e.getVictim();
		LivingEntity damager = e.getDamager();
		if(damager == null || !(damager instanceof LivingEntity)
				|| victim.equals(damager))
			return;
		
		if(!Utils.hasEntityBuff(victim, EpicModifierTypes.SFERA))
			return;
		
		double dmg = e.getFinalDamage() * 0.5;
		
		DamageUtils.applyDirectDamageEffect(victim, damager, dmg, DamageType.MAGIC, DamageCause.CUSTOM);
		
	}

}
