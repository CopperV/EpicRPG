package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class ZwiastunWojny_H extends ARune {

	public ZwiastunWojny_H(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier2 = true;
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1, .6f);
		
		new BukkitRunnable() {
			int timer = 11*2;
			double radius = dr.getObszar();
			Random rand = new Random();
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				
				Location loc = p.getLocation();
				for(int i = 0; i < 6; ++i) {
					double r = rand.nextDouble(radius);
					double angle = rand.nextDouble(Math.PI*2);
					
					Location dst = loc.clone();
					double x = Math.sin(angle) * r;
					double z = Math.cos(angle) * r;
					dst.add(x, 0, z);
					
					Location src = dst.clone().add(0, 20, 0);
					r = rand.nextDouble(8);
					angle = rand.nextDouble(Math.PI*2);
					x = Math.sin(angle) * r;
					z = Math.cos(angle) * r;
					src.add(x, 0, z);
					
					castMeteor(src, dst);
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
	}
	
	private void castMeteor(Location src, Location dst) {
		Vector vec = dst.toVector().subtract(src.toVector()).normalize();
		Location loc = src.clone();
		new BukkitRunnable() {
			double h = src.distance(dst) * 1.2;
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!casterInCastWorld()) {
					cancel();
					return;
				}
				if(loc.distance(src) > h || loc.getBlock().getType().isSolid()) {
					castExplode(loc);
					cancel();
					return;
				}
				
				loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.2f, 1);
				loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 4, 0.15, 0.15, 0.15, 0);
				loc.add(vec);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void castExplode(Location loc) {
		Collection<Entity> shooted = new HashSet<>();
		new BukkitRunnable() {
			double maxRadius = dr.getObszar() * 0.25;
			double r = 0.5;
			@Override
			public void run() {
				if(r > maxRadius || !casterInCastWorld()) {
					this.cancel();
					return;
				}

				loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.2f, 1);
				double step = Math.PI*2 / (r*6);
				for(double theta = 0; theta <= Math.PI*2; theta += step) {
					Location pos = loc.clone();
					double x = Math.sin(theta) * r;
					double z = Math.cos(theta) * r;
					pos.add(x, 0.5, z);
					
					p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, pos, 1, 0, 0, 0, 0);
				}
				
				loc.getWorld().getNearbyEntities(loc, r, 4, r, e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
						return false;
					if(e.getLocation().distance(loc) > r)
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
					if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
							&& e.getType().equals(EntityType.ARMOR_STAND))
						return false;
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).forEach(e -> {
					RuneDamage.damageNormal(p, (LivingEntity) e, dr, (p, le, dr)->{
						new BukkitRunnable() {
							int timer = 14;
							double dmg = dr.getDamage()/50.0;
							@Override
							public void run() {
								if(timer <= 0 || !casterInCastWorld() || !entityInCastWorld(e)) {
									this.cancel();
									return;
								}
								--timer;
								boolean end = RuneDamage.damageTiming(p, le, dr, dmg);
								if(!end) {
									this.cancel();
									return;
								}
								Location loc = le.getLocation().add(0,1,0);
								p.getWorld().spawnParticle(Particle.FLAME, loc,10,0.2,0.2,0.2,0.05);
								p.getWorld().playSound(loc, Sound.ENTITY_GENERIC_BURN, 1, 1);
							}
						}.runTaskTimer(Main.getInstance(), 0, 20);
					});
					shooted.add(e);
				});
				
				r += 0.5;
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}
	
}
