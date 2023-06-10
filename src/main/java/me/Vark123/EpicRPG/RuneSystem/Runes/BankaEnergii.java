package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class BankaEnergii extends ARune {
	
	public BankaEnergii(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location circle = p.getLocation().clone().add(0,0.2d,0);
		Location loc = p.getLocation().clone();
		loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1, 0.4F);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			Location mid = loc.clone().add(0, 2, 0);
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, mid,10,0.5F,0.5F,0.5F,0.1F);
				
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
				List<Entity> tmp = new LinkedList<>();

				entities.stream().filter(e -> {
					if(e.equals(p) || !(e instanceof LivingEntity)){
						tmp.add(e);
						return false;
					}
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable()){
						tmp.add(e);
						return false;
					}
					if(e instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !e.getWorld().getName().toLowerCase().contains("dungeon")){
							tmp.add(e);
							return true;
						}
						return false;
					}
					if(e.getLocation().distance(loc) > dr.getObszar()) {
						tmp.add(e);
						return false;
					}
					return true;
				}).forEach(e -> {
					e.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, e.getLocation().add(0, 1, 0),10,0.3F,0.3F,0.3F,0.05F);
					if(RuneDamage.damageTiming(p, (LivingEntity) e, dr)) {
						((LivingEntity)e).addPotionEffect(effect);
					} else {
						tmp.add(e);
					}
				});
				
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
