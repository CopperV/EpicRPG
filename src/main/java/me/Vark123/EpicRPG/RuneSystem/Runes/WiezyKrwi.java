package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
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
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.Utils.Utils;
import net.minecraft.world.phys.AxisAlignedBB;

public class WiezyKrwi extends ARune {
	
	private static final double MAX_ANGLE = 30;
	private static final double MAX_ANGLE_SQUARED = MAX_ANGLE*MAX_ANGLE;
	private static final DustOptions dust = new DustOptions(Color.fromRGB(138, 3, 3), 0.8f);

	private Entity target = null;
	List<Entity> entitiesList = new LinkedList<>();
	
	public WiezyKrwi(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1f, 0.8f);
		
		p.getWorld().getNearbyEntities(p.getLocation(), MAX_ANGLE, MAX_ANGLE, MAX_ANGLE, e -> {
			if(e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if(e instanceof Player) {
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(e.getLocation()));
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
			if(Utils.getAngle(p, e) > MAX_ANGLE)
				return false;
			return true;
		}).stream().min((e1, e2) -> {
			double comp1 = (e1.getLocation().distance(p.getLocation()) / MAX_ANGLE
					- e2.getLocation().distance(p.getLocation()) / MAX_ANGLE) * 0.3;
			double comp2 = (Utils.getAngle(p, e1) / MAX_ANGLE
					- Utils.getAngle(p, e2)) * 0.7;
			double result = comp1+comp2;
			if(result < 0)
				return -1;
			else if(result > 0)
				return 1;
			return 0;
		}).ifPresent(e -> target = e);
		
		if(target != null) {
			double modifier = 1 - (0.08 * entitiesList.size());
			drawLine(p.getLocation().clone().add(0,1,0), target.getLocation().add(0, 1, 0));
			entitiesList.add(target);
			castEffect(target);
			p.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1f, 1.2f);
			RuneDamage.damageNormal(p, (LivingEntity) target, dr, dr.getDamage()*modifier);
		} else {
			new BukkitRunnable() {
				Location loc = p.getLocation().add(0, 1.25, 0);
				Location origin = loc.clone();
				Vector vec = loc.getDirection().normalize();
				@Override
				public void run() {
					if(loc.distanceSquared(origin) > MAX_ANGLE_SQUARED || !casterInCastWorld()) {
						this.cancel();
						return;
					}
					
					for(int i = 0; i < 2; ++i) {
						loc.add(vec);
						p.getWorld().spawnParticle(Particle.REDSTONE, loc, 5, 0.06f, 0.06f, 0.06f, 0.01f, dust);

						loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
							AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
							AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.55, loc.getY()-0.55, loc.getZ()-0.55, loc.getX()+0.55, loc.getY()+0.55, loc.getZ()+0.55);
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
							double modifier = 1 - (0.08 * entitiesList.size());
							entitiesList.add(e);
							castEffect(e);
							p.getWorld().playSound(e.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1f, 1.2f);
							RuneDamage.damageNormal(p, (LivingEntity) e, dr, dr.getDamage()*modifier);
							this.cancel();
						});
						
						if(this.isCancelled()) {
							return;
						}
						
						if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
							this.cancel();
							return;
						}
					}
				}
			}.runTaskTimer(Main.getInstance(), 0, 1);
		}
	}
	
	private void castEffect(Entity source) {
		if(entitiesList.size() >= 5)
			return;
		
		source.getWorld().getNearbyEntities(source.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar(), e -> {
			if(e.getLocation().distanceSquared(source.getLocation()) > dr.getObszar()*dr.getObszar())
				return false;
			if(entitiesList.contains(e))
				return false;
			if(e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if(e instanceof Player) {
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(e.getLocation()));
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
		}).stream().min((e1, e2) -> Double.compare(
				e1.getLocation().distanceSquared(source.getLocation()),
				e2.getLocation().distanceSquared(source.getLocation())))
		.ifPresent(e -> {
			double modifier = 1 - (0.08 * entitiesList.size());
			drawLine(source.getLocation().clone().add(0,1,0), e.getLocation().add(0, 1, 0));
			entitiesList.add(e);
			castEffect(e);
			p.getWorld().playSound(e.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1f, 1.2f);
			RuneDamage.damageNormal(p, (LivingEntity) e, dr, dr.getDamage()*modifier);
		});
	}
	
	private void drawLine(Location loc1, Location loc2) {
		double space = 0.25;
		Vector p1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		double distance = loc1.distance(loc2);
		Vector vec = p2.clone().subtract(p1).normalize().multiply(space);
		for(double length = 0; length < distance; p1.add(vec), length += space) {
			loc1.getWorld().spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ() , 10,0.1F,0.1F,0.1F,0.01F, dust);
		}
	}

}
