package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
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

public class SopelLodu extends ARune {

	public SopelLodu(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1.2f);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation().add(0, 1.25, 0);
			Vector vec = loc.getDirection().normalize().multiply(0.8);
			@Override
			public void run() {
				if(t >= 33 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				for(int i = 0; i < 2; ++i) {
					loc.add(vec);
					p.getWorld().spawnParticle(Particle.END_ROD, loc, 7, 0.06f, 0.06f, 0.06f, 0.01f);

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
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
