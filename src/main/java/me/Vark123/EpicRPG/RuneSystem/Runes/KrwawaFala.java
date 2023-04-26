package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

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
					
					for(Entity entity : point.getWorld().getNearbyEntities(loc, 3, 5, 3)) {
						AxisAlignedBB aabb = ((CraftEntity)entity).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(point.getX()-1.5, point.getY()-4, point.getZ()-1.5, point.getX()+1.5, point.getY()+4, point.getZ()+1.5);
						if(aabb.c(aabb2)) {
							if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(loc) <= dr.getObszar()) {
								if(entity instanceof Player || entity.hasMetadata("NPC")) {
									RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
									ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
									if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
										continue;
								}
								le = (LivingEntity) entity;
								RuneDamage.damageNormal(p, le, dr, (p,le,dr)->{
									Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()->{
										new BukkitRunnable() {
											double t = 0;
											DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 0), 1f);
											double dmg = dr.getDamage()/50.0;
											@Override
											public void run() {
												
												if(t>15 || !casterInCastWorld() || !entityInCastWorld(entity)) {
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
												loc.getWorld().playSound(loc, Sound.ENTITY_SLIME_ATTACK, 1, 1.2f);
												loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 2, 0.2, 0.2, 0.2, 0.1, dust);
												
												
											}
										}.runTaskTimer(Main.getInstance(), 0, 20);
									}, 20);
								});
								entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.ENTITY_HOSTILE_BIG_FALL, 1, .4f);
								shooted.add(entity);
							}
						}
					}
					
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
