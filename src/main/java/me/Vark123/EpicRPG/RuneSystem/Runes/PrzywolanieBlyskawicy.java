package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Random;

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
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;

public class PrzywolanieBlyskawicy extends ARune {

	public PrzywolanieBlyskawicy(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		
		int time = (20/5)*3;
		final Location loc = p.getLocation().add(0, .25, 0);
		loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 1.7F);
		new BukkitRunnable() {
			double timer = 0;
			@Override
			public void run() {
				if(timer >= time || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++timer;
				loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 9, .2f, .3f, .2f, .05f);
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
		new BukkitRunnable() {
			Random rand = new Random();
			Location loc2 = loc.clone().add(rand.nextInt(20)-10, 60, rand.nextInt(20)-10);
			Vector vec = (new Vector(loc.getX()-loc2.getX(), loc.getY()-loc2.getY(), loc.getZ()-loc2.getZ())).normalize();
			double temp = 0;
			@Override
			public void run() {

				if(!casterInCastWorld())
					return;
				
				loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, .5f);
				for(double k = loc2.getY(); k >= loc.getY(); k-=0.5) {
					double x = vec.getX()*temp;
					double y = vec.getY()*temp;
					double z = vec.getZ()*temp;
					loc2.add(x, y, z);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc2, 10,0.1F,0.1F,0.1F,0.05F);
					loc2.subtract(x, y, z);
					temp+=0.5;
				}
				
				for(double r = 0.5; r < dr.getObszar(); r += .5) {
					for(double theta = 0; theta < (Math.PI*2); theta += ((Math.PI*2)/(r*4))) {
						double x = r*Math.sin(theta);
						double z = r*Math.cos(theta);
						loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.clone().add(x,0,z), 2, .05f, 0, .05f, .02f);
					}
				}
				
				for(Entity e : loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar())) {
					if(e.equals(p) || !(e instanceof LivingEntity))
						continue;
					if(e instanceof Player || e.hasMetadata("NPC")) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
							continue;
					}
					if(e.getLocation().distance(loc) > dr.getObszar())
						continue;
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
				}
				
			}
		}.runTaskLater(Main.getInstance(), 20*3);
		
	}

}
