package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
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

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class BurzaOgnista extends ARune {

	private List<Entity> shooted;
	
	public BurzaOgnista(ItemStackRune dr, Player p) {
		super(dr, p);
		shooted = new ArrayList<Entity>();
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
		new BukkitRunnable() {
			LivingEntity le;
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			public void run() {
				t++;
				double x = vec.getX()*t;
				double y = vec.getY()*t+1.5;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc,10,0,0,0,0);
				
				loc.getWorld().getNearbyEntities(loc, 5, 5, 5, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-2, loc.getY()-2, loc.getZ()-2, loc.getX()+2, loc.getY()+2, loc.getZ()+2);
					if(!aabb.c(aabb2))
						return false;
					if(e.equals(p) || !(e instanceof LivingEntity))
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
				}).parallelStream().min((e1, e2) -> {
					double dist1 = e1.getLocation().distanceSquared(loc);
					double dist2 = e2.getLocation().distanceSquared(loc);
					if(dist1 == dist2)
						return 0;
					return dist1 < dist2 ? -1 : 1;
				}).ifPresent(e -> {
					le = (LivingEntity) e;
					RuneDamage.damageNormal(p, le, dr, (p, le, dr)->{
						new BukkitRunnable() {
							int timer = 10;
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
					e.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
					shooted.add(e);
					rangeSpell(e.getLocation());
					this.cancel();
				});
				
				if(this.isCancelled())
					return;
				
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					rangeSpell(loc);
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>75 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

	private void rangeSpell(Location loc) {
		new BukkitRunnable() {
			LivingEntity le;
			double t = 0;
			Location check = loc.clone();
			@Override
			public void run() {
				t+=0.5;
				for(double theta = 0;theta<=Math.PI*2;theta=theta+(Math.PI/8)) {
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x,0.7,z);
					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
						this.cancel();
						return;
					}
					p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc,10,0,0,0,0);
					
					loc.getWorld().getNearbyEntities(loc, 5, 5, 5, e -> {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1.5, loc.getY()-1.5, loc.getZ()-1.5, loc.getX()+1.5, loc.getY()+1.5, loc.getZ()+1.5);
						if(!aabb.c(aabb2))
							return false;
						if(shooted.contains(e))
							return false;
						if(e.getLocation().distance(check) > dr.getObszar())
							return false;
						if(e.equals(p) || !(e instanceof LivingEntity))
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
						RuneDamage.damageNormal(p, le, dr,(p, le, dr) -> {
							new BukkitRunnable() {
								int timer = 10;
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
						e.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
						shooted.add(e);
					});
					loc.subtract(x,0.7,z);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}
	
}
