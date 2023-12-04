package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

public class Zaglada extends ARune {
	
	private static final int TRAILS_AMOUNT = 10;
	private static final Random rand = new Random();

	public Zaglada(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 3, 0.5f);
		
		int radius = dr.getObszar();
		Location point = p.getLocation().add(0, 0.1, 0);
		int points = radius * 6;
		
		List<Location> ringPoinst = new LinkedList<>();
		for(double theta = 0; theta < 2*Math.PI; theta += 2*Math.PI/points) {
			double x = Math.sin(theta) * radius;
			double z = Math.cos(theta) * radius;
			
			ringPoinst.add(point.clone().add(x,0,z));
		}

		List<Location> trailPoints = new LinkedList<>();
		for(int i = 0; i < TRAILS_AMOUNT; ++i) {
			Vector vec = new Vector(rand.nextDouble(2)-1, 0, rand.nextDouble(2)-1).normalize().multiply(0.25);
			Location trailLoc = point.clone();
			int length = radius * 5;
			
			int distance = 10;
			while(length > 0 && trailLoc.distanceSquared(point) < (radius*radius)) {
				trailLoc.add(vec);
				trailPoints.add(trailLoc.clone());
				
				if(distance < 0 && rand.nextDouble() < 0.1) {
					vec = vec.rotateAroundY(Math.toRadians(90) - Math.toRadians(45));
					distance = 10;
				}
				
				--length;
				--distance;
			}
		}
		
		new BukkitRunnable() {
			double speed = 0.01f;
			int timer = 4*5;
			@Override
			public void run() {
				if(timer <= 0) {
					spellEffect(point, ringPoinst, trailPoints);
					cancel();
					return;
				}
				if(!casterInCastWorld()) {
					cancel();
					return;
				}
				if(isCancelled())
					return;
				
				for(Location loc : ringPoinst)
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 0, 0, 0.5f, 0, speed);
				for(Location loc : trailPoints)
					p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 0, 0, 0.5f, 0, speed);
				
				--timer;
				speed += 0.01;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

	private void spellEffect(Location point, List<Location> ringPoinst, List<Location> trailPoints) {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 2, 0.7f);
		for(Location loc : ringPoinst) {
			for(double y = 0; y < 4; y += 0.5) {
				Location tmp = loc.clone().add(0, y, 0);
				p.getWorld().spawnParticle(Particle.SMOKE_LARGE, tmp, 1, 0.1f, 0.1f, 0.1f, 0.15);
			}
		}
		for(Location loc : trailPoints) {
			for(double y = 0; y < 4; y += 0.5) {
				Location tmp = loc.clone().add(0, y, 0);
				p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, tmp, 1, 0.1f, 0.1f, 0.1f, 0.15);
			}
		}
		
		int radius = dr.getObszar();
		point.getWorld().getNearbyEntities(point, radius, radius, radius, e -> {
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
		}).forEach(e -> RuneDamage.damageNormal(p, (LivingEntity)e, dr));
	}
	
}
