package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class Zacma extends ARune {

	private List<Entity> shooted;
	
	public Zacma(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		shooted = new ArrayList<>();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CREEPER_HURT, 3, 0.5f);
		new BukkitRunnable() {
			
			Location loc = p.getLocation();
			Location check = p.getLocation();
			double t = 0.5;
			
			@Override
			public void run() {
				
				double progress = Math.PI/(2*t);
				
				for(double theta = 0; theta <= (Math.PI*2); theta+=progress) {
					
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x,0,z);
					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
						this.cancel();
						return;
					}
					loc.add(0, 0.75, 0);
					
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 5, 0.1F, 0.1F, 0.1F, 0.05F, Bukkit.createBlockData(Material.DIRT));
					
					for(Entity entity:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
						AxisAlignedBB aabb = ((CraftEntity)entity).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.75, loc.getY()-2, loc.getZ()-0.75, loc.getX()+0.75, loc.getY()+2, loc.getZ()+0.75);
						if(aabb.c(aabb2)) {
							if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity) && entity.getLocation().distance(check) <= dr.getObszar()) {
								if(entity instanceof Player || entity.hasMetadata("NPC")) {
									RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
									ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
									if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
										continue;
								}
								RuneDamage.damageNormal(p, (LivingEntity)entity, dr);
								((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 2));
								((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*dr.getDurationTime(), 2));
								entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_CREEPER_DEATH, 1, 0.3f);
								shooted.add(entity);
							}
						}
					}
					
					loc.subtract(x, 0.75, z);
				}
				
				t+=0.5;
					
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

}
