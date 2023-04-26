package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
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

public class Lowy_H extends ARune {

	public Lowy_H(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1, 1.5f);
		preparation(p.getLocation().add(0, 0.25, 0));
		
		Vector direction = p.getLocation().getDirection().normalize().multiply(1);
		Vector direction2 = p.getLocation().getDirection().normalize().multiply(-1);
		Location loc = p.getLocation().add(0, 1.25, 0);
		Location loc2 = p.getLocation().add(0, 1.25, 0);
		double gravity = 0.04;
		
//		Bukkit.broadcastMessage("§aPoczatkowa lokalizacja: §e"+loc.toString());
		
		new BukkitRunnable() {
			
			DustOptions dust = new DustOptions(Color.fromRGB(0, 128, 0), 2);
			
			@Override
			public void run() {
				if(loc.getBlock().getType().isSolid() || !casterInCastWorld()) {
					loc.setY(Math.ceil(loc.getY())+0.25);
					preparation(loc);
//					Bukkit.broadcastMessage("§4Koncowa lokalizacja: §6"+loc.toString());
					this.cancel();
					return;
				}

				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 5,0.1F,0.1F,0.1F,0.05F, dust);
				
				loc.add(direction);
				direction.setY(direction.getY()-gravity);
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
		
		new BukkitRunnable() {
			
			DustOptions dust = new DustOptions(Color.fromRGB(0, 128, 0), 2);
			
			@Override
			public void run() {
				if(loc2.getBlock().getType().isSolid() || !casterInCastWorld()) {
					loc2.setY(Math.ceil(loc2.getY())+0.25);
					preparation(loc2);
//					Bukkit.broadcastMessage("§4Koncowa lokalizacja: §6"+loc.toString());
					this.cancel();
					return;
				}

				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 5,0.1F,0.1F,0.1F,0.05F, dust);
				
				loc2.add(direction2);
				direction2.setY(direction2.getY()-gravity);
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
		
	}
	
	private void preparation(Location loc) {
		new BukkitRunnable() {
			DustOptions dust = new DustOptions(Color.fromRGB(0, 128, 0), 2);
			double timer = 40*10;
			LivingEntity le;
			@Override
			public void run() {
				
				for(Entity entity : loc.getWorld().getNearbyEntities(loc, 2, 5, 2)){
					AxisAlignedBB aabb = ((CraftEntity)entity).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.75, loc.getY()-3, loc.getZ()-0.75, loc.getX()+0.75, loc.getY()+3, loc.getZ()+0.75);
					if(aabb.c(aabb2)) {
						if(!entity.equals(p) && entity instanceof LivingEntity) {
							if(entity instanceof Player || entity.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set1 = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
								ApplicableRegionSet set2 = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
								if(set1.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY)
										|| set2.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY))
									continue;
							}
							le = (LivingEntity) entity;
							if(RuneDamage.damageNormal(p, le, dr, (p,le,dr)->{
								p.getWorld().playSound(le.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1, .5f);
								le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 7));
								le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*dr.getDurationTime(), 7));
								le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*dr.getDurationTime(), 7));
								trapEffect(le.getLocation().add(0, 1, 0));
							})) {
								this.cancel();
								return;
							}
						}
					}
				}
				
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				
				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 3,0.2F,0.2F,0.2F,0.1F, dust);
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}
	
	private void trapEffect(Location loc) {
		new BukkitRunnable() {
			DustOptions dust = new DustOptions(Color.fromRGB(0, 128, 0), 2);
			double timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 8,0.2F,0.2F,0.2F,0.1F, dust);
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
