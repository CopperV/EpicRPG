package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class PiachWOczy extends ARune{

	public PiachWOczy(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			public void run() {
				t+=0.25;
				double x = vec.getX()*t;
				double y = vec.getY()*t+0.1;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.CRIT, loc, 1, 0, 0, 0, 0);
				p.getWorld().spawnParticle(Particle.CRIT, loc, 10, 0.75F,0.75F,0.75F,0.4F);
				loc.getWorld().playSound(loc, Sound.BLOCK_SAND_STEP, 1, 1);

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
					p.getWorld().spawnParticle(Particle.CRIT, e.getLocation().add(0,1,0),20,0.5F,0.5F,0.5F,0);
					loc.getWorld().playSound(loc, Sound.BLOCK_SAND_STEP, 10, 1);
					PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 3);
					PotionEffect pe2 = new PotionEffect(PotionEffectType.BLINDNESS, 20*dr.getDurationTime(), 3);
					((LivingEntity)e).addPotionEffect(pe);
					((LivingEntity)e).addPotionEffect(pe2);
					cancel();
				});
				
				if(this.isCancelled()) {
					return;
				}
				
				loc.subtract(x, y, z);
				if(t>10 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
