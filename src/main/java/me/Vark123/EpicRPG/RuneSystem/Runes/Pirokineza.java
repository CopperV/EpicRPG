package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
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
import net.minecraft.world.phys.AxisAlignedBB;

public class Pirokineza extends ARune {

	public Pirokineza(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
		new BukkitRunnable() {
			double t = 1;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			LivingEntity le;
			@Override
			public void run() {
				t++;
				double x = vec.getX()*t;
				double y = vec.getY()*t+1.5;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.FLAME, loc, 10,0.3F,0.3F,0.3F,0);
				p.getWorld().spawnParticle(Particle.FLAME, loc, 10,0.1F,0.1F,0.1F,0);
				
				for(Entity e:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1, loc.getZ()-1, loc.getX()+1, loc.getY()+1, loc.getZ()+1);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
							le = (LivingEntity) e;
							if(RuneDamage.damageNormal(p, le, dr, (p, le, dr)->{
								le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 10));
								new BukkitRunnable() {
									@Override
									public void run() {
										new BukkitRunnable() {
											int t = 1;
											@Override
											public void run() {
												if(t>=dr.getDurationTime() || !casterInCastWorld()) {
													this.cancel();
													return;
												}
												p.getWorld().spawnParticle(Particle.FLAME, e.getLocation().add(0, 1, 0),10,0.3F,0.3F,0.3F,0);
												if(!RuneDamage.damageTiming(p, le, dr)) {
													this.cancel();
													return;
												};
												t++;
											}
										}.runTaskTimer(Main.getInstance(), 0, 20);
									}
								}.runTaskLater(Main.getInstance(), 20);
							})) {
								e.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
							}
							this.cancel();
							return;
						}
					}
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
//				if(!loc.getBlock().getType().equals(Material.AIR)) {
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>30 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				t++;
				x = vec.getX()*t;
				y = vec.getY()*t+1.5;
				z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.FLAME, loc, 10,0.3F,0.3F,0.3F,0);
				p.getWorld().spawnParticle(Particle.FLAME, loc, 10,0.1F,0.1F,0.1F,0);
				for(Entity e:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1, loc.getZ()-1, loc.getX()+1, loc.getY()+1, loc.getZ()+1);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
							le = (LivingEntity) e;
							if(RuneDamage.damageNormal(p, le, dr, (p, le, dr)->{
								le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 10));
								new BukkitRunnable() {
									@Override
									public void run() {
										new BukkitRunnable() {
											int t = 1;
											@Override
											public void run() {
												if(t>=dr.getDurationTime() || !casterInCastWorld()) {
													this.cancel();
													return;
												}
												p.getWorld().spawnParticle(Particle.FLAME, e.getLocation().add(0, 1, 0),10,0.3F,0.3F,0.3F,0);
												if(!RuneDamage.damageTiming(p, le, dr)) {
													this.cancel();
													return;
												};
												t++;
											}
										}.runTaskTimer(Main.getInstance(), 0, 20);
									}
								}.runTaskLater(Main.getInstance(), 20);
							})) {
								e.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
							}
							this.cancel();
							return;
						}
					}
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
//				if(!loc.getBlock().getType().equals(Material.AIR)) {
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>30 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
