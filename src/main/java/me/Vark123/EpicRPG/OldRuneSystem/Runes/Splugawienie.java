package me.Vark123.EpicRPG.OldRuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
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
import me.Vark123.EpicRPG.OldFightSystem.RuneDamage;
import me.Vark123.EpicRPG.OldRuneSystem.ARune;
import me.Vark123.EpicRPG.OldRuneSystem.ItemStackRune;

public class Splugawienie extends ARune {
	
	private static final Random rand = new Random();
	List<Player> defense;

	public Splugawienie(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Map<UUID, Double> mobModifiers = new ConcurrentHashMap<>();
		double maxRadius = dr.getObszar() * 10;
		MutableDouble radius = new MutableDouble(dr.getObszar());

		Location loc = p.getLocation().clone().add(0,0.1,0);
		loc.getWorld().playSound(loc, Sound.ENTITY_MAGMA_CUBE_JUMP, 1.5f, 0.65f);
		
		defense = new ArrayList<>();
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*2;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				if(isCancelled())
					return;

				double _radius = radius.getValue();
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, _radius, _radius, _radius);
				entities.stream().filter(e -> {
					if(e.getLocation().distanceSquared(loc) > (_radius * _radius))
						return false;
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(e instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !(e.getWorld().getName().toLowerCase().contains("dungeon") || e.getWorld().getName().toLowerCase().contains("raid")))
							return true;
						return false;
					}
					if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
							&& e.getType().equals(EntityType.ARMOR_STAND))
						return false;
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).forEach(e -> {
					radius.add(0.5);
					double modifier = mobModifiers.getOrDefault(e.getUniqueId(), 1.);
					
					e.getWorld().playSound(e.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, .9f, 1.6f);
					e.getWorld().spawnParticle(Particle.SMOKE, e.getLocation().clone().add(0,1,0), 8, 0.45f, 1f, 0.45f, 0.02f);

					RuneDamage.damageNormal(p, (LivingEntity)e, dr, modifier * dr.getDamage());
					
					mobModifiers.put(e.getUniqueId(), modifier + 0.025);
				});
				
				radius.setValue(Math.min(maxRadius, radius.getValue()));
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				double _radius = radius.getValue();
				int outerPoints = (int) (3 * _radius);
				int innerPoints = (int) (4 * _radius * Math.log(_radius));
				double angleOffset = rand.nextDouble(Math.PI * 2);
				double angleStep = Math.PI*2 / (double) outerPoints;
				
				for(int i = 0; i < outerPoints; i++) {
					double angle = angleStep * i + angleOffset;
					double x = _radius * Math.sin(angle);
					double z = _radius * Math.cos(angle);
					Location tmp = loc.clone().add(x, 0, z);
					p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, tmp, 1, 0.05f, 0.05f, 0.05f, 0.02f);
				}
				
				for(int i = 0; i < innerPoints; i++) {
					double r = rand.nextDouble(_radius);
					double angle = rand.nextDouble(Math.PI * 2);
					double x = r * Math.sin(angle);
					double z = r * Math.cos(angle);
					Location tmp = loc.clone().add(x, 0, z);
					p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, tmp, 1, 0.05f, 0.05f, 0.05f, 0.02f);
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
	}

}
