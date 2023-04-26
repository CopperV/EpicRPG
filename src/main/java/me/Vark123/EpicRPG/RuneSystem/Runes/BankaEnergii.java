package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

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

public class BankaEnergii extends ARune {
	
//	Set<Entity> rangedEntities = new HashSet<>();
	
	public BankaEnergii(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location circle = p.getLocation().clone().add(0,0.2d,0);
		Location loc = p.getLocation().clone().add(0, 2, 0);
		loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1, 0.4F);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc,10,0.5F,0.5F,0.5F,0.1F);
				
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/(dr.getObszar()*2))) {
					double x = dr.getObszar()*Math.sin(theta);
					double z = dr.getObszar()*Math.cos(theta);
					Location tmp = new Location(circle.getWorld(), circle.getX()+x, circle.getY(), circle.getZ()+z);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, tmp, 2, 0.05F, 0.05F, 0.05F, 0.05F);
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime();
			PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 20, 1);
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}

				Collection<Entity> entities = loc.getWorld().getNearbyEntities(circle, dr.getObszar(), dr.getObszar(), dr.getObszar());
				Set<Entity> tmp = new HashSet<>();

//				rangedEntities.addAll(entities);
//				Bukkit.broadcastMessage(entities.isEmpty()+" Empty?");
//				if(!entities.isEmpty())
//					Bukkit.broadcastMessage(entities.toString());
				for(Entity entity : entities) {
					if(!entity.equals(p) && entity instanceof LivingEntity) {
						if(entity instanceof Player || entity.hasMetadata("NPC")) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
							if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon")) {
//								entities.remove(entity);
								tmp.add(entity);
								continue;
							}
						}
						p.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, entity.getLocation().add(0, 1, 0),10,0.3F,0.3F,0.3F,0.05F);
						if(RuneDamage.damageTiming(p, (LivingEntity) entity, dr)) {
							((LivingEntity)entity).addPotionEffect(effect);
						}else {
							tmp.add(entity);
							continue;
						}
					}else {
						tmp.add(entity);
						continue;
					}
				}
				
				if(entities.size()>tmp.size()) {
					p.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, loc,20,0.5F,0.5F,0.5F,0.1F);
					int number = new Random().nextInt(2)+1;
					String sound = "ITEM_TRIDENT_RIPTIDE_"+number;
					loc.getWorld().playSound(loc, Sound.valueOf(sound), 2, 1.2F);
				}
				
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
	}

}
