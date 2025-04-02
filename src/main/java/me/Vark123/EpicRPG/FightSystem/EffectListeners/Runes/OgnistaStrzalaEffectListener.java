package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class OgnistaStrzalaEffectListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;
		
		LivingEntity damager = e.getDamager();
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.OGNISTA_STRZALA))
			return;
		
		e.increaseModifier(0.24);
	}
	
	@EventHandler
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;
		
		LivingEntity victim = e.getVictim();
		LivingEntity damager = e.getDamager();
		
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.OGNISTA_STRZALA))
			return;
		
		DamageUtils.applyTimingDirectDamageEffect(
				damager, 
				victim,
				e.getFinalDamage() * 0.05,
				org.bukkit.damage.DamageType.ARROW,
				DamageCause.CUSTOM,
				loc -> {
					loc.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(0,1,0), 9,
							0.3, 0.5, 0.3, 0.04);
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 0.8f, 1.1f);
				},
				20,
				20,
				20*6);
	}

}
