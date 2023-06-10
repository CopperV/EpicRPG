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
import org.bukkit.util.Vector;

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

public class Sztorm extends ARune {

	List<Entity> entitiesList = new ArrayList<>();

	public Sztorm(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.WEATHER_RAIN_ABOVE, 3.5F, 0.7F);
		new BukkitRunnable() {
			double t = 0;
			LivingEntity le;
			@Override
			public void run() {
				if(t>=20 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				
				Location loc = p.getLocation();
				double theta = Math.random() * Math.PI * 2;
				double x = dr.getObszar() * Math.sin(theta);
				double y = Math.random()*1+1;
				double z = dr.getObszar() * Math.cos(theta);
				Location loc1 = new Location(loc.getWorld(), 
						loc.getX()+x, 
						loc.getY()+y, 
						loc.getZ()+z);
				y = Math.random()*5+5;
				Location loc2 = new Location(loc.getWorld(), 
						loc.getX()-x, 
						loc.getY()+y, 
						loc.getZ()-z);
				theta = Math.random() * Math.PI * 2;
				x = dr.getObszar() * Math.sin(theta);
				y = Math.random()*1+1;
				z = dr.getObszar() * Math.cos(theta);
				Location loc3 = new Location(loc.getWorld(), 
						loc.getX()+x, 
						loc.getY()+y, 
						loc.getZ()+z);
				y = Math.random()*5+5;
				Location loc4 = new Location(loc.getWorld(), 
						loc.getX()-x, 
						loc.getY()+y, 
						loc.getZ()-z);
				theta = Math.random() * Math.PI * 2;
				x = dr.getObszar() * Math.sin(theta);
				y = Math.random()*1+1;
				z = dr.getObszar() * Math.cos(theta);
				Location loc5 = new Location(loc.getWorld(), 
						loc.getX()+x, 
						loc.getY()+y, 
						loc.getZ()+z);
				y = Math.random()*5+5;
				Location loc6 = new Location(loc.getWorld(), 
						loc.getX()-x, 
						loc.getY()+y, 
						loc.getZ()-z);
				spellEffect(loc1, loc2);
				spellEffect(loc3, loc4);
				spellEffect(loc5, loc6);
				
				Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc,dr.getObszar(), dr.getObszar(), dr.getObszar());
				
				tmpList.stream().filter(e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(entitiesList.contains(e))
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
					entitiesList.add(e);
					le = (LivingEntity) e;
					RuneDamage.damageNormal(p, le, dr, (p, le, dr)->{
						double X = Math.random()*4-2;
						double Y = Math.random()*3;
						double Z = Math.random()*4-2;
						le.setVelocity(new Vector(X, Y, Z));
					});
				});
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
	}

	private void spellEffect(Location loc1, Location loc2) {
		Vector p1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		Vector vec = p1.clone().subtract(p2).normalize();
		new BukkitRunnable() {
			
			Location loc3 = loc2.clone();
			double distance = loc2.distance(loc1);
			
			@Override
			public void run() {
				
				if(loc3.distance(loc2) > distance || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				loc3.getWorld().spawnParticle(Particle.DRIP_WATER, loc3, 2, 0.1, 0.1, 0.1, 0.05);
				loc3.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc3, 2, 0.1, 0.1, 0.1, 0.05);
				loc3.add(vec);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
}
