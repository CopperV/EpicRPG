package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Projectile;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.RuneTimeEffect;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;

public class ProjectileEffectsListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.PROJECTILE))
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(!(damager instanceof Projectile))
			return;
		
		Projectile projectile = (Projectile) damager;
		damager = (Entity) projectile.getShooter();
		if(!(damager instanceof Player))
			return;

		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(modifiers.hasLodowaStrzala()) {
			PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, 20*15, 2);
			((LivingEntity)victim).addPotionEffect(pe);
			Location loc = damager.getLocation().add(0, 1, 0);
			Location loc2 = victim.getLocation().add(0, 1, 0);
			loc.getWorld().spawnParticle(Particle.SNOWBALL, loc, 15, 0.2, 0.2, 0.2, 0.05);
			loc2.getWorld().spawnParticle(Particle.SNOWBALL, loc2, 15, 0.2, 0.2, 0.2, 0.05);
		}
		if(modifiers.hasZakletaStrzala()) {
			PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, 20*20, 2);
			((LivingEntity)victim).addPotionEffect(pe);
			Location loc = damager.getLocation().add(0, 1, 0);
			Location loc2 = victim.getLocation().add(0, 1, 0);
			loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.2, 0.2, 0.2, 0.05);
			loc2.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc2, 15, 0.2, 0.2, 0.2, 0.05);
		}
		if(modifiers.hasSwietaStrzala()) {
			Location loc = damager.getLocation().add(0, 1, 0);
			Location loc2 = victim.getLocation().add(0, 1, 0);
			loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 15, 0.2, 0.2, 0.2, 0.05);
			loc2.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc2, 15, 0.2, 0.2, 0.2, 0.35);
		}
		if(modifiers.hasOgnistaStrzala()) {
			double dmg = e.getDmg() * 0.075;
			if(RuneDamage.directDamageEffect(p, (LivingEntity) victim, 
					Optional.of(dmg), RuneUtils.getCustomTimeEffect(dmg, RuneTimeEffect.FIRE, e.getDamageType()))) {
				Location loc2 = victim.getLocation().add(0, 1, 0);
				loc2.getWorld().spawnParticle(Particle.FLAME, loc2, 15, 0.2, 0.2, 0.2, 0.05);
			}
		}
		if(modifiers.hasZatrutaStrzala()) {
			double dmg = e.getDmg()*0.03;
			if(RuneDamage.directDamageEffect(p, (LivingEntity) victim, 
					Optional.of(dmg), RuneUtils.getCustomTimeEffect(dmg, RuneTimeEffect.POISON, e.getDamageType()))) {
				Location loc2 = victim.getLocation().add(0, 1, 0);
				loc2.getWorld().spawnParticle(Particle.SLIME, loc2, 15, 0.2, 0.2, 0.2, 0.05);
			}
		}
		if(modifiers.hasKrwawaStrzala()) {
			double dmg = e.getDmg() * 0.04;
			RuneDamage.directDamageEffect(p, (LivingEntity) victim, 
					Optional.of(dmg), RuneUtils.getCustomTimeEffect(dmg, RuneTimeEffect.BLOOD, e.getDamageType()));
		}
		
	}

}
