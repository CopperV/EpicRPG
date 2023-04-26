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

public class SwieteSlowo extends ARune {

	private List<Entity> shooted;
	
	public SwieteSlowo(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		shooted = new ArrayList<>();
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 4, 1.5f);
		new BukkitRunnable() {
			Location loc = p.getLocation();
			Location check = p.getLocation();
			double t = 0.25;
			@Override
			public void run() {
				
				spawnColumnParticle(loc.clone(), Particle.FIREWORKS_SPARK);
				
				double progress = Math.PI/(4*t);
				for(double theta = 0; theta <= (Math.PI*2); theta+=progress) {
					
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					
					loc.add(x, 0, z);
					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
						this.cancel();
						return;
					}
					loc.add(0, 0.75, 0);
					
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 3, 0.1F, 0.1F, 0.1F, 0.05F);
					
					for(Entity entity:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
						AxisAlignedBB aabb = ((CraftEntity)entity).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.75, loc.getY()-2, loc.getZ()-0.75, loc.getX()+0.75, loc.getY()+2, loc.getZ()+0.75);
						if(aabb.c(aabb2)) {
							if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(check) <= dr.getObszar()) {
								if(entity instanceof Player || entity.hasMetadata("NPC")) {
									RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
									ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
									if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
										continue;
								}
								RuneDamage.damageNormal(p, (LivingEntity)entity, dr);
								entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1.6f);
								shooted.add(entity);
							}
						}
					}
					
					loc.subtract(x, 0.75, z);
					
				}
				
				t += 0.25;
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

	private void spawnColumnParticle(Location loc, Particle p) {
		for(double i = 0; i<40; i+=0.5) {
			loc.add(0, i, 0);
			
			loc.getWorld().spawnParticle(p, loc, 3, 0.25F, 0.25F, 0.25F, 0.01F);
			
			loc.subtract(0, i, 0);
		}
	}
	
}
