package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;

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
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import net.minecraft.world.phys.AxisAlignedBB;

public class LodowePrzebicie extends ARune {

	public LodowePrzebicie(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation();
		p.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1, 0.8f);
		p.getWorld().spawnParticle(Particle.SNOWFLAKE, loc.clone().add(0,1,0), 15, 0.4,0.4,0.4,0.03);
		
		Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
		
		tmpList.stream().filter(e -> {
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
		}).forEach(e -> {
			spellEffect(e);
		});
	}
	
	private void spellEffect(Entity e) {
		new BukkitRunnable() {
			Location startLoc = e.getLocation().add(0, 25, 0);
			Location endLoc = e.getLocation();
			Vector v = new Vector(0, endLoc.getY() - startLoc.getY(), 0).normalize().multiply(0.1);
			Location loc = startLoc.clone();
			@Override
			public void run() {
//				Bukkit.broadcastMessage("Dystans: "+loc.distance(startLoc)+" = "+loc.getY()+" "+startLoc.getY());
				if(loc.distance(startLoc) > 40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}

				p.getWorld().spawnParticle(Particle.SNOWBALL, loc, 6, 0.04f, 0.04f, 0.04f, 0.02f);
				
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
					e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 0.65f);
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
				
				loc.add(v);
				v.multiply(1.05);
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
