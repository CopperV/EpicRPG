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
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class SzalBeliara extends ARune {

	public SzalBeliara(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1, 0.5f);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Location loc2 = loc.clone();
			Vector vec = loc.getDirection().normalize().multiply(1.1);
			List<Entity> shooted = new ArrayList<>();
			DustOptions dust1 = new DustOptions(Color.fromRGB(224, 32, 32), 0.8f);
			DustOptions dust2 = new DustOptions(Color.fromRGB(224, 32, 32), 0.5f);
			@Override
			public void run() {
				if(t >= 35 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				t+= 0.5;
				
				double x = vec.getX()*t;
				double y = vec.getY()*t + 1.5;
				double z = vec.getZ()*t;
				loc2.setX(loc.getX()+x);
				loc2.setY(loc.getY()+y);
				loc2.setZ(loc.getZ()+z);
				
				p.getWorld().spawnParticle(Particle.REDSTONE, loc2, 4, 0.1f, 0.1f, 0.1f, 0.02f, dust1);
				p.getWorld().spawnParticle(Particle.REDSTONE, loc2, 4, 0f, 0f, 0f, 0.25f, dust2);
				
				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
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
					shooted.add(e);
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					p.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation(), 15, 0.4f, 0.5f, 0.4f, 0.15f, dust1);
				});
				
				if(loc2.getBlock().getType().isSolid() && !loc2.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
