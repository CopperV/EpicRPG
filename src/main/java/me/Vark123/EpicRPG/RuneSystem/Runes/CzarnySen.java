package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class CzarnySen extends ARune {

	private PotionEffect effect;
	
	public CzarnySen(ItemStackRune dr, Player p) {
		super(dr, p);
		effect =  new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 10);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1.75f, 0.6f);
		
		Location loc = p.getLocation().add(0, 0.1, 0);
		int points = 4 * dr.getObszar();
		double pointSpace = 2 * Math.PI / points;
		int radius = dr.getObszar();

		List<PointInfo> pointInfos = new LinkedList<>();
		for(double theta = 0; theta < 2*Math.PI; theta += pointSpace) {
			double x = Math.sin(theta) * radius;
			double z = Math.cos(theta) * radius;
			
			Location target = loc.clone().add(x,0,z);
			Vector vec = new Vector(loc.getX() - target.getX(),
					10,
					loc.getZ() - target.getZ()).normalize();
			
			pointInfos.add(new PointInfo(target, vec));
		}
		
		new BukkitRunnable() {
			int timer = 5*3;
			@Override
			public void run() {
				if(timer <= 0) {
					spellEffect(loc);
					cancel();
					return;
				}
				if(!casterInCastWorld()) {
					cancel();
					return;
				}
				if(isCancelled())
					return;
				
				pointInfos.forEach(info -> {
					Location location = info.getLoc();
					Vector vec = info.getVec();
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 1, 0, 0, 0, 0);
					p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 0, vec.getX(), vec.getY(), vec.getZ(), 0.18f);
				});
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
	}
	
	private void spellEffect(Location loc) {
		visualEffect(loc.clone().add(0, 0.9, 0));
		
		int radius = dr.getObszar();
		loc.getWorld().getNearbyEntities(loc, radius, radius, radius, e -> {
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
			if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
					&& e.getType().equals(EntityType.ARMOR_STAND))
				return false;
			if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
				return false;
			return true;
		}).forEach(this::entityEffect);
	}
	
	private void entityEffect(Entity e) {
		if(RuneDamage.damageNormal(p, (LivingEntity) e, dr, (p, le, dr) -> {
			le.addPotionEffect(effect);
			AbstractEntity ae = io.lumine.mythic.bukkit.BukkitAdapter.adapt(e);
			if(!ae.hasAI())
				return;
			
			ae.setAI(false);
			Location loc = io.lumine.mythic.bukkit.BukkitAdapter.adapt(ae.getEyeLocation());
			new BukkitRunnable() {
				int timer = 4 * dr.getDurationTime();
				@Override
				public void run() {
					if(e == null || ae == null) {
						cancel();
						return;
					}
					if(e.isDead() || ae.isDead()) {
						cancel();
						return;
					}
					if(timer <= 0) {
						ae.setAI(true);
						cancel();
						return;
					}
					if(isCancelled())
						return;
					
					loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 4, 0.2f, 0.2f, 0.2f, 0.03f);
					--timer;
				}
			}.runTaskTimer(Main.getInstance(), 0, 5);
		}));
	}
	
	private void visualEffect(Location loc) {
		p.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, 1f);
		p.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, .9f);
		p.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, .8f);
		p.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, .7f);
		p.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, .6f);
		p.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2f, .5f);
		new BukkitRunnable() {
			int radius = 1;
			@Override
			public void run() {
				if(radius > dr.getObszar()) {
					cancel();
					return;
				}
				if(isCancelled())
					return;
				
				int points = 4 * radius;
				double space = 2 * Math.PI / points;
				for(double theta = 0; theta < 2*Math.PI; theta += space) {
					double x = Math.sin(theta) * radius;
					double z = Math.cos(theta) * radius;
					
					Location target = loc.clone().add(x,0,z);
					target.getWorld().spawnParticle(Particle.SMOKE_LARGE, target, 3, 0.2f, 0.8f, 0.2f, 0.04f);
				}
				
				++radius;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	@AllArgsConstructor
	@Getter
	private class PointInfo {
		private Location loc;
		private Vector vec;
	}

}
