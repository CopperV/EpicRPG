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
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class SwietaKrucjata extends ARune {

	public SwietaKrucjata(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1.1f);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation().add(0, 1.25, 0);
			Vector vec = loc.getDirection().normalize().multiply(1.1);
			@Override
			public void run() {
				if(t >= 30 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				loc.add(vec);
				p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 10, 0.25f, 0.25f, 0.25f, 0.15f);

				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
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
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					e.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_DEATH, 1, 1.25f);
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
