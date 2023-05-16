package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
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
import net.minecraft.world.phys.AxisAlignedBB;

public class Rezonans extends ARune {

	public Rezonans(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation();
		p.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 2f, 1.4f);
		spellEffect(loc.clone(), 0, Particle.FIREWORKS_SPARK);
		spellEffect(loc.clone(), Math.PI/2, Particle.FLAME);
		spellEffect(loc.clone(), Math.PI, Particle.ENCHANTMENT_TABLE);
		spellEffect(loc.clone(), (3*Math.PI)/2, Particle.END_ROD);
	}

	public void spellEffect(Location loc, final double angle, Particle particle) {
		loc.add(0, 1, 0);
		new BukkitRunnable() {
			
			Location check = loc.clone();
			double theta = angle;
			double progress = (Math.PI)/16;
			double t = 0.5;
			
			@Override
			public void run() {
				double x = t*Math.sin(theta);
				double z = t*Math.cos(theta);
				check.add(x,0,z);
				
				if(particle.equals(Particle.FALLING_DUST)) {
					p.getWorld().spawnParticle(particle, check, 10, 0.2F, 0.2F, 0.2F, 0.02F,Bukkit.createBlockData(Material.DIRT));
				}
				else p.getWorld().spawnParticle(particle, check, 10, 0.2F, 0.2F, 0.2F, 0.02F);
					
				check.getWorld().getNearbyEntities(check, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(check.getX()-0.75, check.getY()-1, check.getZ()-0.75, check.getX()+0.75, check.getY()+1, check.getZ()+0.75);
					if(!aabb.c(aabb2))
						return false;
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
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).parallelStream().min((e1, e2) -> {
					double dist1 = e1.getLocation().distanceSquared(check);
					double dist2 = e2.getLocation().distanceSquared(check);
					if(dist1 == dist2)
						return 0;
					return dist1 < dist2 ? -1 : 1;
				}).ifPresent(e -> {
					RuneDamage.damageNormal(p, (LivingEntity) e, dr);
					p.getWorld().spawnParticle(particle, check, 45, 0.4F, 0.4F, 0.4F, 0.15F);
					check.getWorld().playSound(e.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 1, 1.7f);
					cancel();
				});
				
				if(this.isCancelled()) {
					return;
				}
				
				t += 0.1;
				theta += progress;
				
				if(check.distance(loc)>dr.getObszar() || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				check.subtract(x,0,z);
			}
			
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}
	
}
