package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
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
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;

public class SzeptPrzedwiecznych extends ARune {
	
	List<Entity> effected;

	public SzeptPrzedwiecznych(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		effected = new ArrayList<>();
		Location loc = p.getLocation().clone().add(0,0.2d,0);
		loc.getWorld().playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1, 1);
		DustOptions dust = new DustOptions(Color.PURPLE, 2.5f);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime();
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					effected.clear();
					this.cancel();
					return;
				}
				
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				for(Entity e : entities) {
					if(!e.equals(p) && e instanceof LivingEntity) {
						if(e instanceof Player || e.hasMetadata("NPC")) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
							if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
								continue;
						}
						RuneDamage.damageNormal(p, (LivingEntity)e, dr);
						if(e instanceof ArmorStand) continue;
						if(e instanceof AbstractHorse && ((AbstractHorse)e).getOwner() instanceof Player)
							continue;

						if(effected.contains(e)) continue;
						effected.add(e);
						addEffect(e, loc, timer);
					}
				}
				
				--timer;
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
								
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/(dr.getObszar()*2))) {
					double x = dr.getObszar()*Math.sin(theta);
					double z = dr.getObszar()*Math.cos(theta);
					Location tmp = new Location(loc.getWorld(), loc.getX()+x, loc.getY(), loc.getZ()+z);
					p.getWorld().spawnParticle(Particle.REDSTONE, tmp, 2, 0.15F, 0.15F, 0.15F, 0.1F, dust);
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}
	
	private void addEffect(Entity entity, Location loc, int time) {
		int bounds = 5;
		bounds = new Random().nextInt(bounds);
		switch(bounds) {
			case 0:
				new BukkitRunnable() {
					int timer = time;
					@Override
					public void run() {
						if(timer <= 0 || !casterInCastWorld()) {
							this.cancel();
							return;
						}
	
						if(entity.isDead()) {
							this.cancel();
							return;
						}
						
						if(Math.abs(entity.getLocation().distance(loc)) > dr.getObszar())
							RuneDamage.damageNormal(p, (LivingEntity)entity, dr, dr.getDamage() * 3);
						--timer;
					}
				}.runTaskTimer(Main.getInstance(), 0, 20);
				break;
			case 1:
				new BukkitRunnable() {
					int timer = time;
					PotionEffect effect = new PotionEffect(PotionEffectType.LEVITATION, 15, 1);
					@Override
					public void run() {
						if(timer <= 0 || !casterInCastWorld()) {
							this.cancel();
							return;
						}
	
						if(entity.isDead()) {
							this.cancel();
							return;
						}
						((LivingEntity)entity).addPotionEffect(effect);				
						--timer;
					}
				}.runTaskTimer(Main.getInstance(), 0, 20);
				break;
			case 2:
				PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 20*time, 1);
				((LivingEntity)entity).addPotionEffect(effect);	
				break;
			case 3:
				new BukkitRunnable() {
					int timer = time;
					DustOptions dust = new DustOptions(Color.PURPLE, 1);
					@Override
					public void run() {
						if(timer <= 0 || !casterInCastWorld()) {
							this.cancel();
							return;
						}
						p.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 20, 0.8F, 0.8F, 0.8F, 0.2F, dust);		
						loc.getWorld().playSound(loc, Sound.ENTITY_GUARDIAN_DEATH, 1, 1);
						
						Collection<Entity> entities = loc.getWorld().getNearbyEntities(entity.getLocation(), 2,2,2);
						for(Entity en : entities) {
							if(!en.equals(p) && en instanceof LivingEntity) {
								if(en instanceof Player || en.hasMetadata("NPC")) {
									RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
									ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(en.getLocation()));
									if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
										continue;
								}	
								RuneDamage.damageNormal(p, (LivingEntity) en, dr, dr.getDamage()/2);
							}
						}

						--timer;
						
					}
				}.runTaskTimer(Main.getInstance(), 0, 20);
				break;
			case 4:
				new BukkitRunnable() {
					int timer = time*4;
					@Override
					public void run() {
						if(timer <= 0 || !casterInCastWorld()) {
							this.cancel();
							return;
						}
						
						Location loc2 = entity.getLocation();
						if(Math.abs(loc2.distance(loc)) <= dr.getObszar()) {
							if(!(entity instanceof ArmorStand)) {
								p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, entity.getLocation(), 25, 0.7F, 0.7F, 0.7F, 0.2F);		
								loc.getWorld().playSound(loc, Sound.ENTITY_GUARDIAN_HURT, 1, 1);
								Vector vec = new Vector(loc2.getX()-loc.getX(),0,loc2.getZ()-loc.getZ()).normalize().setY((Math.abs(Math.abs(loc2.distance(loc))-dr.getObszar())+0.5)/2).multiply(3);
								entity.setVelocity(vec);
							}
							
						}

						--timer;
					}
				}.runTaskTimer(Main.getInstance(), 0, 5);;
				break;
		}
	}

}
