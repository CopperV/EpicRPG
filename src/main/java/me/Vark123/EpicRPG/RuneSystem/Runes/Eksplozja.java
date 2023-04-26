package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
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

public class Eksplozja extends ARune {

	List<Vector> directions;
	
	public Eksplozja(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		directions = new ArrayList<>();
		Location loc = p.getLocation();
		loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 0.2F);
		loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 0.5F);
		loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 0.8F);
		loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 1.1F);
		loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 1.4F);
		loc.getWorld().playSound(loc, Sound.ENTITY_TNT_PRIMED, 3, 1.7F);
		
		double r = 2;
		double x,y,z,theta;
		for(int i = 0; i < 25; ++i) {
			theta = Math.random()*Math.PI*2;
			x = r * Math.sin(theta);
			y = Math.random()*2.5+0.1;
			z = r * Math.cos(theta);
			directions.add(new Vector(x, y, z));
		}
		
		for(Vector v : directions) {
			Vector tmp = v.clone().normalize();
			loc.getWorld().spawnParticle(Particle.CRIT, loc.clone().add(v), 0, 
					tmp.getX()*(-2),tmp.getY(),tmp.getZ()*(-2));
		}
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				spellEffect(loc);
			}
		}.runTaskLater(Main.getInstance(), 20*5);
		
	}
	
	private void spellEffect(Location loc) {
		
		if(!casterInCastWorld())
			return;
		
		loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5, 0.2F);
		for(Vector v : directions) {
			Vector tmp = v.normalize().multiply(3);
			loc.getWorld().spawnParticle(Particle.CRIT, loc, 0,
					tmp.getX(), tmp.getY(), tmp.getZ());
		}
		
		Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc,dr.getObszar(), dr.getObszar(), dr.getObszar());
		for(Entity entity : tmpList) {
			if(!entity.equals(p) && entity instanceof LivingEntity) {
				if(entity instanceof Player || entity.hasMetadata("NPC")) {
					RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
					ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
					if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
						continue;
				}
				RuneDamage.damageNormal(p, (LivingEntity)entity, dr);
				if(!(entity instanceof ArmorStand)) {
					Location eLoc = entity.getLocation();
					double strength = dr.getObszar() - Math.abs(loc.distance(eLoc));
					Vector tmp = new Vector(eLoc.getX() - loc.getX(),
							0,
							eLoc.getZ() - loc.getZ()).normalize().setY(1).multiply(strength/2);
					entity.setVelocity(tmp);
				}
			}
		}
		
	}

}
