package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class Zmrok extends ARune {

	public Zmrok(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		double radius = dr.getObszar();
		List<Entity> startTargets = p.getWorld()
				.getNearbyEntities(p.getLocation(), radius, radius, radius)
				.stream()
				.filter(e -> {
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
				})
				.collect(Collectors.toList());
		
		List<Entity> targets;
		if(startTargets.size() > 5) {
			Random rand = new Random();
			targets = new LinkedList<>();
			while(targets.size() < 5) {
				Entity e = startTargets.get(rand.nextInt(startTargets.size()));
				if(targets.contains(e))
					continue;
				targets.add(e);
			}
		} else
			targets = startTargets;
		
		new BukkitRunnable() {
			int counter = 0;
			int timer = 1;
			@Override
			public void run() {
				if(counter >= targets.size() || !casterInCastWorld()) {
					cancel();
					return;
				}
				if(isCancelled())
					return;
				
				Location loc = p.getLocation();
				float angle = loc.getYaw()/60;
				Location castLoc = loc.clone();
				castLoc.add(0, 2.5, 0);
				castLoc.subtract(new Vector(Math.cos(angle), 0, Math.sin(angle))
						.normalize()
						.multiply(0.5));
				p.getWorld().spawnParticle(Particle.SMOKE_LARGE, castLoc, 4, 0.2f, 0.2f, 0.2f, 0.02f);
				
				if(timer % 20 == 0) {
					Entity e = targets.get(counter);
					spellEffect(castLoc, e);
					
					++counter;
				}
				++timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void spellEffect(Location loc, Entity target) {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.5f, 1.4f);
		new BukkitRunnable() {
			@Override
			public void run() {
				if(target == null || target.isDead()) {
					cancel();
					return;
				}
				if(!casterInCastWorld()) {
					cancel();
					return;
				}
				if(loc.getBlock().getType().isSolid() 
						&& !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				if(isCancelled())
					return;
				
				Location tmp = target.getLocation().add(0,1,0);
				Vector vec = new Vector(tmp.getX() - loc.getX(),
						tmp.getY() - loc.getY(),
						tmp.getZ() - loc.getZ()).normalize().multiply(0.5);
				loc.add(vec);
				p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 6, 0.15f, 0.15f, 0.15f, 0.01f);

				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.8, loc.getY()-1.5, loc.getZ()-0.8, loc.getX()+0.8, loc.getY()+1.5, loc.getZ()+0.8);
					if(!aabb.c(aabb2))
						return false;
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(e instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
							return false;
					}
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
					cancel();
					if(!RuneDamage.damageNormal(p, (LivingEntity) e, dr))
						return;
					p.getWorld().playSound(e.getLocation(), Sound.ENTITY_STRAY_DEATH, 1.5f, 0.6f);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getLocation().add(0,1,0), 15, 0.3f, 0.5f, 0.3f, 0.1f);
				});
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
