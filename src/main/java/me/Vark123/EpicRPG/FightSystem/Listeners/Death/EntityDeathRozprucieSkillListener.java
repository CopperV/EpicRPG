package me.Vark123.EpicRPG.FightSystem.Listeners.Death;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.damage.DamageType;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDeathEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class EntityDeathRozprucieSkillListener implements Listener {
	
	private static final Random rand = new Random();
	private static final DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 0), 0.4f);
	
	@EventHandler
	private void onDeath(EpicDeathEvent e) {
		Player killer = e.getPlayerKiller();
		if(killer == null)
			return;
		
		LivingEntity victim = e.getVictim();
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(killer);
		RpgSkills skills = rpg.getSkills();
		if(!skills.hasRozprucie())
			return;
		
		EntityDamageEvent lastCause = Utils.getLastDamageCause(victim);
		if(lastCause != null && lastCause instanceof EntityDamageByEntityEvent
				&& !((EntityDamageByEntityEvent) lastCause).getDamager().getUniqueId().equals(killer.getUniqueId()))
			return;
		
		if(rand.nextInt(5) != 0)
			return;
		
		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_BIG_FALL, 1, 0.75f);	
		
		List<Vector> directions = new LinkedList<>();
		double r = 2;
		double x,y,z,theta;
		for(int i = 0; i < 20; ++i) {
			theta = Math.random()*Math.PI*2;
			x = r * Math.sin(theta);
			y = Math.random()*2.5+0.1;
			z = r * Math.cos(theta);
			directions.add(new Vector(x, y, z).normalize().multiply(0.25));
		}
		for(Vector vec : directions) {
			new BukkitRunnable() {
				Vector clone = vec.clone();
				int timer = 0;
				@Override
				public void run() {
					if(timer >= 20) {
						this.cancel();
						return;
					}
					killer.getWorld().spawnParticle(Particle.DUST, victim.getLocation().clone().add(0,1,0).add(clone), 4, 0.06f, 0.06f, 0.06f, 0.01f, dust);
					clone.add(vec);
					++timer;
				}
			}.runTaskTimer(Main.getInstance(), 0, 1);
		}
		
		double radius = 5;
		Location loc = victim.getLocation().clone();
		double damage = lastCause.getFinalDamage() * 0.25;
		IRuneHitCondition hitCondition = new PvPRuneHitCondition();
		victim.getWorld().getNearbyEntities(loc, radius, radius, radius, entity -> {
			if(entity.getLocation().distanceSquared(loc) > radius * radius)
				return false;
			
			if(!(entity instanceof LivingEntity))
				return false;
			
			LivingEntity le = (LivingEntity) entity;

			return hitCondition.check(killer, le);
		}).forEach(entity -> {
			DamageUtils.applyDirectDamageEffect(killer, (LivingEntity) entity, damage, DamageType.GENERIC, DamageCause.CUSTOM);
		});
	}

}
