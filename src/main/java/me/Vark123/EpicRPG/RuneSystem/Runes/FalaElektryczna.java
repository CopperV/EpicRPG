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
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;

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
				
				entities.stream().filter(e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
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
				}).forEach(e -> {
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					e.getLocation().getWorld().playSound(e.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
					shooted.add(e);
				});
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
