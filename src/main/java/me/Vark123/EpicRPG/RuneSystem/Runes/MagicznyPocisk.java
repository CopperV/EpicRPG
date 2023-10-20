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

import de.slikey.effectlib.util.MathUtils;
import io.lumine.mythic.core.utils.VectorUtils;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class MagicznyPocisk extends ARune {

	public MagicznyPocisk(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
		new BukkitRunnable() {
			double t = 0;
			double theta = 0;
			Location loc = p.getLocation();
//			Vector vec = loc.getDirection().normalize();
			@Override
			public void run() {
				if(t==0) {
					loc.add(0, 1.5, 0);
				}
				t+=0.33;
				double x = Math.sin(theta);
				double y = 2*t;
				double z = -Math.sin(theta);
				theta = theta + Math.PI/16;
				Vector v = new Vector(x,y,z);
				VectorUtils.rotateAroundAxisX(v, (loc.getPitch() + 90) * MathUtils.degreesToRadians);
				VectorUtils.rotateAroundAxisZ(v, (loc.getPitch() - 45) * MathUtils.degreesToRadians);
	            VectorUtils.rotateAroundAxisY(v, -loc.getYaw() * MathUtils.degreesToRadians);
				loc.add(v);
				loc.getWorld().playSound(loc, Sound.BLOCK_LAVA_POP, 1, 1);
				DustOptions dust = new DustOptions(Color.PURPLE, 1);
				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 20, 0.2, 0.2, 0.2, 0.05, dust);
			
				loc.getWorld().getNearbyEntities(loc, 4, 4, 4, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1, loc.getZ()-1, loc.getX()+1, loc.getY()+1, loc.getZ()+1);
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
				loc.subtract(v);
				if(t>30 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
