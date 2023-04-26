package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

public class FalaElektryczna extends ARune {
	
	private List<Entity> shooted;

	public FalaElektryczna(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		shooted = new ArrayList<>();
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 2, 0.4F);
		new BukkitRunnable() {
			
			Location loc = p.getLocation();
			double t = 0;
			
			@Override
			public void run() {
				
				if(t>dr.getObszar() || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				t+=0.25;
				
				for(double theta = 0; theta <=(2*Math.PI); theta = theta + (Math.PI/(t*2))) {
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					Location tmp = new Location(loc.getWorld(), loc.getX()+x, loc.getY(), loc.getZ()+z);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F);

				}
				
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, t, t, t);
				for(Entity entity : entities) {
					if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity)) {
						if(entity instanceof Player || entity.hasMetadata("NPC")) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
							if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
								continue;
						}
						RuneDamage.damageNormal(p, (LivingEntity)entity, dr);
						entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
						shooted.add(entity);
					}
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
