package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.Sound;
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
import net.minecraft.world.phys.AxisAlignedBB;

public class SzeptNZotha extends ARune {
	
	private static final Random rand = new Random();
	private static final DustOptions prepareDust = new DustOptions(Color.fromRGB(48, 25, 52), 1.2f);
	private static final DustOptions projectileDust = new DustOptions(Color.fromRGB(48, 25, 52), 0.25f);

	public SzeptNZotha(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation().clone().add(0,1,0);
		loc.getWorld().playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 2.5f, 0.6f);
		
		new BukkitRunnable() {
			int timer = 3*4;
			double radius = dr.getObszar();
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!casterInCastWorld()) {
					this.cancel();
					return;
				}
				if(timer <= 0) {
					spellEffect(loc);
					this.cancel();
					return;
				}
				--timer;
				
				loc.getWorld().getNearbyEntities(loc, radius, radius, radius, e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(e.getLocation().distanceSquared(loc) > radius*radius)
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
				}).forEach(e -> {
					Location eLoc = e.getLocation();
					Vector vec = new Vector(
							loc.getX() - eLoc.getX(),
							loc.getY() - eLoc.getY(),
							loc.getZ() - eLoc.getZ())
							.normalize()
							.multiply(0.1);
					e.setVelocity(vec);
				});
				
				loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 5, .3, .3, .3, .15, prepareDust);
				loc.getWorld().spawnParticle(Particle.PORTAL, loc, 15, .7, .7, .7, .5);
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}
	
	public void spellEffect(Location loc) {
		loc.getWorld().playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_HURT, 2.5f, 1.2f);
		for(int i = 0; i < 20; ++i) {
			double x = rand.nextDouble(2) - 1;
			double y = rand.nextDouble(.4) - .2;
			double z = rand.nextDouble(2) - 1;
			Vector dir = new Vector(x,y,z).normalize().multiply(1.2);
			castProjectile(loc, dir);
		}
	}
	
	public void castProjectile(Location src, Vector dir) {
		new BukkitRunnable() {
			Location loc = src.clone();
			Vector tmpVec = dir.clone().multiply(-0.2);
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!casterInCastWorld()) {
					cancel();
					return;
				}
				
				Location tmpLoc = loc.clone();
				for(int i = 0; i < 4; ++i) {
					p.getWorld().spawnParticle(Particle.REDSTONE, tmpLoc, 3, .15, .15, .15, .03, projectileDust);
					tmpLoc.add(tmpVec);
				}
				
				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.3, loc.getY()-0.3, loc.getZ()-0.3, loc.getX()+0.3, loc.getY()+0.3, loc.getZ()+0.3);
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
					p.getWorld().spawnParticle(Particle.REVERSE_PORTAL, e.getLocation().add(0,1,0), 6, 0.3f, 0.3f, 0.3f, 1.5f);
					p.getWorld().playSound(e.getLocation(), Sound.ENTITY_GUARDIAN_DEATH, .9f, 1.2f);
					cancel();
				});
				
				if(loc.distanceSquared(src) > dr.getObszar()*dr.getObszar() || loc.getBlock().getType().isSolid()) {
					cancel();
					return;
				}
				loc.add(dir);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
