package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
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

public class PorazenieElektryczne extends ARune {

	List<Entity> entitiesList = new ArrayList<>();

	public PorazenieElektryczne(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_STEP, 1, 0.5F);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			@Override
			public void run() {
				t+=0.5;
				double x = vec.getX()*t;
				double y = vec.getY()*t+1.5;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 10,0.1F,0.1F,0.1F,0.01F);
				
				for(Entity e:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1, loc.getZ()-1, loc.getX()+1, loc.getY()+1, loc.getZ()+1);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								Location loc = e.getLocation();
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
							if(RuneDamage.damageNormal(p, (LivingEntity)e, dr)) {
								entitiesList.add(e);
								spellEffect(e);
								p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 10,1F,1F,1F,0.2F);
								e.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
								this.cancel();
								return;
							}
						}
					}
				}
				
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
//					if(!loc.getBlock().getType().equals(Material.AIR)) {
						this.cancel();
						return;
				}
				loc.subtract(x, y, z);
				if(t>30 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				t+=0.5;
				x = vec.getX()*t;
				y = vec.getY()*t+1.5;
				z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 10,0.1F,0.1F,0.1F,0.01F);
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
							if(RuneDamage.damageNormal(p, (LivingEntity)e, dr)) {
								entitiesList.add(e);
								spellEffect(e);
								p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 10,1F,1F,1F,0.2F);
								e.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
								this.cancel();
								return;
							}
						}
					}
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
//					if(!loc.getBlock().getType().equals(Material.AIR)) {
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

	private void spellEffect(Entity entity) {
		new BukkitRunnable() {
			Entity tmp = entity;
			Location loc1;
			double t = 1;
			@Override
			public void run() {
				if(t>=4 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				loc1 = tmp.getLocation();
				Collection<Entity> lista = loc1.getWorld().getNearbyEntities(loc1, 5, 5, 5);
				Location loc2 = null;
				
				double distance = 10;
				Entity e = null;
				for(Entity en : lista) {
					if(entitiesList.contains(en)) continue;
					if(en.equals(p)) continue;
					if(!(en instanceof LivingEntity)) continue;
					if(en instanceof Player || en.hasMetadata("NPC")) {
						Location loc = en.getLocation();
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(en.getLocation()));
						if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
							continue;
					}
					if(Math.abs(en.getLocation().distance(loc1)) >= distance) continue;
					
					loc2 = en.getLocation();
					distance = loc2.distance(loc1);
					e = en;
				}
				
				if(loc2 == null) return;
				if(RuneDamage.damageNormal(p, (LivingEntity)e, dr)) {
					entitiesList.add(e);
					drawLine(loc1.clone().add(0,1,0), loc2.clone().add(0,1,0));
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc2, 10,1F,1F,1F,0.2F);
					tmp = e;
					loc1 = loc2;
				}else {
					--t;
				}
					
			}
		}.runTaskTimer(Main.getInstance(),0 , 5);
	}
	
	private void drawLine(Location loc1, Location loc2) {
		double space = 0.25;
		Vector p1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		double distance = loc1.distance(loc2);
		Vector vec = p2.clone().subtract(p1).normalize().multiply(space);
		for(double length = 0; length < distance; p1.add(vec), length += space) {
			loc1.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p1.getX(), p1.getY(), p1.getZ() , 10,0.1F,0.1F,0.1F,0.01F);
		}
	}
	
}
