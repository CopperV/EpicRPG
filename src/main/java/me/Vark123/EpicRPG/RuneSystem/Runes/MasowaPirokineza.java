package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

public class MasowaPirokineza extends ARune {

	private LivingEntity le;

	public MasowaPirokineza(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, .8f);
		PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 20 * dr.getDurationTime(), 2);
		PotionEffect stun = new PotionEffect(PotionEffectType.SLOW, 20 * dr.getDurationTime(), 7);
		p.addPotionEffect(slow);
		Location loc = p.getLocation();
		for (Entity entity : loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar())) {
			if (!entity.equals(p) && entity instanceof LivingEntity
					&& entity.getLocation().distance(loc) <= dr.getObszar()) {
				if (entity instanceof Player || entity.hasMetadata("NPC")) {
					RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
					ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
					if (set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY)) {
						continue;
					}
				}
				le = (LivingEntity) entity;
				if (RuneDamage.damageNormal(p, le, dr, (p, le, dr) -> {
					new BukkitRunnable() {
						@Override
						public void run() {
							new BukkitRunnable() {
								int t = 1;

								@Override
								public void run() {
									if (t >= dr.getDurationTime() || !casterInCastWorld()) {
										this.cancel();
										return;
									}
									p.getWorld().spawnParticle(Particle.FLAME, entity.getLocation().add(0, 1, 0), 10,
											0.3F, 0.3F, 0.3F, 0);
									if (!RuneDamage.damageTiming(p, le, dr)) {
										this.cancel();
										return;
									}
									;
									t++;
								}
							}.runTaskTimer(Main.getInstance(), 0, 20);
						}
					}.runTaskLater(Main.getInstance(), 20);
				})) {
//					Bukkit.broadcastMessage(entity.getName() + " damage");
					le.addPotionEffect(stun);
					entity.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
					createLine(loc.clone().add(0, 1, 0), entity.getLocation().add(0, 1, 0));
				} else {
//					Bukkit.broadcastMessage(entity.getName() + " none damage");
				}
			}
		}
//		new BukkitRunnable() {
//			Location loc = p.getLocation();
//			Location check = p.getLocation();
//			double t = 0;
//			PotionEffect stun = new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 7);
//			LivingEntity le;
//			@Override
//			public void run() {
//				t+=0.65;
//				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
//					double x = t*Math.sin(theta);
//					double z = t*Math.cos(theta);
//					loc.add(x, 0.25, z);
//					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
//						p.addPotionEffect(slow);
//						this.cancel();
//						return;
//					}
//					p.getWorld().spawnParticle(Particle.FLAME, loc, 2,0.2F,0.2F,0.2F,0.05F);
//					loc.add(0,0.25,0);
//					p.getWorld().spawnParticle(Particle.FLAME, loc, 2,0.2F,0.2F,0.2F,0.05F);
//					loc.add(0,0.25,0);
//					p.getWorld().spawnParticle(Particle.FLAME, loc, 2,0.2F,0.2F,0.2F,0.05F);
//					loc.add(0,0.25,0);
//					p.getWorld().spawnParticle(Particle.FLAME, loc, 2,0.2F,0.2F,0.2F,0.05F);
//					loc.add(0,0.25,0);
//					p.getWorld().spawnParticle(Particle.FLAME, loc, 2,0.2F,0.2F,0.2F,0.05F);
//					loc.add(0,0.25,0);
//					p.getWorld().spawnParticle(Particle.FLAME, loc, 2,0.2F,0.2F,0.2F,0.05F);
//					
//					for(Entity entity:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
//						AxisAlignedBB aabb = ((CraftEntity)entity).getHandle().cw();
//						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.75, loc.getY()-0.75, loc.getZ()-0.75, loc.getX()+0.75, loc.getY()+4, loc.getZ()+0.75);
//						if(aabb.c(aabb2)) {
//							if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(check) <= dr.getObszar()) {
//								if(entity instanceof Player || entity.hasMetadata("NPC")) {
//									RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
//									ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
//									if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon")) {
//										continue;
//									}
//								}
//								le = (LivingEntity) entity;
//								if(RuneDamage.damageNormal(p, le, dr, (p, le, dr)->{
//									new BukkitRunnable() {
//										@Override
//										public void run() {
//											new BukkitRunnable() {
//												int t = 1;
//												@Override
//												public void run() {
//													if(t>=dr.getDurationTime() || !casterInCastWorld()) {
//														this.cancel();
//														return;
//													}
//													p.getWorld().spawnParticle(Particle.FLAME, entity.getLocation().add(0, 1, 0),10,0.3F,0.3F,0.3F,0);
//													if(!RuneDamage.damageTiming(p, le, dr)) {
//														this.cancel();
//														return;
//													};
//													t++;
//												}
//											}.runTaskTimer(Main.getInstance(), 0, 20);
//										}
//									}.runTaskLater(Main.getInstance(), 20);
//								})) {
//									le.addPotionEffect(stun);
//									entity.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
//								}
//							}
//						}
//					}
//					loc.subtract(x,1.5,z);
//				}
//			}
//		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

	private void createLine(Location loc1, Location loc2) {
		double space = 0.25;
		Vector p1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		double distance = loc1.distance(loc2);
		Vector vec = p2.clone().subtract(p1).normalize().multiply(space);
		for (double length = 0; length < distance; p1.add(vec), length += space) {
			loc1.getWorld().spawnParticle(Particle.FLAME, p1.getX(), p1.getY(), p1.getZ(), 3, 0.1F, 0.1F, 0.1F, 0.01F);
		}
	}

}
