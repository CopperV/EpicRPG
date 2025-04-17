package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
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

public class KrwawaStrzalaEffectListener implements Listener {
	
	private static final DustOptions dust = new DustOptions(Color.RED, 1.3f);
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;
		
		LivingEntity damager = e.getDamager();
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.KRWAWA_STRZALA))
			return;
		
		e.increaseModifier(0.2);
	}
	
	@EventHandler
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;
		
		LivingEntity victim = e.getVictim();
		LivingEntity damager = e.getDamager();
		
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.KRWAWA_STRZALA))
			return;
		
		DamageUtils.applyTimingDirectDamageEffect(
				damager, 
				victim,
				e.getFinalDamage() * 0.075,
				org.bukkit.damage.DamageType.ARROW,
				DamageCause.CUSTOM,
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc.clone().add(0,1,0), 12,
							0.3, 0.5, 0.3, 0.04, dust);
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 0.8f, 0.7f);
				},
				30,
				30,
				20*8);
	}

}
