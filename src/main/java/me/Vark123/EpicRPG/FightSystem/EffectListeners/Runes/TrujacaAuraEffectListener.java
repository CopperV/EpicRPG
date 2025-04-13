package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class TrujacaAuraEffectListener implements Listener {
	
	@EventHandler
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getDamageType().equals(me.Vark123.EpicRPG.FightSystem.DamageType.CUSTOM))
			return;
		
		LivingEntity victim = e.getVictim();
		LivingEntity damager = e.getDamager();
		
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.TRUJACA_AURA))
			return;
		
		DamageUtils.applyTimingDirectDamageEffect(
				damager, 
				victim,
				e.getFinalDamage() * 0.05,
				e.getDamageSource().getDamageType(),
				DamageCause.CUSTOM,
				loc -> {
					loc.getWorld().spawnParticle(Particle.ITEM_SLIME, loc.clone().add(0,1,0), 11,
							0.5, 0.5, 0.5, 0.1);
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 0.8f, 1.3f);
				},
				20,
				20,
				20*4);
	}

}
