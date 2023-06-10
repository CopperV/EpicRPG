package me.Vark123.EpicRPG.RuneSystem.Runes;

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

public class Gejzer extends ARune {

	public Gejzer(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_SWIM, 1, 1);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			@Override
			public void run() {
				t+=0.4;
				double x = t*vec.getX();
				double y = t*vec.getY()+0.1;
				double z = t*vec.getZ();
				loc.add(x, y, z);
				p.getWorld().spawnParticle(Particle.CLOUD, loc,10,0,0,0,0);
				p.getWorld().spawnParticle(Particle.CLOUD, loc,10,0.2F,0.2F,0.2F,0.01F);

				loc.getWorld().getNearbyEntities(loc, 5, 5, 5, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1.5, loc.getY()-1.5, loc.getZ()-1.5, loc.getX()+1.5, loc.getY()+1.5, loc.getZ()+1.5);
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
					zadajDmg(e);
					cancel();
				});
				
				if(this.isCancelled())
					return;
				
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void zadajDmg(Entity e) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!casterInCastWorld() || !entityInCastWorld(e))
					return;
				p.getWorld().spawnParticle(Particle.DRIP_WATER, e.getLocation(),20,0.75F,1.9F,0.75F,0.5F);
				p.getWorld().spawnParticle(Particle.DRIP_WATER, e.getLocation().add(0, 1.5, 0),20,0.75F,1.9F,0.75F,0.5F);
				e.getLocation().getWorld().playSound(e.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 10, 1);
				RuneDamage.damageNormal(p, (LivingEntity)e, dr);
			}
		}.runTaskLater(Main.getInstance(), 20);
	}

}
