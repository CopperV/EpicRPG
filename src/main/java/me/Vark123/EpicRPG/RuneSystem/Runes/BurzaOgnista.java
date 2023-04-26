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
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
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
//			double dmg = dr.getDamage();
//			RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
			public void run() {
				t++;
				double x = vec.getX()*t;
				double y = vec.getY()*t+1.5;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc,10,0,0,0,0);
				for(Entity e:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-2, loc.getY()-2, loc.getZ()-2, loc.getX()+2, loc.getY()+2, loc.getZ()+2);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
//							if(rpg.hasInkantacja()) dmg *= 1.3;
							le = (LivingEntity)e;
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
//							if(!(e instanceof ArmorStand))
//								e.setFireTicks(20*30);
							shooted.add(e);
							rangeSpell(e.getLocation());
							this.cancel();
							return;
						}
					}
				}
//				for(Entity e:loc.getChunk().getEntities()) {
//					if(e.getLocation().distance(loc) < 2.0 || e.getLocation().add(0, 1, 0).distance(loc) < 2.0 || e.getLocation().add(0, 1, 0).distance(loc) < 2.0) {
//						if(!e.equals(p) && e instanceof LivingEntity) {
//							if(e instanceof Player || e.hasMetadata("NPC")) {
//								ApplicableRegionSet set = WorldGuardPlugin.inst().getRegionManager(e.getWorld()).getApplicableRegions(e.getLocation());
//								if(set.queryState(null, DefaultFlag.PVP) == State.DENY) 
//									continue;
//							}
//							((LivingEntity)e).damage(dr.getDamage(), p);
//							e.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
//							e.setFireTicks(20*30);
//							shooted.add(e);
//							rangeSpell(e.getLocation());
//							this.cancel();
//							return;
//						}
//					}
//				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
//				if(!loc.getBlock().getType().equals(Material.AIR)) {
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
//			double dmg = dr.getDamage();
//			RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
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
					for(Entity entity:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
						AxisAlignedBB aabb = ((CraftEntity)entity).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1.5, loc.getY()-1.5, loc.getZ()-1.5, loc.getX()+1.5, loc.getY()+1.5, loc.getZ()+1.5);
						if(aabb.c(aabb2)) {
							if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(check) <= dr.getObszar()) {
								if(entity instanceof Player || entity.hasMetadata("NPC")) {
									RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
									ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
									if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
										continue;
								}
//								if(rpg.hasInkantacja()) dmg *= 1.3;
								le = (LivingEntity)entity;
								RuneDamage.damageNormal(p, le, dr,(p, le, dr) -> {
									new BukkitRunnable() {
										int timer = 10;
										double dmg = dr.getDamage()/50.0;
										@Override
										public void run() {
											if(timer <= 0 || !casterInCastWorld() || !entityInCastWorld(entity)) {
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
								entity.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
//								if(!(entity instanceof ArmorStand))
//									entity.setFireTicks(20*30);
								shooted.add(entity);
							}
						}
					}
//					for(Entity entity:loc.getChunk().getEntities()) {
//						if(entity.getLocation().distance(loc) < 0.75 || entity.getLocation().add(0, 1, 0).distance(loc) < 0.75 || entity.getLocation().add(0, 1, 0).distance(loc) < 0.75) {
//							if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(check) <= dr.getObszar()) {
//								((LivingEntity)entity).setLastDamageCause(new EntityDamageEvent(p, DamageCause.CUSTOM, dr.getDamage()));
//								((LivingEntity)entity).damage(dr.getDamage(), p);
//								shooted.add(entity);
//								if(!loc.getWorld().getPVP() && entity instanceof Player) return;
//								entity.setFireTicks(20*20);
//							}
//						}
//					}
					loc.subtract(x,0.7,z);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}
	
//	private void rangeSpell(Entity e) {
//		new BukkitRunnable() {
//			double t = 0;
//			Location loc = e.getLocation();
//			Location check = e.getLocation();
//			@Override
//			public void run() {
//				t+=0.5;
//				for(double theta = 0;theta<=Math.PI*2;theta=theta+(Math.PI/8)) {
//					double x = t*Math.sin(theta);
//					double z = t*Math.cos(theta);
//					loc.add(x,0.7,z);
//					if(loc.distance(check)>dr.getObszar()) {
//						this.cancel();
//					}
//					p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc,10,0,0,0,0);
//					for(Entity entity:loc.getChunk().getEntities()) {
//						if(entity.getLocation().distance(loc) < 0.75 || entity.getLocation().add(0, 1, 0).distance(loc) < 0.75 || entity.getLocation().add(0, 1, 0).distance(loc) < 0.75) {
//							if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(check) <= dr.getObszar()) {
//								((LivingEntity)entity).damage(dr.getDamage(), p);
//								if(!e.getWorld().getPVP() && e instanceof Player) return;
//								entity.setFireTicks(20*20);
//								shooted.add(entity);
//							}
//						}
//					}
//					loc.subtract(x,0.7,z);
//				}
//			}
//		}.runTaskTimer(Main.getInstance(), 0, 5);
//	}
	
}
