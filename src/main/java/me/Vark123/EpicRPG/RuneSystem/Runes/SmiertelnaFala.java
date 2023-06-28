package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
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

public class SmiertelnaFala extends ARune {
	
	private List<Entity> shooted;

	public SmiertelnaFala(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		shooted = new ArrayList<Entity>();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_HORSE_DEATH, 1, 1);
		new BukkitRunnable() {
			Location loc = p.getLocation();
			Location check = p.getLocation();
			double t = 0;
			@Override
			public void run() {
				t+=0.25;
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x, 0.25, z);
					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
						this.cancel();
						return;
					}
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					
					loc.subtract(x,1.5,z);
				}
				
				loc.getWorld().getNearbyEntities(loc, t, t, t, e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
						return false;
					if(e.getLocation().distance(loc) > t)
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
					RuneDamage.damageNormal(p, (LivingEntity) e, dr);
					loc.getWorld().playSound(e.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, 1, 1);
					shooted.add(e);
				});
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

}
