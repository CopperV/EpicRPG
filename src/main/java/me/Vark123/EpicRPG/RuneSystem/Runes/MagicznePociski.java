package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
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

public class MagicznePociski extends ARune {

	public MagicznePociski(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0.6f);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1.4f);
		DustOptions dust1 = new DustOptions(Color.fromRGB(128, 0, 128), 1);
		DustOptions dust2 = new DustOptions(Color.fromRGB(75, 0, 130), 1);
		DustOptions dust3 = new DustOptions(Color.fromRGB(255, 0, 255), 1);
		new BukkitRunnable() {
			double theta = 0;
			Location loc = p.getLocation().add(0,1.1,0);
			Vector vec = loc.getDirection().normalize().multiply(0.65);
			double t = 0;
			@Override
			public void run() {
				if(t >= 60 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				theta = theta + Math.PI/16;
				
				loc.add(vec);
				double x1 = 2 * Math.sin(theta);
				double z1 = 2 * Math.cos(theta);
				double x2 = 2 * -Math.cos(theta);
				double z2 = 2 * -Math.sin(theta);
				double y3 = 2 * Math.sin(theta);
				
				p.getWorld().spawnParticle(Particle.REDSTONE, loc.clone().add(x1,0,z1), 12, 0.15f, 0.15f, 0.15f, 0.04f, dust1);
				p.getWorld().spawnParticle(Particle.REDSTONE, loc.clone().add(x2,0,z2), 12, 0.15f, 0.15f, 0.15f, 0.04f, dust2);
				p.getWorld().spawnParticle(Particle.REDSTONE, loc.clone().add(0,y3,0), 12, 0.15f, 0.15f, 0.15f, 0.04f, dust3);
				
				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1.25, loc.getY()-1.25, loc.getZ()-1.25, loc.getX()+1.25, loc.getY()+1.25, loc.getZ()+1.25);
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
				}).stream().min((e1, e2) -> {
					double dist1 = e1.getLocation().distanceSquared(loc);
					double dist2 = e2.getLocation().distanceSquared(loc);
					if(dist1 == dist2)
						return 0;
					return dist1 < dist2 ? -1 : 1;
				}).ifPresent(e -> {
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					cancel();
				});
				
				if(this.isCancelled()) {
					return;
				}
				
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
