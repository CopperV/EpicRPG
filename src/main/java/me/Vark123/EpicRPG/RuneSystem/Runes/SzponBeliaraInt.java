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
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class SzponBeliaraInt extends ARune {

	public SzponBeliaraInt(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1, 0.1f);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Location loc2 = loc.clone();
			Vector vec = loc.getDirection().normalize();
			List<Entity> shooted = new ArrayList<>();
			DustOptions dust = new DustOptions(Color.RED, 1.5f);
			@Override
			public void run() {
				if(t >= 30 || !casterInCastWorld()) {
					castSpellReverse(vec, loc2);
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
				
				for(Entity e : loc.getWorld().getNearbyEntities(loc2, 3, 3, 3)) {
					if(shooted.contains(e)) continue;
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc2.getX()-0.6, loc2.getY()-0.6, loc2.getZ()-0.6, loc2.getX()+0.6, loc2.getY()+0.6, loc2.getZ()+0.6);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
							shooted.add(e);
							RuneDamage.damageNormal(p, (LivingEntity)e, dr);
							p.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 10, 0.3f, 0.3f, 0.3f, 0.1f, dust);
						}
					}
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
		
	}
	
	private void castSpellReverse(Vector vec, Location from) {
		new BukkitRunnable() {
			double t = 0;
			Location loc = from.clone();
			Vector v = vec.multiply(-1);
			List<Entity> shooted = new ArrayList<>();
			DustOptions dust = new DustOptions(Color.RED, 1.5f);
			@Override
			public void run() {
				if(t >= 30 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				t+=0.5;
				double x = v.getX()*t;
				double y = v.getY()*t;
				double z = v.getZ()*t;
				loc.add(x, y, z);
				
				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 3, 0.15f, 0.15f, 0.15f, 0.05f, dust);
				for(Entity e : loc.getWorld().getNearbyEntities(loc, 3, 3, 3)) {
					if(shooted.contains(e)) continue;
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.6, loc.getY()-0.6, loc.getZ()-0.6, loc.getX()+0.6, loc.getY()+0.6, loc.getZ()+0.6);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
							shooted.add(e);
							RuneDamage.damageNormal(p, (LivingEntity)e, dr);
							p.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 10, 0.3f, 0.3f, 0.3f, 0.1f, dust);
						}
					}
				}
				
				loc.subtract(x, y, z);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
