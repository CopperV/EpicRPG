package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
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
import net.minecraft.world.phys.AxisAlignedBB;

public class Spetanie extends ARune {
	
	private List<Entity> shooted;

	public Spetanie(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		shooted = new ArrayList<Entity>();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, .1f);
		new BukkitRunnable() {
			Location loc = p.getLocation();
			Location check = p.getLocation();
			double t = 0;
			PotionEffect stun = new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 7);
			PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 1);
			@Override
			public void run() {
				t+=0.65;
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x, 0.25, z);
					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
						p.addPotionEffect(slow);
						this.cancel();
						return;
					}
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					
					for(Entity entity:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
						AxisAlignedBB aabb = ((CraftEntity)entity).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.75, loc.getY()-0.75, loc.getZ()-0.75, loc.getX()+0.75, loc.getY()+4, loc.getZ()+0.75);
						if(aabb.c(aabb2)) {
							if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(check) <= dr.getObszar()) {
								if(entity instanceof Player || entity.hasMetadata("NPC")) {
									RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
									ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
									if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon")) {
										((LivingEntity)entity).addPotionEffect(slow);
										continue;
									}
								}
								((LivingEntity)entity).addPotionEffect(stun);
								if(RuneDamage.damageNormal(p, (LivingEntity)entity, dr)) {
									loc.getWorld().playSound(loc, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1);
									shooted.add(entity);
									spellEffect(entity);
								}
							}
						}
					}
					loc.subtract(x,1.5,z);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}
	
	private void spellEffect(Entity e) {
		new BukkitRunnable() {
			double timer = dr.getDurationTime()*4;
			double _45deg = Math.PI/4.0;
			double r = 0.75;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				if(e.isDead()) {
					this.cancel();
					return;
				}
				Location loc = e.getLocation().add(0, 0.75, 0);
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
					double x = r*Math.cos(theta)*Math.cos(_45deg);
					double z = r*Math.cos(theta)*Math.sin(_45deg);
					double y = r*Math.sin(theta);
					loc.add(x, y, z);
					p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 1,0,0,0,0);
					loc.subtract(x,y,z);
				}
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
					double x = r*Math.cos(theta)*Math.cos(-_45deg);
					double z = r*Math.cos(theta)*Math.sin(-_45deg);
					double y = r*Math.sin(theta);
					loc.add(x, y, z);
					p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 1,0,0,0,0);
					loc.subtract(x,y,z);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
