package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;

public class PlugawaKrew extends ARune {
	
	private List<Entity> blooded = new ArrayList<>();

	public PlugawaKrew(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation().clone().add(0,0.2d,0);
		loc.getWorld().playSound(loc, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1, 1.2f);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime();
			LivingEntity le;
			@Override
			public void run() {
				
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				
				entities.parallelStream().filter(e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(blooded.contains(e))
						return false;
					if(e instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
							return false;
					}
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).forEach(e -> {
					blooded.add(e);
					e.getLocation().getWorld().playSound(e.getLocation(), Sound.ENTITY_HOSTILE_BIG_FALL, 1, .4f);
					le = (LivingEntity) e;
					RuneDamage.damageNormal(p, le, dr, (p,le,dr)->{
						Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()->{
							new BukkitRunnable() {
								double t = 1;
								DustOptions dust = new DustOptions(Color.fromRGB(154, 67, 43), .75f);
								@Override
								public void run() {
									
									if(t>9|| !casterInCastWorld() || !entityInCastWorld(e)) {
										this.cancel();
										return;
									}
									++t;
									
									if(le.isDead()) {
										this.cancel();
										return;
									}
									
									if(!RuneDamage.damageTiming(p, le, dr)) {
										this.cancel();
										return;
									}
									Location loc = le.getLocation().clone().add(0, 1, 0);
									loc.getWorld().playSound(loc, Sound.ENTITY_SLIME_ATTACK, 1, 1.2f);
									loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 5, 0.2, 0.2, 0.2, 0.1, dust);
									
									
								}
							}.runTaskTimer(Main.getInstance(), 0, 20);
						}, 20);
					});
				});
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			DustOptions dust = new DustOptions(Color.fromRGB(154, 67, 3), 0.85f);
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
					p.getWorld().spawnParticle(Particle.REDSTONE, tmp, 3, 0.15F, 0.15F, 0.15F, 0.1F, dust);
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
