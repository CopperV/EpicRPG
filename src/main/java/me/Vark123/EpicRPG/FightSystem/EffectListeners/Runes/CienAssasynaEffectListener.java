package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class CienAssasynaEffectListener implements Listener {
	
	private Random rand = new Random();
	
	@EventHandler
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getDamageType().equals(DamageType.CUSTOM))
			return;
		
		LivingEntity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.CIEN_ASSASYNA))
			return;
		
		double chance = rand.nextDouble();
		if(chance > 0.1)
			return;
		
		Entity victim = e.getVictim();
		
		IRuneHitCondition hitCondition = new PvPRuneHitCondition();
		Collection<Entity> shooted = new HashSet<>();
		shooted.add(victim);
		
		damager.getWorld().playSound(damager, Sound.ENTITY_WITHER_SHOOT, 0.8f, 1.6f);
		new BukkitRunnable() {
			double damage = e.getFinalDamage() * 0.2;
			Collection<Entity> lasts = new HashSet<>(shooted);
			Collection<Entity> newTargets = new HashSet<>();
			@Override
			public void run() {
				if(e.isCancelled())
					return;
				newTargets.clear();
				
				lasts.stream().forEach(entity -> {
					Location loc = entity.getLocation().clone();
					entity.getWorld().getNearbyEntities(loc, 3, 3, 3, entity2 -> {
						if(entity2.getLocation().distanceSquared(loc) > 3 * 3)
							return false;
						
						if(shooted.contains(entity2))
							return false;
						
						if(!(entity2 instanceof LivingEntity))
							return false;
						
						LivingEntity le = (LivingEntity) entity2;
						return hitCondition.check((Player) damager, le);
					}).forEach(entity2 -> {
						shooted.add(entity2);
						newTargets.add(entity2);
						if(DamageUtils.applyDirectDamageEffect(
								damager, 
								(LivingEntity) entity2,
								damage,
								e.getDamageSource().getDamageType(),
								DamageCause.CUSTOM)) {
							
							Utils.drawLine(Particle.SMOKE, entity.getLocation().clone().add(0,1,0), entity2.getLocation().clone().add(0,1,0),
									0.1, 2, .1f, .1f, .1f, .02f);
						}
					});
				});
				
				if(newTargets.isEmpty() || damager.isDead() ||
						(damager instanceof Player && !((Player)damager).isOnline())) {
					cancel();
					return;
				}
				
				lasts.clear();
				lasts.addAll(newTargets);
				
				damage *= 0.2;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
