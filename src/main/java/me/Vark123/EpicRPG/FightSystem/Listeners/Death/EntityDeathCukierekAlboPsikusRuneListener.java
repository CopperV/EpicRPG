package me.Vark123.EpicRPG.FightSystem.Listeners.Death;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDeathEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.RuneEffectType;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class EntityDeathCukierekAlboPsikusRuneListener implements Listener {

	@EventHandler
	private void onDeath(EpicDeathEvent e) {
		LivingEntity victim = e.getVictim();
		if(!Utils.hasEntityEffect(victim, RuneEffectType.CUKIEREK_ALBO_PSIKUS))
			return;
		
		LivingEntity damager = Utils.getEntityEffectCaster(victim, RuneEffectType.CUKIEREK_ALBO_PSIKUS);
		if(damager == null || damager.isDead() ||
				!(damager instanceof Player) || !((Player)damager).isOnline())
			return;

		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) damager);
		double damage = rpg.getInfo().getLevel() * 50;

		Location loc = victim.getLocation().clone().add(0, 0.25, 0);
		double radius = 4;
		
		loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 30, 0.4f, 0.4f, 0.4f, 0.15f);
		loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 1.2f, 0.8f);
		
		IRuneHitCondition hitCondition = new NonPvPRuneHitCondition();
		victim.getWorld().getNearbyEntities(loc, radius, radius, radius, entity -> {
			if(entity.getLocation().distanceSquared(loc) > radius * radius)
				return false;
			
			if(!(entity instanceof LivingEntity))
				return false;
			
			LivingEntity le = (LivingEntity) entity;

			return hitCondition.check((Player) damager, le);
		}).forEach(entity -> {
			DamageUtils.applyDirectDamageEffect(damager, (LivingEntity) entity, damage, DamageType.GENERIC, DamageCause.CUSTOM);
		});
	}
	
}
