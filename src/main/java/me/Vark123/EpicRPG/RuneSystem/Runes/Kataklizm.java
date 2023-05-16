package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;
import net.minecraft.world.phys.AxisAlignedBB;

public class Kataklizm extends ARune {
	
	private Random rand = new Random();
	
	public Kataklizm(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier2 = true;
	}

	@Override
	public void castSpell() {
		Random rand = new Random();
		switch(rand.nextInt(6)) {
			case 0:
				effect1();
				break;
			case 1:
				effect2();
				break;
			case 2:
				effect3();
				break;
			case 3:
				effect4();
				break;
			case 4:
				effect5();
				break;
			case 5:
				effect6();
				break;
		}

		RuneManager.getInstance().getObszarowkiCd().put(p, new Date());
	}
	
	//Deszcz meteorytow
	private void effect1() {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 2, 0.8f);
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*2;
			double obszar = dr.getObszar()*1.5;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				Location loc = p.getLocation();
				Location l1 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*obszar-obszar, 
						loc.getY(), 
						loc.getZ()+Math.random()*2*obszar-obszar);
				Location l2 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*obszar-obszar, 
						loc.getY(), 
						loc.getZ()+Math.random()*2*obszar-obszar);
				spellEffect1(l1);
				spellEffect1(l2);
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
	}
	
	private void spellEffect1(Location loc) {
		double h = 20;
		Location loc2 = new Location(loc.getWorld(), 
				loc.getX()+Math.random()*2*8-8, 
				loc.getY()+h, 
				loc.getZ()+Math.random()*2*8-8);
		Vector p1 = new Vector(loc.getX(), loc.getY(), loc.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		Vector vec = p1.clone().subtract(p2).normalize();
		final Location check = loc2.clone();
		new BukkitRunnable() {
			double h = 30;
			@Override
			public void run() {
				if(loc2.distance(check) > h || !casterInCastWorld()) {
					spellEffect1_1(loc2);
					this.cancel();
					return;
				}
				if(loc2.getBlock().getType().isSolid()) {
					spellEffect1_1(loc2.subtract(vec));
					this.cancel();
					return;
				}
				loc2.getWorld().playSound(loc2, Sound.ENTITY_GENERIC_EXPLODE, 0.2f, 1);
				loc2.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc2, 1, 0, 0, 0, 0);
				loc2.add(vec);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void spellEffect1_1(Location loc) {
		List<Entity> shooted = new ArrayList<>();
		final Location check = loc.clone();
		new BukkitRunnable() {
			double obszar = dr.getObszar()*0.75;
			double dmg = dr.getDamage()*0.75;
			double t = 0.5;
			LivingEntity le;
			@Override
			public void run() {
				if(t > obszar || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				double toAdd = Math.PI*2/(t*6);
				for(double theta = 0; theta <= (Math.PI*2); theta += toAdd) {
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x,0.5,z);
					loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.2f, 1);
					p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1, 0, 0, 0, 0);
					
					loc.getWorld().getNearbyEntities(loc, 4, 4, 4).parallelStream().filter(e -> {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-5, loc.getZ()-1, loc.getX()+1, loc.getY()+5, loc.getZ()+1);
						if(!aabb.c(aabb2))
							return false;
						if(e.equals(p) || !(e instanceof LivingEntity))
							return false;
						if(shooted.contains(e))
							return false;
						if(e.getLocation().distance(check) > obszar)
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
					}).forEach(e -> {
						le = (LivingEntity) e;
						RuneDamage.damageNormal(p, le, dr, dmg, (p, le, dr)->{
							new BukkitRunnable() {
								int timer = 20;
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
					
					loc.subtract(x, 0.5, z);
				}
				++t;
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}
	
	//Czarna dziura
	private void effect2() {
		Location loc = p.getLocation().add(0, 0.5, 0);
		new BukkitRunnable() {
			int timer = (int) (10*dr.getDurationTime()*1.5);
			double update = (Math.PI*2)/256;
			double jump = (Math.PI*2)/6;
			double obszar = dr.getObszar() * 0.5;
			double theta = 0;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				for(int i = 1; i <= 6; ++i) {
					effect2DrawSinus(loc.clone(), obszar, theta+jump*i);
				}
				theta += update;
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
		new BukkitRunnable() {
			int timer = (int) (dr.getDurationTime()*1.5);
			double dmg = dr.getDamage()/10;
			double obszar = dr.getObszar() * 0.5;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				
				loc.getWorld().getNearbyEntities(loc, obszar, obszar, obszar).parallelStream().filter(e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(e.getLocation().distance(loc) > obszar)
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
				}).forEach(e -> {
					loc.getWorld().playSound(e.getLocation(), Sound.ENTITY_PHANTOM_HURT, 0.4f, 0.7f);
					RuneDamage.damageNormal(p, (LivingEntity)e, dr, dmg);
				});
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		new BukkitRunnable() {
			int timer = (int) (4*dr.getDurationTime()*1.5);
			double obszar = dr.getObszar() * 1.5;
			@Override
			public void run() {
				if(timer <= 0) {
					this.cancel();
					return;
				}
				--timer;
				
				loc.getWorld().getNearbyEntities(loc, obszar, obszar, obszar).parallelStream().filter(e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(e.getLocation().distance(loc) > obszar)
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
				}).forEach(e -> {
					Location loc2 = e.getLocation();
					Vector p1 = new Vector(loc.getX(), loc.getY(), loc.getZ());
					Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
					Vector vec = p1.clone().subtract(p2).normalize().multiply(2/loc2.distance(loc));
					e.setVelocity(vec);
				});
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}
	
	private void effect2DrawSinus(Location loc, double obszar, double theta) {
		DustOptions dust = new DustOptions(Color.BLACK, 1.7f);
		for(double t = 0; t <= obszar; t+=0.5) {
			double percent = t/obszar * Math.PI;
			double x = t*Math.sin(theta-percent);
			double z = t*Math.cos(theta-percent);
			loc.add(x,0,z);
			p.getWorld().spawnParticle(Particle.REDSTONE, loc, 3, 0.1f, 0.1f, 0.1f, 0, dust);
			loc.subtract(x, 0, z);
		}
	}
	
	//Burza z piorunami
	private void effect3() {
		p.getWorld().playSound(p.getLocation(), Sound.WEATHER_RAIN, 3, 0.85f);
		new BukkitRunnable() {
			int timer = (int) (dr.getDurationTime() * 1.5);
			double dmg = dr.getDamage()/3;
			@Override
			public void run() {
				if(timer < 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				List<Entity> list = p.getNearbyEntities(dr.getObszar(), dr.getObszar(), dr.getObszar());
				
				list.parallelStream().filter(e -> {
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
				}).sorted((e1, e2) -> {
					Random rand = new Random();
					return rand.nextInt(3) - 1;
				}).limit(2).forEach(e -> {
					if(RuneDamage.damageNormal(p, (LivingEntity)e, dr, dmg)) {
						e.getWorld().playSound(e.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 0.9f);
						spellEffect3(e.getLocation());
					}
				});
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		new BukkitRunnable() {
			int timer = (int) (dr.getDurationTime() * 6);
			double obszar = dr.getObszar()*2;
			@Override
			public void run() {
				if(timer < 0) {
					this.cancel();
					return;
				}
				--timer;
				Location loc = p.getLocation().add(0, 20, 0);
				Location l1 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*obszar-obszar, 
						loc.getY(), 
						loc.getZ()+Math.random()*2*obszar-obszar);
				Location l2 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*obszar-obszar, 
						loc.getY(), 
						loc.getZ()+Math.random()*2*obszar-obszar);
				Location l3 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*obszar-obszar, 
						loc.getY(), 
						loc.getZ()+Math.random()*2*obszar-obszar);
				Location l4 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*obszar-obszar, 
						loc.getY(), 
						loc.getZ()+Math.random()*2*obszar-obszar);
				Location l5 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*obszar-obszar, 
						loc.getY(), 
						loc.getZ()+Math.random()*2*obszar-obszar);
				l1.getWorld().spawnParticle(Particle.CLOUD, l1, 15, 2f,0.2f,2f,0.05f);
				l1.getWorld().spawnParticle(Particle.DRIP_WATER, l1, 8, 1f,0.3f,1f,0.05f);
				l1.getWorld().spawnParticle(Particle.CLOUD, l2, 15, 2f,0.2f,2f,0.05f);
				l1.getWorld().spawnParticle(Particle.DRIP_WATER, l2, 8, 1f,0.3f,1f,0.05f);
				l1.getWorld().spawnParticle(Particle.CLOUD, l3, 15, 2f,0.2f,2f,0.05f);
				l1.getWorld().spawnParticle(Particle.DRIP_WATER, l3, 8, 1f,0.3f,1f,0.05f);
				l1.getWorld().spawnParticle(Particle.CLOUD, l4, 15, 2f,0.2f,2f,0.05f);
				l1.getWorld().spawnParticle(Particle.DRIP_WATER, l4, 8, 1f,0.3f,1f,0.05f);
				l1.getWorld().spawnParticle(Particle.CLOUD, l5, 15, 2f,0.2f,2f,0.05f);
				l1.getWorld().spawnParticle(Particle.DRIP_WATER, l5, 8, 1f,0.3f,1f,0.05f);
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}
	
	private void spellEffect3(Location loc) {
		Location loc2 = loc.clone().add(rand.nextInt(20)-10, 20, rand.nextInt(20)-10);
		Vector p1 = new Vector(loc.getX(), loc.getY(), loc.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		Vector vec = p1.clone().subtract(p2).normalize();
		double distance = loc2.distance(loc);
		final Location check = loc2.clone();
		while(loc2.distance(check) < distance) {
			p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc2, 5,0.1F,0.1F,0.1F,0.05F);
			loc2.add(vec);
		}
	}
	
	//Fala plomieni
	private void effect4() {
		List<Entity> shooted = new ArrayList<>();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
		new BukkitRunnable() {
			double t = 0.25;
			Location loc = p.getLocation().add(0,0.75,0);
			Location check = p.getLocation();
			double obszar = dr.getObszar()*2;
			double dmg = dr.getDamage()/2.5;
			LivingEntity le;
			@Override
			public void run() {
				if(t > obszar || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				double toAdd = Math.PI*2/(t*6);
				for(double theta = 0; theta <= (Math.PI*2); theta += toAdd) {

					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x,0,z);
					p.getWorld().spawnParticle(Particle.FLAME, loc, 15, 0.3f, 1.5f, 0.3f, 0.1f);
					
					loc.getWorld().getNearbyEntities(loc, 4, 4, 4).parallelStream().filter(e -> {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-5, loc.getZ()-1, loc.getX()+1, loc.getY()+5, loc.getZ()+1);
						if(!aabb.c(aabb2))
							return false;
						if(e.equals(p) || !(e instanceof LivingEntity))
							return false;
						if(shooted.contains(e))
							return false;
						if(e.getLocation().distance(check) > obszar)
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
					}).forEach(e -> {
						le = (LivingEntity) e;
						RuneDamage.damageNormal(p, le, dr, dmg, (p, le, dr)->{
							new BukkitRunnable() {
								int timer = 25;
								double dmg = dr.getDamage()/30.0;
								@Override
								public void run() {
									if(timer <= 0 || !casterInCastWorld()) {
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
					
					loc.subtract(x, 0, z);
				}
				t += 0.25;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	//Tornada
	private void effect5() {
		List<Entity> shooted = new ArrayList<>();
		new BukkitRunnable() {
			double radius = dr.getObszar() * 0.5;
			double dmg = dr.getDamage() * 0.75;
			int timer = dr.getDurationTime()*2*20;
			double theta = 0;
			double jump = Math.PI;
			double update = (2*Math.PI)/256;
			double alfa = 0;
			double alfaUpdate = (2*Math.PI)/64;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				
				Location base = p.getLocation().add(0, 0.25, 0);
				double x1 = radius * Math.sin(theta);
				double z1 = radius * Math.cos(theta);
				double x2 = radius * Math.sin(theta + jump);
				double z2 = radius * Math.cos(theta + jump);
				Location loc1 = base.clone().add(x1,0,z1);
				Location loc2 = base.clone().add(x2,0,z2);
				loc1.getWorld().playSound(loc1, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.2f, 0.8f);
				loc2.getWorld().playSound(loc2, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.2f, 0.8f);
				effect5Tornado(loc1,alfa);
				effect5Tornado(loc2,alfa);
				
				loc1.getWorld().getNearbyEntities(loc1, 3, 3, 3).parallelStream().filter(e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc1.getX()-1, loc1.getY()-5, loc1.getZ()-1, loc1.getX()+1, loc1.getY()+5, loc1.getZ()+1);
					if(!aabb.c(aabb2))
						return false;
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
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
				}).forEach(e -> {
					if(RuneDamage.damageNormal(p, (LivingEntity) e, dr, dmg)) {
						Location tmp = e.getLocation();
						Vector p1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ());
						Vector p2 = new Vector(tmp.getX(), tmp.getY(), tmp.getZ());
						Vector vec = p1.clone().subtract(p2).normalize().setY(1).multiply(3);
						e.setVelocity(vec);
						loc1.getWorld().playSound(loc1, Sound.ENTITY_ENDER_DRAGON_FLAP, 0.6f, 1.2f);
						shooted.add(e);
						new BukkitRunnable() {
							@Override
							public void run() {
								shooted.remove(e);
							}
						}.runTaskLater(Main.getInstance(), 20);
					}
				});
				
				loc2.getWorld().getNearbyEntities(loc2, 3, 3, 3).parallelStream().filter(e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc2.getX()-1, loc2.getY()-5, loc2.getZ()-1, loc2.getX()+1, loc2.getY()+5, loc2.getZ()+1);
					if(!aabb.c(aabb2))
						return false;
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
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
				}).forEach(e -> {
					if(RuneDamage.damageNormal(p, (LivingEntity) e, dr, dmg)) {
						Location tmp = e.getLocation();
						Vector p1 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
						Vector p2 = new Vector(tmp.getX(), tmp.getY(), tmp.getZ());
						Vector vec = p1.clone().subtract(p2).normalize().setY(0.3).multiply(3);
						e.setVelocity(vec);
						loc2.getWorld().playSound(loc2, Sound.ENTITY_ENDER_DRAGON_FLAP, 0.4f, 1.2f);
						shooted.add(e);
						new BukkitRunnable() {
							@Override
							public void run() {
								shooted.remove(e);
							}
						}.runTaskLater(Main.getInstance(), 20);
					}
				});
				
				theta -= update;
				alfa += alfaUpdate;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void effect5Tornado(Location loc, double alfa) {
		for(double y = 0; y <= 3; y+=0.5) {
			double radius = y+0.25;
			double toAdd = (Math.PI*2)/(radius*2);
			for(double theta = 0; theta <= (Math.PI*2); theta += toAdd) {
				double x = radius * Math.sin(theta+alfa);
				double z = radius * Math.cos(theta+alfa);
				loc.add(x, y, z);
				loc.getWorld().spawnParticle(Particle.CLOUD, loc, 1, 0, 0, 0, 0);
				loc.subtract(x, y, z);
			}
		}
	}
	
	//Erupcja
	private void effect6() {
		Location base = p.getLocation().add(0,0.15,0);
		double obszar = dr.getObszar()/2;
		for(int i = 0; i < 5; ++i) {
			double x = Math.random()*2*obszar-obszar;
			double z = Math.random()*2*obszar-obszar;
			Location tmp = base.clone().add(x,0,z);
			effect6Eruption(tmp);
		}
	}
	
	private void effect6Eruption(Location loc) {
		while(loc.getBlock().getType().isSolid())
			loc.add(0, 1, 0);
		new BukkitRunnable() {
			int timer = (int)(dr.getDurationTime()*5);
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				loc.getWorld().spawnParticle(Particle.LAVA, loc, 5, 0.1f, 0.1f, 0.1f, 0);
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		new BukkitRunnable() {
//			double dmg = dr.getDamage()*0.6;
			int timer = (int)(dr.getDurationTime()*1.25);
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				Vector vec = new Vector(Math.random(),Math.random(),Math.random()).normalize().multiply(2*Math.random());
				double gravity = 0.01 + Math.random()/2;
				loc.getWorld().playSound(loc, Sound.BLOCK_LAVA_POP, 1.5f, 0.8f);
				effect6EruptionBall(loc.clone(), vec, gravity);
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
	
	private void effect6EruptionBall(Location loc, Vector vec, double gravity) {
		new BukkitRunnable() {
			double dmg = dr.getDamage()*0.6;
			LivingEntity le;
			@Override
			public void run() {
				
				loc.getWorld().getNearbyEntities(loc, 2, 2, 2, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1, loc.getZ()-1, loc.getX()+1, loc.getY()+1, loc.getZ()+1);
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
					if(RuneDamage.damageNormal(p, le, dr, dmg, (p, le, dmg)->{
						new BukkitRunnable() {
							int timer = 10;
							double dmg = dr.getDamage()/50.0;
							@Override
							public void run() {
								if(timer <= 0) {
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
					})) {
						loc.getWorld().playSound(loc, Sound.BLOCK_LAVA_EXTINGUISH, 0.7f, 0.8f);
						this.cancel();
						return;
					}
				});
				
				if(this.isCancelled()) {
					return;
				}
				
				if(loc.getBlock().getType().isSolid() || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				loc.getWorld().spawnParticle(Particle.LAVA, loc, 3, 0.1f, 0.1f, 0.1f, 0);
				loc.add(vec);
				vec.setY(vec.getY()-gravity);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
