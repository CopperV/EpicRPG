package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class LodowaStrzalaEffectListener implements Listener {
	
	private static final PotionEffect potion = new PotionEffect(PotionEffectType.SLOWNESS, 20*20, 2);
	private static final BlockData particleBlockData = Material.ICE.createBlockData();
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;
		
		LivingEntity damager = e.getDamager();
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.LODOWA_STRZALA))
			return;
		
		e.increaseModifier(0.22);
	}
	
	@EventHandler
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;
		
		LivingEntity victim = e.getVictim();
		LivingEntity damager = e.getDamager();
		
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.LODOWA_STRZALA))
			return;
		
		Location loc = victim.getLocation().clone().add(0,1,0);
		loc.getWorld().spawnParticle(Particle.BLOCK, loc, 12,
				.4, .4, .4, .05, particleBlockData);
		
		victim.addPotionEffect(potion);
	}

}
