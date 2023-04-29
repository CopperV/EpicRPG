package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
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

public class Meteor extends ARune {
	
	private List<Entity> shooted;

	public Meteor(ItemStackRune dr, Player p) {
		super(dr, p);
		shooted = new ArrayList<Entity>();
	}

	@Override
	public void castSpell() {
		Location loc = p.getTargetBlock((Set<Material>)null, 40).getLocation();
		loc.add(0, 30, 0);
		new BukkitRunnable() {
			double t = 30;
			@Override
			public void run() {
				loc.add(0, t, 0);
				loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
				p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 1, 0, 0, 0, 0);
				p.getWorld().spawnParticle(Particle.CLOUD, loc, 10, 0.75F, 0.75F, 0.75F, 0);
				if(!loc.getBlock().getType().equals(Material.AIR) || !casterInCastWorld()) {
					spellEffect(loc.clone());
					this.cancel();
					return;
				}
				loc.subtract(0, t, 0);
				t--;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void spellEffect(Location loc) {
		Location check = loc.clone();
		new BukkitRunnable() {
			double t = 0;
			@Override
			public void run() {
				t++;
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x, 1, z);
					loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
						this.cancel();
						return;
					}
					p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1, 0, 0, 0, 0);
					
					loc.subtract(x,1,z);
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
							return false;
					}
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).parallelStream().forEach(e -> {
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					shooted.add(e);
				});
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

}
