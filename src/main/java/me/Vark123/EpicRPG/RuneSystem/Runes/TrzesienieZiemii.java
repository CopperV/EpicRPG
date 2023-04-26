package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
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

public class TrzesienieZiemii extends ARune {

	public TrzesienieZiemii(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		
		Location loc = p.getLocation().clone().add(0,0.3d,0);
		
		//Damage
		new BukkitRunnable() {
			int timer = dr.getDurationTime();
			PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 20, 1);
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				for(Entity entity : loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar())) {
					if(!entity.equals(p) && entity instanceof LivingEntity) {
						if(entity instanceof Player || entity.hasMetadata("NPC")) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
							if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
								continue;
						}	
						if(RuneDamage.damageNormal(p, (LivingEntity) entity, dr)) {
							((LivingEntity)entity).addPotionEffect(effect);
						}
					}
				}
				
				--timer;
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		//Effect
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			BlockData data = Bukkit.createBlockData(Material.DIRT);
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				int points = dr.getObszar()*3;
				for(int i = 0; i < points; ++i) {
					double r = Math.random() * dr.getObszar();
					double theta = Math.random() * Math.PI * 2;
					double x = loc.getX() + r * Math.sin(theta);
					double z = loc.getZ() + r * Math.cos(theta);
					Location tmp = new Location(loc.getWorld(), x, loc.getY(), z);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp,3,0.2F,0.05F,0.2F,0.02F,data);
					r = Math.random() * dr.getObszar();
					theta = Math.random() * Math.PI * 2;
					x = loc.getX() + r * Math.sin(theta);
					z = loc.getZ() + r * Math.cos(theta);
//					Block block = loc.getWorld().getHighestBlockAt((int)x, (int)z);
//					ItemStack it = new ItemStack(block.getType(),1);
//					p.getWorld().spawnParticle(Particle.ITEM_CRACK, block.getLocation().add(0.5,0,0.5),4,0.3F,0.3F,0.3F,0.1F, it);
				}
				
				for(Entity entity : loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar())) {
					
					if(!entity.equals(p) && entity instanceof LivingEntity) {
						if(entity instanceof Player || entity.hasMetadata("NPC")) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
							if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
								continue;
						}
						
						int number = new Random().nextInt(100);
						if(number < 10) {
							entity.setVelocity(new Vector(0, Math.random()*0.5+0.01, 0));
						}
						
					}
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
		//Sound
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*10;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				loc.getWorld().playSound(loc, Sound.ENTITY_HUSK_STEP, 4, 0.1f);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
		
	}

}
