package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.LinkedList;
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
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;

public class WstrzasElektryczny extends ARune {

	private List<Entity> entitiesList = new ArrayList<>();

	public WstrzasElektryczny(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		List<Entity> tmp = new LinkedList<>();
		for(Entity e : p.getWorld().getNearbyEntities(p.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar())) {
			if(!casterInCastWorld())
				break;
			if(tmp.size() >= 5)
				break;
			if(e.getLocation().distance(p.getLocation()) > dr.getObszar())
				continue;
			if(!(e instanceof LivingEntity)) {
				entitiesList.add(e);
				continue;
			}
			if(e instanceof Player || e.hasMetadata("NPC")) {
				Location loc = e.getLocation();
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
				if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon")) {
					entitiesList.add(e);
					continue;
				}
			}
			tmp.add(e);
			drawLine(p.getLocation().add(0, 1.25, 0), e.getLocation().add(0,1,0));
		}
		if(tmp.size() > 0){
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
			spellEffect(tmp);
		}
		
	}
	
	private void spellEffect(List<Entity> list) {
		entitiesList.addAll(list);
		for(Entity e : list) {
			RuneDamage.damageNormal(p, (LivingEntity)e, dr);
		}
		new BukkitRunnable() {
			List<Entity> main = new ArrayList<>();
			@Override
			public void run() {
				
				for(Entity e : list) {
					if(e.isDead())
						continue;
					List<Entity> tmp = new LinkedList<>();
					for(Entity en : e.getWorld().getNearbyEntities(e.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar())) {
						if(tmp.size() >= 5)
							break;
						if(en.getLocation().distance(e.getLocation()) > dr.getObszar())
							continue;
						if(entitiesList.contains(en) || main.contains(en))
							continue;
						if(!(en instanceof LivingEntity)) {
							entitiesList.add(en);
							continue;
						}
						if(en instanceof Player || en.hasMetadata("NPC")) {
							Location loc = en.getLocation();
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(en.getLocation()));
							if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon")) {
								entitiesList.add(en);
								continue;
							}
						}
						tmp.add(en);
						drawLine(en.getLocation().add(0, 1, 0), e.getLocation().add(0,1,0));
					}
					main.addAll(tmp);
				}

				if(main.size() > 0) {
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
					spellEffect(main);
				}
				
			}
		}.runTaskLater(Main.getInstance(), 10);
	}
	
	private void drawLine(Location loc1, Location loc2) {
		double space = 0.25;
		Vector p1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		double distance = loc1.distance(loc2);
		Vector vec = p2.clone().subtract(p1).normalize().multiply(space);
		for(double length = 0; length < distance; p1.add(vec), length += space) {
			loc1.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p1.getX(), p1.getY(), p1.getZ() , 10,0.1F,0.1F,0.1F,0.01F);
		}
	}

}
