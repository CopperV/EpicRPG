package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class WiecznyOgien extends ARune {

	public WiecznyOgien(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation();
		loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 0.7F);
		double radius = dr.getObszar();
		Collection<Location> locs = p.getWorld().getNearbyEntities(loc, radius, radius, radius, e -> {
			if(e.getLocation().distance(loc) > radius)
				return false;
			if(e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
					&& e.getType().equals(EntityType.ARMOR_STAND))
				return false;
			if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
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
			return true;
		}).stream()
			.map(e -> e.getLocation().clone().add(0, 0.1, 0))
			.collect(Collectors.toSet());
		
		locs.forEach(this::spellEffect);
	}
	
	private void spellEffect(Location loc) {
		new BukkitRunnable() {
			int timer = dr.getDurationTime() * 4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				for(double r = 0.5; r <= 3; r += 0.5) {
					for(double theta = 0; theta < Math.PI * 2; theta += Math.PI / (r*2)) {
						double x = r*Math.sin(theta);
						double z = r*Math.cos(theta);
						Location tmp = loc.clone().add(x,0,z);
						p.getWorld().spawnParticle(Particle.FLAME, tmp, 1, 0.05f, 0.05f, 0.05f, 0.02f);
					}
				}
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime();
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
					if(e.getLocation().distance(loc) > 3)
						return false;
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
							&& e.getType().equals(EntityType.ARMOR_STAND))
						return false;
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
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
					return true;
				}).forEach(e -> {
					loc.getWorld().spawnParticle(Particle.SMALL_FLAME, e.getLocation().clone().add(0,1,0),
							9, .6f, .6f, .6f, .04f);
					loc.getWorld().playSound(e.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1, 0.7F);
					RuneDamage.damageNormal(p, (LivingEntity) e, dr);
				});
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

}
