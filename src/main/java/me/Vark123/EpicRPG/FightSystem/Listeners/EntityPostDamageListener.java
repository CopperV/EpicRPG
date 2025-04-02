package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Calculators.IDamageCalculator.DamageCalculatorResult;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.FightSystem.Events.MagicEntityDamageByEntityEvent;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public class EntityPostDamageListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamage(EntityDamageEvent e) {
		if(e.isCancelled())
			return;

		if(e instanceof EntityDamageByEntityEvent)
			return;
		
		Entity _victim = e.getEntity();
		if(!(_victim instanceof Player))
			return;
		
		DamageSource source = e.getDamageSource();
		DamageType damageType = DamageType.CUSTOM;
		Player victim = (Player) _victim;
		
		DamageCause cause = e.getCause();
		if(cause.equals(DamageCause.FALL)
				|| cause.equals(DamageCause.LAVA)
				|| cause.equals(DamageCause.VOID))
			return;
		
		double damage = e.getDamage();
		DamageCalculatorResult damageInfo = new DamageCalculatorResult(damage, false);
		
		switch(e.getCause()) {
			case PROJECTILE:
				damageType = DamageType.PROJECTILE;
				break;
			case ENTITY_ATTACK:
				damageType = DamageType.MELEE;
				break;
			case CUSTOM:
				if(source.getDamageType().equals(org.bukkit.damage.DamageType.MAGIC))
					damageType = DamageType.MAGIC;
				else
					damageType = DamageType.CUSTOM;
				break;	
			default:
				damageType = DamageType.CUSTOM;
				break;
		}
		
		EpicPostDamageEffectEvent effectEvent = new EpicPostDamageEffectEvent(
				null, 
				victim, 
				source, 
				damageType, 
				damageInfo.damage,
				damageInfo);
		Bukkit.getPluginManager().callEvent(effectEvent);
		if(effectEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		
		damageInfo.damage = effectEvent.getFinalDamage();
		if(damageInfo.damage <= 0) {
			e.setCancelled(true);
			return;
		}
		
		e.setDamage(damageInfo.damage);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		
		if(e instanceof MagicEntityDamageByEntityEvent)
			return;

		DamageSource source = e.getDamageSource();
		Entity _damager = source.getCausingEntity();
		Entity _victim = e.getEntity();
		if(!(_damager instanceof LivingEntity) || !(_victim instanceof LivingEntity))
			return;
		
		LivingEntity damager = (LivingEntity) _damager;
		LivingEntity victim = (LivingEntity) _victim;

		double damage = e.getDamage();
		DamageType damageType;
		DamageCalculatorResult damageInfo = new DamageCalculatorResult(damage, false);
		
		switch(e.getCause()) {
			case PROJECTILE:
				damageType = DamageType.PROJECTILE;
				break;
			case ENTITY_ATTACK:
				damageType = DamageType.MELEE;
				break;
			case CUSTOM:
				if(source.getDamageType().equals(org.bukkit.damage.DamageType.MAGIC))
					damageType = DamageType.MAGIC;
				else
					damageType = DamageType.CUSTOM;
				break;	
			default:
				damageType = DamageType.CUSTOM;
				break;
		}
		
		EpicPostDamageEffectEvent effectEvent = new EpicPostDamageEffectEvent(
				damager, 
				victim, 
				source, 
				damageType, 
				damageInfo.damage,
				damageInfo);
		Bukkit.getPluginManager().callEvent(effectEvent);
		if(effectEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		
		damageInfo.damage = effectEvent.getFinalDamage();
		if(damageInfo.damage <= 0) {
			e.setCancelled(true);
			return;
		}
		
		e.setDamage(damageInfo.damage);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamage(MagicEntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;

		DamageSource source = e.getDamageSource();
		Entity _damager = source.getDirectEntity();
		Entity _victim = e.getEntity();
		if(!(_damager instanceof LivingEntity) || !(_victim instanceof LivingEntity))
			return;
		
		LivingEntity damager = (LivingEntity) _damager;
		LivingEntity victim = (LivingEntity) _victim;

		double damage = e.getDamage();
		DamageType damageType = DamageType.MAGIC;
		DamageCalculatorResult damageInfo = new DamageCalculatorResult(damage, false);

		EpicRune rune = e.getRune();
		
		EpicPostDamageEffectEvent effectEvent = new EpicPostDamageEffectEvent(
				damager, 
				victim, 
				source, 
				damageType, 
				damageInfo.damage,
				damageInfo,
				rune);
		Bukkit.getPluginManager().callEvent(effectEvent);
		if(effectEvent.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		
		damageInfo.damage = effectEvent.getFinalDamage();
		if(damageInfo.damage <= 0) {
			e.setCancelled(true);
			return;
		}
		
		e.setDamage(damageInfo.damage);
	}
	
}
