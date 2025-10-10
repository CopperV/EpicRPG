package me.Vark123.EpicRPG.FightSystem.EffectListeners.Mobs;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.ManualDamage;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastCostCalcEvent;
import net.md_5.bungee.api.ChatColor;

public class LoathebModifierListener implements Listener {

	@EventHandler
	private void onHeal(RpgPlayerHealEvent e) {
		if(e.isCancelled())
			return;
		
		Player player = e.getP();
		World world = player.getWorld();
		if(!world.getName().toLowerCase().startsWith("dungeon12"))
			return;
		
		if(world.getNearbyEntities(player.getLocation(), 30, 10, 30, entity -> {
			String name = ChatColor.stripColor(entity.getName());
			return name.startsWith("Loatheb");
		}).size() > 0) {
			e.setHeal(e.getHealAmount() * 0.6);
		}
	}

	@EventHandler
	private void meleeModifier(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MELEE))
			return;
		
		Entity victim = e.getVictim();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Loatheb"))
			return;

		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_SLIME_HURT, 1.5f, 0.8f);
		victim.getWorld().spawnParticle(Particle.ITEM_SLIME, victim.getLocation().add(0,1,0), 8, .7f, .7f, .7f, 0.15f);
		e.decreaseModifier(0.5);
	}


	@EventHandler(priority = EventPriority.LOW)
	private void projectileNeutralizeModifier(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;

		Entity damager = e.getDamager();
		Entity victim = e.getVictim();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Loatheb"))
			return;
		
		if(Math.random() >= 0.2)
			return;

		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1.5f, 0.6f);
		damager.getWorld().spawnParticle(Particle.ITEM_SLIME, damager.getLocation(), 12, .3f, .3f, .3f, 0.1f);
		e.getDamageSource().getDirectEntity().remove();
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	private void projectileReflectModifier(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;

		Entity damager = e.getDamager();
		Entity victim = e.getVictim();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Loatheb"))
			return;

		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(victim);
		String phase = aMob.getStance();
		if(!(phase.equals("phase2") || phase.equals("phase3")))
			return;
		
		if(Math.random() >= 0.1)
			return;
		
		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_SLIME_JUMP, 1.5f, 1.2f);
		damager.getWorld().spawnParticle(Particle.COMPOSTER, damager.getLocation(), 12, .3f, .3f, .3f, 0.1f);

		EntityDamageByEntityEvent reflectDamageEvent = new EntityDamageByEntityEvent(victim, damager, DamageCause.CUSTOM, DamageSource.builder(org.bukkit.damage.DamageType.MOB_ATTACK).build(), e.getFinalDamage() * 0.1);
		Bukkit.getPluginManager().callEvent(reflectDamageEvent);
		ManualDamage.tryDoDamage((LivingEntity)victim, (LivingEntity)damager, reflectDamageEvent.getDamage(), reflectDamageEvent);
		
		e.setCancelled(true);
	}

	@EventHandler()
	private void projectileDebuffModifier(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;

		Entity victim = e.getVictim();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Loatheb"))
			return;

		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(victim);
		String phase = aMob.getStance();
		if(!phase.equals("phase3"))
			return;
		
		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_SLIME_HURT, 1.5f, 0.8f);
		victim.getWorld().spawnParticle(Particle.ITEM_SLIME, victim.getLocation().add(0,1,0), 8, .7f, .7f, .7f, 0.15f);
		e.decreaseModifier(0.4);
	}
	
	@EventHandler
	private void onRuneCalcCost(RuneCastCostCalcEvent event) {
		if(event.isCancelled())
			return;
		
		Player player = event.getRpgPlayer().getPlayer();
		World world = player.getWorld();
		if(world.getNearbyEntities(player.getLocation(), 30, 10, 30, entity -> {
			String name = ChatColor.stripColor(entity.getName());
			if(!name.startsWith("Loatheb"))
				return false;
			
			ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
			String phase = aMob.getStance();
			return phase.equals("phase2") || phase.equals("phase3");
		}).size() > 0) {
			event.increaseModifier(0.5);
		}
	}
}
