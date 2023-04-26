package me.Vark123.EpicRPG.RuneSystem.Runes;

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
import net.minecraft.world.phys.AxisAlignedBB;

public class Korzen extends ARune{

	public Korzen(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 3, 0.35f);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			public void run() {
				if(t>40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				t+=0.35;
				double x = vec.getX()*t;
				double y = vec.getY()*t+1;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc,40,0.4F,0.4F,0.4F,0.02F, Bukkit.createBlockData(Material.DIRT));
//				p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc,10,0.5F,0.5F,0.5F,0.05F);
//				p.getWorld().spawnParticle(Particle.SPELL_INSTANT, loc,10,0.1F,0.1F,0.1F,0.01F);
//				loc.getWorld().playSound(loc, Sound.FIRE_IGNITE, 1, 1);
//				for(Entity e:loc.getChunk().getEntities()) {
//					if(e.getLocation().distance(loc) < 1.0 || e.getLocation().add(0, 1, 0).distance(loc) < 1.0) {
//						if(!e.equals(p) && e instanceof LivingEntity) {
//							zadajDamage(e);
//							this.cancel();
//							return; 
//						}
//					}
//				}
				for(Entity e : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.75, loc.getY()-0.75, loc.getZ()-0.75, loc.getX()+0.75, loc.getY()+0.75, loc.getZ()+0.75);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}	
							zadajDamage(e);
							this.cancel();
							return;
						}
					}
				}

				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
//				if(!loc.getBlock().getType().equals(Material.AIR)) {
					this.cancel();
					return;
				}
				if(t>40) {
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void zadajDamage(Entity e) {
		((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 10));
		RuneDamage.damageNormal(p, (LivingEntity)e, dr);
		new BukkitRunnable() {
			double t = 0;
			@Override
			public void run() {
				if(t>=dr.getDurationTime() || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				if(!e.isDead()) {
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, e.getLocation().add(0,0.25,0),40,0.3F,0.2F,0.3F,0.05f,Bukkit.createBlockData(Material.DIRT));
//					p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, e.getLocation().add(0,0.5,0),15,0.5F,0.5F,0.5F,0.05f);
//					p.getWorld().spawnParticle(Particle.SPELL_INSTANT, e.getLocation().add(0,0.5,0),15,0.5F,0.5F,0.5F,0.05f);
				}
				t+=0.5;
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
	}

}
