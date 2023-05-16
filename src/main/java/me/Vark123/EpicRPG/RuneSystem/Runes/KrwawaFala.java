package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;

public class KrwawaFala extends ARune {

	private List<Entity> shooted;
	private Location loc;
	
	public KrwawaFala(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		loc = p.getLocation();
		shooted = new ArrayList<Entity>();
		p.getWorld().playSound(loc, Sound.ENTITY_PLAYER_BIG_FALL, 1, 0.1f);
		new BukkitRunnable() {
			
//			Location check = loc.clone();
			DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 0), 2);
			double t = 0;
			LivingEntity le;
			
			@Override
			public void run() {
				t+=0.25;
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/32)) {
					double x = t * Math.sin(theta);
					double z = t * Math.cos(theta);
					Location point = loc.clone();
					point.add(x, 0, z);
					if(point.distance(loc) > dr.getObszar() || !casterInCastWorld()) {
						this.cancel();
						return;
					}
					
					point.add(0, .3, 0);
					p.getWorld().spawnParticle(Particle.REDSTONE, point, 1, .2f, .2f, .2f, .1f, dust);
					point.add(0, .3, 0);
					p.getWorld().spawnParticle(Particle.REDSTONE, point, 1, .2f, .2f, .2f, .1f, dust);
					point.add(0, .3, 0);
					p.getWorld().spawnParticle(Particle.REDSTONE, point, 1, .2f, .2f, .2f, .1f, dust);
					point.add(0, .3, 0);
					p.getWorld().spawnParticle(Particle.REDSTONE, point, 1, .2f, .2f, .2f, .1f, dust);
					point.add(0, .3, 0);
					p.getWorld().spawnParticle(Particle.REDSTONE, point, 1, .2f, .2f, .2f, .1f, dust);
					point.add(0, .3, 0);
					p.getWorld().spawnParticle(Particle.REDSTONE, point, 1, .2f, .2f, .2f, .1f, dust);
					
				}
				
				loc.getWorld().getNearbyEntities(loc, t, t, t, e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
						return false;
					if(e.getLocation().distance(loc) > t)
						return false;
					if(e instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
							return true;
						return false;
					}
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).parallelStream().forEach(e -> {
					le = (LivingEntity) e;
					RuneDamage.damageNormal(p, le, dr, (p,le,dr)->{
						Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()->{
							new BukkitRunnable() {
								double t = 0;
								DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 0), 1f);
								double dmg = dr.getDamage()/50.0;
								@Override
								public void run() {
									
									if(t>15 || !casterInCastWorld() || !entityInCastWorld(e)) {
										this.cancel();
										return;
									}
									++t;
									
									if(le.isDead()) {
										this.cancel();
										return;
									}
									
									if(!RuneDamage.damageTiming(p, le, dr, dmg)) {
										this.cancel();
										return;
									}
									Location loc = le.getLocation().clone().add(0, 1, 0);
									loc.getWorld().playSound(e.getLocation(), Sound.ENTITY_SLIME_ATTACK, 1, 1.2f);
									loc.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 2, 0.2, 0.2, 0.2, 0.1, dust);
									
									
								}
							}.runTaskTimer(Main.getInstance(), 0, 20);
						}, 20);
					});
					e.getLocation().getWorld().playSound(e.getLocation(), Sound.ENTITY_HOSTILE_BIG_FALL, 1, .4f);
					shooted.add(e);
				});
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
