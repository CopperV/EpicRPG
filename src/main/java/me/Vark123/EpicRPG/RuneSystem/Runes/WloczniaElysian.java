package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.LinkedList;
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

public class WloczniaElysian extends ARune {

	private LinkedList<Location> stos;
	private List<Entity> shooted;
	
	public WloczniaElysian(ItemStackRune dr, Player p) {
		super(dr, p);
		stos = new LinkedList<>();
		shooted = new ArrayList<>();
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THROW, 1.2f, 0.2f);
		new BukkitRunnable() {
			int t = 0;
			Location loc = p.getLocation().add(0, 1, 0);
			Vector vec = loc.getDirection().normalize();
			@Override
			public void run() {
				if(t>=45 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				
				loc.add(vec);
				stos.addLast(loc.clone());
				while(stos.size() > 5)
					stos.removeFirst();
				
				for(Location point : stos)
					p.getWorld().spawnParticle(Particle.END_ROD, point, 7, 0.05f, 0.05f, 0.05f, 0.02f);
				
				for(Entity e : loc.getWorld().getNearbyEntities(loc, 3, 3, 3)) {
					if(shooted.contains(e)) continue;
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.7, loc.getY()-0.7, loc.getZ()-0.7, loc.getX()+0.7, loc.getY()+0.7, loc.getZ()+0.7);
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
							p.getWorld().spawnParticle(Particle.END_ROD, e.getLocation().add(0,1,0), 15, 0.4f, 0.5f, 0.4f, 0.15f);
							p.getWorld().playSound(e.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1, 0.7f);
						}
					}
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				
				loc.add(vec);
				stos.addLast(loc.clone());
				while(stos.size() > 5)
					stos.removeFirst();
				
				for(Location point : stos)
					p.getWorld().spawnParticle(Particle.END_ROD, point, 7, 0.05f, 0.05f, 0.05f, 0.02f);
				
				for(Entity e : loc.getWorld().getNearbyEntities(loc, 3, 3, 3)) {
					if(shooted.contains(e)) continue;
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.7, loc.getY()-0.7, loc.getZ()-0.7, loc.getX()+0.7, loc.getY()+0.7, loc.getZ()+0.7);
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
							p.getWorld().spawnParticle(Particle.END_ROD, e.getLocation().add(0,1,0), 15, 0.4f, 0.5f, 0.4f, 0.15f);
							p.getWorld().playSound(e.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1, 0.7f);
						}
					}
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
