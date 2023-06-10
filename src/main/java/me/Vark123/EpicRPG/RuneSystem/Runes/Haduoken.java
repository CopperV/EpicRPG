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
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class Haduoken extends ARune {

	public Haduoken(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SLIME_ATTACK, 1, 1);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			@Override
			public void run() {
				
				for(int i = 0; i < 2; ++i) {
					
					t+=0.4;
					double x = t*vec.getX();
					double y = t*vec.getY()+1;
					double z = t*vec.getZ();
					loc.add(x, y, z);
					DustOptions dust = new DustOptions(Color.AQUA, 0.6f);
					p.getWorld().spawnParticle(Particle.REDSTONE, loc, 20, 0.2, 0.2, 0.2, 0.05, dust);

					
					loc.getWorld().getNearbyEntities(loc, 4, 4, 4, e -> {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.75, loc.getY()-0.75, loc.getZ()-0.75, loc.getX()+0.75, loc.getY()+0.75, loc.getZ()+0.75);
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
						loc.getWorld().playSound(loc, Sound.ENTITY_MAGMA_CUBE_JUMP, 10, 1);
						RuneDamage.damageNormal(p, (LivingEntity)e, dr);
						this.cancel();
					});
					
					if(this.isCancelled()) {
						return;
					}
					

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

			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
