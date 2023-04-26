package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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

public class Meteor extends ARune {
	
	private List<Entity> shooted;

	public Meteor(ItemStackRune dr, Player p) {
		super(dr, p);
		shooted = new ArrayList<Entity>();
	}

	@Override
	public void castSpell() {
		Location loc = p.getTargetBlock((Set<Material>)null, 40).getLocation();
		loc.add(0, 30, 0);
		new BukkitRunnable() {
			double t = 30;
			@Override
			public void run() {
				loc.add(0, t, 0);
				loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
				p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 1, 0, 0, 0, 0);
				p.getWorld().spawnParticle(Particle.CLOUD, loc, 10, 0.75F, 0.75F, 0.75F, 0);
				if(!loc.getBlock().getType().equals(Material.AIR) || !casterInCastWorld()) {
					spellEffect(loc.clone());
					this.cancel();
					return;
				}
				loc.subtract(0, t, 0);
				t--;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void spellEffect(Location loc) {
		Location check = loc.clone();
		new BukkitRunnable() {
			double t = 0;
//			double dmg = dr.getDamage();
//			RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
			@Override
			public void run() {
				t++;
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x, 1, z);
					loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
						this.cancel();
						return;
					}
					p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1, 0, 0, 0, 0);
					
					for(Entity entity:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
						AxisAlignedBB aabb = ((CraftEntity)entity).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-4, loc.getZ()-1, loc.getX()+1, loc.getY()+4, loc.getZ()+1);
						if(aabb.c(aabb2)) {
							if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(check) <= dr.getObszar()) {
								if(entity instanceof Player || entity.hasMetadata("NPC")) {
									RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
									ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
									if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
										continue;
								}
//								if(rpg.hasInkantacja()) dmg *= 1.3;
								RuneDamage.damageNormal(p, (LivingEntity)entity, dr);
								shooted.add(entity);
							}
						}
					}
					
//					for(Entity entity:loc.getChunk().getEntities()) {
//						if(entity.getLocation().distance(loc) < 1 || entity.getLocation().add(0, 1, 0).distance(loc) < 1 || entity.getLocation().add(0, 1, 0).distance(loc) < 1) {
//							if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(check) <= dr.getObszar()) {
//								if(entity instanceof Player || entity.hasMetadata("NPC")) {
//									ApplicableRegionSet set = WorldGuardPlugin.inst().getRegionManager(entity.getWorld()).getApplicableRegions(entity.getLocation());
//									if(set.queryState(null, DefaultFlag.PVP) == State.DENY) 
//										continue;
//								}
//								((LivingEntity)entity).setLastDamageCause(new EntityDamageEvent(p, DamageCause.CUSTOM, dr.getDamage()));
//								((LivingEntity)entity).damage(dr.getDamage(), p);
//								shooted.add(entity);
//							}
//						}
//					}
					loc.subtract(x,1,z);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

}
