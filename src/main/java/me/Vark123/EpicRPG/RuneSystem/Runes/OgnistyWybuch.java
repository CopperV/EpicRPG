package me.Vark123.EpicRPG.RuneSystem.Runes;

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

public class OgnistyWybuch extends ARune {
	
	private static final Random rand = new Random();

	public OgnistyWybuch(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		final Location loc = p.getLocation().add(0, 0.1, 0);
		
		loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_AMBIENT, 2, 0.5f);
		
		new BukkitRunnable() {
			int timer = 20*4;
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!casterInCastWorld()) {
					cancel();
					return;
				}
				if(timer <= 0) {
					cancel();
					spellEffect(loc);
					return;
				}
				
				particleEffect(loc);
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void particleEffect(Location loc) {
		for(int i = 0; i < 6; ++i) {
			double radius = rand.nextDouble(1.5) + 1;
			double angle = rand.nextDouble(Math.PI * 2);
			
			double x = Math.sin(angle) * radius;
			double y = rand.nextDouble(3.5) - 1;
			double z = Math.cos(angle) * radius;
			
			Location source = loc.clone().add(x, y, z);
			Vector dir = new Vector(loc.getX() - source.getX(),
					loc.getY() - source.getY(),
					loc.getZ() - source.getZ()).normalize();
			
			loc.getWorld().spawnParticle(Particle.FLAME, source, 0, dir.getX(), dir.getY(), dir.getZ(), radius/20.f);
		}
	}
	
	private void spellEffect(Location loc) {
		if(!casterInCastWorld())
			return;

		loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_BREAK_BLOCK, 2.5f, 0.6F);
		for(int i = 0; i < 80; ++i) {
			double radius = rand.nextDouble(4) + 2;
			double angle = rand.nextDouble(Math.PI * 2);
			
			double x = Math.sin(angle) * radius;
			double y = rand.nextDouble(6) - 1;
			double z = Math.cos(angle) * radius;
			
			Location target = loc.clone().add(x, y, z);
			Vector dir = new Vector(target.getX() - loc.getX(),
					target.getY() - loc.getY(),
					target.getZ() - loc.getZ()).normalize();
			
			loc.getWorld().spawnParticle(Particle.FLAME, loc, 0, dir.getX(), dir.getY(), dir.getZ(), radius/5.f);
		}
		
		double radius = dr.getObszar();
		loc.getWorld().getNearbyEntities(loc, radius, radius, radius, e -> {
			if(e.getLocation().distanceSquared(loc) > (radius * radius))
				return false;
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
		}).stream()
		.map(e -> {
			return (LivingEntity) e;
		}).forEach(e -> {
			RuneDamage.damageNormal(p, e, dr, (p, le, dr)->{
				new BukkitRunnable() {
					int timer = 12;
					double dmg = dr.getDamage()/50.0;
					@Override
					public void run() {
						if(timer <= 0 || !casterInCastWorld() || !entityInCastWorld(e)) {
							this.cancel();
							return;
						}
						--timer;
						boolean end = RuneDamage.damageTiming(p, le, dr, dmg);
						if(!end) {
							this.cancel();
							return;
						}
						Location loc = le.getLocation().add(0,1,0);
						p.getWorld().spawnParticle(Particle.FLAME, loc,10,0.2,0.2,0.2,0.05);
						p.getWorld().playSound(loc, Sound.ENTITY_GENERIC_BURN, 1, 1);
					}
				}.runTaskTimer(Main.getInstance(), 0, 20);
			});
		});
	}

}
