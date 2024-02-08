package me.Vark123.EpicRPG.FightSystem.Listeners;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Core.ExpSystem;
import me.Vark123.EpicRPG.Core.StygiaSystem;
import me.Vark123.EpicRPG.FightSystem.ManualDamage;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;

public class PlayerDeathListener implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player victim = e.getEntity();
		Entity _killer = victim.getKiller();
		if(_killer == null) {
			Event event = victim.getLastDamageCause();
			if(event instanceof EntityDamageByEntityEvent)
				_killer = ((EntityDamageByEntityEvent) event).getDamager();
		}
		if(_killer == null)
			return;
		if(!(_killer instanceof Player))
			return;
		
		Player killer = (Player) _killer;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(killer);
		RpgSkills skills = rpg.getSkills();
		RpgModifiers modifiers = rpg.getModifiers();
		
		RpgPlayer victimRpg = PlayerManager.getInstance().getRpgPlayer(victim);
		int xp = victimRpg.getInfo().getLevel()*3;
		if(xp > 0) {
			ExpSystem.getInstance().addMobExp(rpg, xp);
			StygiaSystem.getInstance().addMobStygia(rpg, xp);
		}
		
		if(skills.hasRozprucie()) {
			Random rand = new Random();
			if(rand.nextInt(5) == 0) {
				
				victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_BIG_FALL, 1, 0.75f);
				DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 0), 0.4f);
				
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
							killer.getWorld().spawnParticle(Particle.REDSTONE, victim.getLocation().clone().add(0,1,0).add(clone), 4, 0.06f, 0.06f, 0.06f, 0.01f, dust);
							clone.add(vec);
							++timer;
						}
					}.runTaskTimer(Main.getInstance(), 0, 1);
				}
				
				double dmg = victim.getLastDamageCause().getDamage() * 0.3;
				victim.getWorld().getNearbyEntities(victim.getLocation(), 5, 5, 5, v -> {
					if(v.equals(killer) || !(v instanceof LivingEntity))
						return false;
					if(v.equals(victim))
						return false;
					if(v instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(v.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !v.getWorld().getName().toLowerCase().contains("dungeon"))
							return true;
						return false;
					}
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(v).isDamageable())
						return false;
					return true;
				}).forEach(v -> {
					EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(killer, v, DamageCause.CONTACT, dmg);
					Bukkit.getPluginManager().callEvent(event);
					if(event.isCancelled()) {
						return;
					}
					ManualDamage.doDamage(killer, (LivingEntity) v, dmg, event);
				});
			}
		}

		if(modifiers.hasWampiryzm()) {
			double restoreHp = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.05;
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled())
				killer.getWorld().spawnParticle(Particle.HEART, killer.getLocation(), 10, 0.5F, 0.5F, 0.5F, 0.1f);
		}
		if(modifiers.hasWampiryzm_h()) {
			double restoreHp = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.065;
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled())
				killer.getWorld().spawnParticle(Particle.HEART, killer.getLocation(), 10, 0.5F, 0.5F, 0.5F, 0.1f);
		}
		if(modifiers.hasWampiryzm_m()) {
			double restoreHp = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.09;
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled())
				killer.getWorld().spawnParticle(Particle.HEART, killer.getLocation(), 10, 0.5F, 0.5F, 0.5F, 0.1f);
		}
		
		if(modifiers.hasBarbarzynskiSzal()) {
			new BukkitRunnable() {
				int timer = 0;
				double restoreHp = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.0075;
				@Override
				public void run() {
					if(timer >= 20) {
						this.cancel();
						return;
					}
					++timer;
					RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp/2);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()){
						killer.getWorld().spawnParticle(Particle.HEART, killer.getLocation().add(0,1.25,0), 5, 0.5F, 0.5F, 0.5F, 0.1f);
					}
				}
			}.runTaskTimer(Main.getInstance(), 0, 10);
		}
	}
	
}
