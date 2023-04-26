package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import me.Vark123.EpicRPGRespawn.Main;

public class FalaUderzeniowa extends ARune {
	
	private List<Entity> shooted;

	public FalaUderzeniowa(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		shooted = new ArrayList<>();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_BIG_FALL, 2, 0.6f);
		new BukkitRunnable() {
			BlockData data = Bukkit.createBlockData(Material.DIRT);
			Location loc = p.getLocation();
			double r = 0;
			@Override
			public void run() {
				if(r > dr.getObszar() || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				r += 0.17;
				
				for(double theta = 0; theta <=(2*Math.PI); theta = theta + (Math.PI/(r*2))) {
					double x = r*Math.sin(theta);
					double z = r*Math.cos(theta);
					Location tmp = new Location(loc.getWorld(), loc.getX()+x, loc.getY(), loc.getZ()+z);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
				}
				
				for(Entity entity : loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar())) {
					if(entity.equals(p) || !(entity instanceof LivingEntity) || shooted.contains(entity)) {
						continue;
					}
					if(entity.getLocation().distance(loc) > r)
						continue;
					shooted.add(entity);
					if(entity instanceof Player || entity.hasMetadata("NPC")) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
						if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
							continue;
					}
					if(RuneDamage.damageNormal(p, (LivingEntity)entity, dr)) {
						Location eLoc = entity.getLocation();
						Vector tmp = new Vector(eLoc.getX() - loc.getX(),
								0,
								eLoc.getZ() - loc.getZ()).normalize().setY(1).multiply(3);
						entity.setVelocity(tmp);
					}
					entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.ENTITY_GENERIC_SMALL_FALL, 1, 0.8f);
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
