package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
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
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class MalaBurzaOgnista extends ARune {

	private List<Entity> shooted;
	private LivingEntity le;

	public MalaBurzaOgnista(ItemStackRune dr, Player p) {
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
				p.getWorld().spawnParticle(Particle.FLAME, loc,35,0.7f,0.7f,0.7f,0.1f);
				
				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1.4, loc.getY()-1.4, loc.getZ()-1.4, loc.getX()+1.4, loc.getY()+1.4, loc.getZ()+1.4);
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
					le = (LivingEntity)e;
					RuneDamage.damageNormal(p, le, dr, (p, le, dr)->{
						new BukkitRunnable() {
							int timer = 5;
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
					shooted.add(e);
					rangeSpell(e.getLocation());
					this.cancel();
				});
				
				if(this.isCancelled()) {
					return;
				}
				
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					rangeSpell(loc);
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>50 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

	private void rangeSpell(Location loc) {
		Location tmp = loc.clone().add(0, 1, 0);
		for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
			double x = 0.5 * Math.sin(theta);
			double z = 0.5 * Math.cos(theta);
			p.getWorld().spawnParticle(Particle.FLAME, tmp, 0, x, 0, z, 0.35);
		}
		
		loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar()).parallelStream().filter(e -> {
			if(e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if(shooted.contains(e))
				return false;
			if(e.getLocation().distance(loc) > dr.getObszar())
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
			le = (LivingEntity)e;
			RuneDamage.damageNormal(p, le, dr,(p, le, dr) -> {
				new BukkitRunnable() {
					int timer = 5;
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
			shooted.add(e);
		});
	}
	
}
