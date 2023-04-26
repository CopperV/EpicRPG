package me.Vark123.EpicRPG.RuneSystem.Runes;

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

public class KulaSmierci extends ARune {

	public KulaSmierci(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SKELETON_HORSE_HURT, 1, 0.2f);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			public void run() {
				t++;
				double x = vec.getX()*t;
				double y = vec.getY()*t+1.5;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0, 0, 0, 0.05F);
				p.getWorld().spawnParticle(Particle.LAVA, loc, 15,0.5F,0.5F,0.5F,0.1F);
				
				for(Entity e:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1.25, loc.getY()-1.25, loc.getZ()-1.25, loc.getX()+1.25, loc.getY()+1.25, loc.getZ()+1.25);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}	
							RuneDamage.damageNormal(p, (LivingEntity)e, dr);
							p.getWorld().spawnParticle(Particle.LAVA, loc, 10, 1F,1F,1F,0.4F);
							p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0, 1F, 0, 0.05F);
							loc.getWorld().playSound(loc, Sound.ENTITY_SKELETON_DEATH, 1, 0.2F);
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
				if(t>40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				t++;
				x = vec.getX()*t;
				y = vec.getY()*t+1.5;
				z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0, 0, 0, 0.05F);
				p.getWorld().spawnParticle(Particle.LAVA, loc, 15,0.5F,0.5F,0.5F,0.1F);
//				loc.getWorld().playSound(loc, Sound.FIRE_IGNITE, 1, 1);
				for(Entity e:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1.25, loc.getY()-1.25, loc.getZ()-1.25, loc.getX()+1.25, loc.getY()+1.25, loc.getZ()+1.25);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}	
							RuneDamage.damageNormal(p, (LivingEntity)e, dr);
							p.getWorld().spawnParticle(Particle.LAVA, loc, 10, 1F,1F,1F,0.4F);
							p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0, 1F, 0, 0.05F);
							loc.getWorld().playSound(loc, Sound.ENTITY_SKELETON_DEATH, 1, 0.2F);
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
				if(t>40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
