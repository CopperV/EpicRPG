package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
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
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;

public class KrwawyDeszcz extends ARune {

	List<Entity> entitiesList = new ArrayList<>();
	
	public KrwawyDeszcz(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier2 = true;
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.WEATHER_RAIN, 3, 0.1f);
		new BukkitRunnable() {
			double t = 0;
			LivingEntity le;
			@Override
			public void run() {
				if(t>30 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				
				Location loc = p.getLocation();
				Location l1 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar(), 
						loc.getY(), 
						loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar());
				Location l2 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar(), 
						loc.getY(), 
						loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar());
				Location l3 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar(), 
						loc.getY(), 
						loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar());
				Location l4 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar(), 
						loc.getY(), 
						loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar());
				spellEffect(l1);
				spellEffect(l2);
				spellEffect(l3);
				spellEffect(l4);
				
				Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc,dr.getObszar(), dr.getObszar(), dr.getObszar());
				
				tmpList.parallelStream().filter(e -> {
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
					RuneDamage.damageNormal(p, le, dr, (p,le,dr)->{
						Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()->{
							new BukkitRunnable() {
								double t = 0;
								DustOptions dust = new DustOptions(Color.RED, 1f);
								double dmg = dr.getDamage()/50.0;
								@Override
								public void run() {
									
									if(t>15 || !casterInCastWorld() || !entityInCastWorld(e)) {
										this.cancel();
										return;
									}
									++t;
									
									if(le.isDead()) {
										this.cancel();
										return;
									}
									
									if(!RuneDamage.damageTiming(p, le, dr, dmg)) {
										this.cancel();
										return;
									}
									Location loc = le.getLocation().clone().add(0, 1, 0);
									loc.getWorld().playSound(loc, Sound.ENTITY_SLIME_ATTACK, 1, 1.2f);
									loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 2, 0.2, 0.2, 0.2, 0.1, dust);
									
									
								}
							}.runTaskTimer(Main.getInstance(), 0, 20);
						}, 20);
					});
				});
				
			}
			
		}.runTaskTimer(Main.getInstance(), 0, 10);

		RuneManager.getInstance().getObszarowkiCd().put(p, new Date());
	}
	
	private void spellEffect(Location loc) {

		Location loc2 = new Location(loc.getWorld(), 
				loc.getX()+Math.random()*2*8-8, 
				loc.getY()+35, 
				loc.getZ()+Math.random()*2*8-8);
		Vector p1 = new Vector(loc.getX(), loc.getY(), loc.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		Vector vec = p1.clone().subtract(p2).normalize();
		new BukkitRunnable() {
			
			Location loc3 = loc2.clone();
			double distance = loc2.distance(loc);
			DustOptions dust = new DustOptions(Color.RED, 2);
			
			@Override
			public void run() {
				
				if(loc3.distance(loc2) > distance || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				loc.getWorld().spawnParticle(Particle.REDSTONE, loc3, 3, 0.3, 0.5, 0.3, 0.1, dust);
				
				loc3.add(vec.getX(), vec.getY(), vec.getZ());
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
