package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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

public class FalaMrozu extends ARune {
	
	private List<Entity> shooted;

	public FalaMrozu(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation();
		Location check = p.getLocation();
		shooted = new ArrayList<Entity>();
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 0.8f);
		PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 2);
		
		new BukkitRunnable() {
			double t = 0;
			LivingEntity le;
			@Override
			public void run() {
				t+=0.25;
				if(t>=dr.getObszar() || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x, 0.25, z);
					p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 3, 0.1F, 0.1F, 0.1F, 0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 3, 0.1F, 0.1F, 0.1F, 0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 3, 0.1F, 0.1F, 0.1F, 0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 3, 0.1F, 0.1F, 0.1F, 0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 3, 0.1F, 0.1F, 0.1F, 0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 3, 0.1F, 0.1F, 0.1F, 0.05F);
					loc.subtract(x,1.5,z);
				}
				for(Entity entity : check.getWorld().getNearbyEntities(check, t, t, t)) {
//					AxisAlignedBB aabb = ((CraftEntity)entity).getHandle().cw();
//					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1, loc.getZ()-1, loc.getX()+1, loc.getY()+3, loc.getZ()+1);
					if(entity.getLocation().distance(check) <= t) {
						if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(loc) <= dr.getObszar()) {
							shooted.add(entity);
							if(entity instanceof Player || entity.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
							le = (LivingEntity) entity;
							RuneDamage.damageNormal(p, le, dr, (p,le,dr)->{
								le.addPotionEffect(pe);
								le.getLocation().getWorld().playSound(le.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 0.8f);
								Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()->{
									new BukkitRunnable() {
										double t = 0;
										@Override
										public void run() {
											
											if(t>=dr.getDurationTime() || !casterInCastWorld() || !entityInCastWorld(entity)) {
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
											le.getLocation().getWorld().playSound(le.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 0.5f);
											loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 3, 0.2, 0.2, 0.2, 0.1);
											loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc.add(0,1,0), 3, 0.2, 0.2, 0.2, 0.1);
											
											
										}
									}.runTaskTimer(Main.getInstance(), 0, 20);
								}, 20);
							});
							entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.ENTITY_HOSTILE_BIG_FALL, 1, .4f);
						}
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
		
	}

}
