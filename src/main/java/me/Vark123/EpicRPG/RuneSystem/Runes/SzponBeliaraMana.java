package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
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
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class SzponBeliaraMana extends ARune {

	public SzponBeliaraMana(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		int points = rpg.getStats().getKrag();
		List<Vector> directions = new ArrayList<>();
		Location loc = p.getLocation().clone();
		directions.add(loc.getDirection().normalize());
		
		double pitch = loc.getPitch();
		double pitchMod = pitch;
		double pitchChange = pitch/points*4*(-1);
		
		double yaw = loc.getYaw();
		double yawChange = 360./points;
		
		for(int i = 1; i < points; ++i) {
			yaw += yawChange;
			if(yaw > 180) {
				yaw -= 360;
			}
			
			double tmpPitch = pitchMod - pitchChange;
			if(Math.abs(tmpPitch) > Math.abs(pitch)) {
				tmpPitch = 2*pitch - tmpPitch;
				pitchChange *= -1;
				pitch *= -1;
			}
			pitchMod = tmpPitch;
			
			Location tmp = loc.clone();
			tmp.setPitch((float) pitchMod);
			tmp.setYaw((float) yaw);
			directions.add(tmp.getDirection().normalize());
			
		}
		
		directions.parallelStream().forEach(vec -> {
			spellEffect(vec, loc);
		});
	}
	
	private void spellEffect(Vector vec, Location from) {
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1, 0.1f);
		new BukkitRunnable() {
			double t = 0;
			Location loc = from.clone();
			Location loc2 = loc.clone();
			List<Entity> shooted = new ArrayList<>();
			DustOptions dust = new DustOptions(Color.RED, 1.5f);
			@Override
			public void run() {
				if(t >= 20 || !casterInCastWorld()) {
					spellEffectReverse(vec, loc2);
					this.cancel();
					return;
				}
				
				t+=0.5;
				double x = vec.getX()*t;
				double y = vec.getY()*t + 1.5;
				double z = vec.getZ()*t;
				loc2.setX(loc.getX()+x);
				loc2.setY(loc.getY()+y);
				loc2.setZ(loc.getZ()+z);
				
				p.getWorld().spawnParticle(Particle.REDSTONE, loc2, 3, 0.15f, 0.15f, 0.15f, 0.05f, dust);

				loc.getWorld().getNearbyEntities(loc2, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc2.getX()-0.6, loc2.getY()-0.6, loc2.getZ()-0.6, loc2.getX()+0.6, loc2.getY()+0.6, loc2.getZ()+0.6);
					if(!aabb.c(aabb2))
						return false;
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
						return false;
					if(e instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
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
					shooted.add(e);
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					p.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 10, 0.3f, 0.3f, 0.3f, 0.1f, dust);
				});
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void spellEffectReverse(Vector vec, Location from) {
		new BukkitRunnable() {
			double t = 0;
			Location loc = from.clone();
			Vector v = vec.multiply(-1);
			List<Entity> shooted = new ArrayList<>();
			DustOptions dust = new DustOptions(Color.RED, 1.5f);
			@Override
			public void run() {
				if(t >= 20 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				t+=0.5;
				double x = v.getX()*t;
				double y = v.getY()*t;
				double z = v.getZ()*t;
				loc.add(x, y, z);
				
				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 3, 0.15f, 0.15f, 0.15f, 0.05f, dust);
				
				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.6, loc.getY()-0.6, loc.getZ()-0.6, loc.getX()+0.6, loc.getY()+0.6, loc.getZ()+0.6);
					if(!aabb.c(aabb2))
						return false;
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
						return false;
					if(e instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
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
					shooted.add(e);
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					p.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 10, 0.3f, 0.3f, 0.3f, 0.1f, dust);
				});
				
				loc.subtract(x, y, z);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
