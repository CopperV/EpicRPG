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
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class WstrzasElektryczny extends ARune {

	private List<Entity> entitiesList = new ArrayList<>();

	public WstrzasElektryczny(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		List<Entity> tmp = new LinkedList<>();
		
		if(!casterInCastWorld())
			return;
		
		p.getWorld().getNearbyEntities(p.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar(), e -> {
			if(e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if(e.getLocation().distance(p.getLocation()) > dr.getObszar())
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
		}).stream().takeWhile(e -> {
			if(tmp.size() > 5)
				return false;
			tmp.add(e);
			return true;
		}).forEach(e -> {
			drawLine(p.getLocation().add(0, 1.25, 0), e.getLocation().add(0,1,0));
		});
		
		if(tmp.size() > 0){
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
			spellEffect(tmp);
		}
		
	}
	
	private void spellEffect(List<Entity> list) {
		entitiesList.addAll(list);
		list.stream().forEach(e -> {
			RuneDamage.damageNormal(p, (LivingEntity)e, dr);
		});
		new BukkitRunnable() {
			List<Entity> main = new ArrayList<>();
			@Override
			public void run() {
				
				list.stream().forEach(e -> {
					List<Entity> tmp = new LinkedList<>();
					e.getWorld().getNearbyEntities(e.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar(), en -> {
						if(en.equals(p) || !(en instanceof LivingEntity))
							return false;
						if(en.getLocation().distance(e.getLocation()) > dr.getObszar())
							return false;
						if(entitiesList.contains(en) || main.contains(en))
							return false;
						if(en instanceof Player) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(en.getLocation()));
							State flag = set.queryValue(null, Flags.PVP);
							if(flag != null && flag.equals(State.ALLOW)
									&& !en.getWorld().getName().toLowerCase().contains("dungeon"))
								return false;
						}
						if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(en).isDamageable())
							return false;
						return true;
					}).stream().takeWhile(en -> {
						if(tmp.size() > 5)
							return false;
						tmp.add(en);
						return true;
					}).forEach(en -> {
						drawLine(en.getLocation().add(0, 1, 0), e.getLocation().add(0,1,0));
					});
					main.addAll(tmp);
				});

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
