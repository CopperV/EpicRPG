package me.Vark123.EpicRPG.OldRuneSystem.Runes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
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
import me.Vark123.EpicRPG.OldFightSystem.RuneDamage;
import me.Vark123.EpicRPG.OldRuneSystem.ARune;
import me.Vark123.EpicRPG.OldRuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class TajemnyGrad_H extends ARune {
	
	private static Random rand = new Random();

	public TajemnyGrad_H(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.7f, 1.2f);
		
//		Block b = p.getTargetBlockExact(dr.getObszar()*2, FluidCollisionMode.NEVER);
		Block b = p.getTargetBlock(new HashSet<>(Arrays.asList(Material.AIR, Material.WATER, Material.LAVA)), dr.getObszar()*2);
		Location hit = b.getLocation().clone().add(0, 0.1, 0);
		Location origin = p.getLocation().clone().add(0, 20+hit.distance(p.getLocation()), 0);
		Vector vec = new Vector(
				hit.getX() - origin.getX(),
				hit.getY() - origin.getY(),
				hit.getZ() - origin.getZ())
				.normalize()
				.multiply(1.2);
		
		new BukkitRunnable() {
			int timer = 4*4;
			double radius = dr.getObszar();
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				
				for(int i = 0; i < 7; ++i) {
					double r = rand.nextDouble(radius);
					double angle = rand.nextDouble(Math.PI*2);
					double x = Math.sin(angle) * r;
					double z = Math.cos(angle) * r;
					
					castProjectile(vec, origin.clone().add(x, 0, z));
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}
	
	private void castProjectile(Vector vec, Location src) {
		p.playSound(src, Sound.BLOCK_AMETHYST_BLOCK_FALL, 2.2f, 0.4f);
		new BukkitRunnable() {
			Location loc = src.clone();
			Vector tmpVec = vec.clone().multiply(-0.2);
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!casterInCastWorld()) {
					cancel();
					return;
				}

				Location tmpLoc = loc.clone();
				for(int i = 0; i < 6; ++i) {
					p.getWorld().spawnParticle(Particle.WITCH, tmpLoc, 8, .15, .15, .15, .02);
					tmpLoc.add(tmpVec);
				}
				
				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cK();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.4, loc.getY()-0.4, loc.getZ()-0.4, loc.getX()+0.4, loc.getY()+0.4, loc.getZ()+0.4);
					if(!aabb.c(aabb2))
						return false;
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(e instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !(e.getWorld().getName().toLowerCase().contains("dungeon") || e.getWorld().getName().toLowerCase().contains("raid")))
							return true;
						return false;
					}
					if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
							&& e.getType().equals(EntityType.ARMOR_STAND))
						return false;
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).stream().min((e1, e2) -> {
					double dist1 = e1.getLocation().distanceSquared(loc);
					double dist2 = e2.getLocation().distanceSquared(loc);
					if(dist1 == dist2)
						return 0;
					return dist1 < dist2 ? -1 : 1;
				}).ifPresent(e -> {
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					p.getWorld().spawnParticle(Particle.REVERSE_PORTAL, e.getLocation().add(0,1,0), 14, 0.4f, 0.4f, 0.4f, 1.5f);
					p.getWorld().playSound(e.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, .9f, 1);
					cancel();
				});
				
				if(loc.distanceSquared(src) > (20+4*dr.getObszar())*(20+4*dr.getObszar()) || loc.getBlock().getType().isSolid()) {
					cancel();
					return;
				}
				loc.add(vec);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
