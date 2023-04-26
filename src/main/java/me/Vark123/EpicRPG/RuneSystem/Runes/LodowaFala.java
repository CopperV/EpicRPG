package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

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

public class LodowaFala extends ARune {

	private List<Entity> shooted;
	
	public LodowaFala(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		shooted = new ArrayList<Entity>();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, -10);
		PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, 20*45, 1);
		new BukkitRunnable() {
			Location loc = p.getLocation();
			Location check = p.getLocation();
			double t = 0;
			LivingEntity le;
			@Override
			public void run() {
				t+=0.25;
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x, 0.25, z);
					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
						this.cancel();
					}
					p.getWorld().spawnParticle(Particle.SNOWBALL, loc,2,0.2F,0.2F,0.2F,0.05F);
					p.getWorld().spawnParticle(Particle.CLOUD, loc,2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SNOWBALL, loc,2,0.2F,0.2F,0.2F,0.05F);
					p.getWorld().spawnParticle(Particle.CLOUD, loc,2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SNOWBALL, loc,2,0.2F,0.2F,0.2F,0.05F);
					p.getWorld().spawnParticle(Particle.CLOUD, loc,2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SNOWBALL, loc,2,0.2F,0.2F,0.2F,0.05F);
					p.getWorld().spawnParticle(Particle.CLOUD, loc,2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SNOWBALL, loc,2,0.2F,0.2F,0.2F,0.05F);
					p.getWorld().spawnParticle(Particle.CLOUD, loc,2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SNOWBALL, loc,2,0.2F,0.2F,0.2F,0.05F);
					p.getWorld().spawnParticle(Particle.CLOUD, loc,2,0.2F,0.2F,0.2F,0.05F);
					
					for(Entity entity:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
						AxisAlignedBB aabb = ((CraftEntity)entity).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1, loc.getZ()-1, loc.getX()+1, loc.getY()+3, loc.getZ()+1);
						if(aabb.c(aabb2)) {
							if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(check) <= dr.getObszar()) {
								if(entity instanceof Player || entity.hasMetadata("NPC")) {
									RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
									ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
									if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
										continue;
								}
								le = (LivingEntity) entity;
								RuneDamage.damageNormal(p, le, dr, (p,le,dr)->{
									le.addPotionEffect(pe);
									le.getLocation().getWorld().playSound(le.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
								});
								shooted.add(entity);
							}
						}
					}
					loc.subtract(x,1.5,z);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
