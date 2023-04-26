package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

public class DuzaBurzaOgnista extends ARune {

	private List<Entity> shooted;
	private LivingEntity le;

	public DuzaBurzaOgnista(ItemStackRune dr, Player p) {
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
			@Override
			public void run() {
				t++;
				double x = vec.getX()*t;
				double y = vec.getY()*t+1.5;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc,10,0,0,0,0);
				p.getWorld().spawnParticle(Particle.FLAME, loc,35,0.7f,0.7f,0.7f,0.1f);
				for(Entity e:loc.getWorld().getNearbyEntities(loc, 3, 3, 3)) {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1.4, loc.getY()-1.4, loc.getZ()-1.4, loc.getX()+1.4, loc.getY()+1.4, loc.getZ()+1.4);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
							le = (LivingEntity)e;
							RuneDamage.damageNormal(p, le, dr, (p, le, dr)->{
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
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					rangeSpell(loc);
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>100 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

	private void rangeSpell(Location loc) {
		Location tmp = loc.clone().add(0, 1, 0);
		Random rand = new Random();
		for(int i = 0; i < 100; ++i) {
			double x = (rand.nextDouble()*4-2) * Math.sin(rand.nextDouble()*Math.PI*2);
			double y = (rand.nextDouble()*4-2) * Math.sin(rand.nextDouble()*Math.PI*2);
			double z = (rand.nextDouble()*4-2) * Math.sin(rand.nextDouble()*Math.PI*2);
			p.getWorld().spawnParticle(Particle.FLAME, tmp, 0, x, y, z, 0.35);
		}
		for(Entity entity : loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar())) {
			if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(loc) <= dr.getObszar()) {
				if(entity instanceof Player || entity.hasMetadata("NPC")) {
					RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
					ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
					if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
						continue;
				}
				le = (LivingEntity)entity;
				RuneDamage.damageNormal(p, le, dr,(p, le, dr) -> {
					new BukkitRunnable() {
						int timer = 14;
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
				shooted.add(entity);
			}
		}
	}

}
