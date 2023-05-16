package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
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
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;

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
				
				loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar()).parallelStream().filter(e -> {
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
				}).forEach(e -> {
					if(RuneDamage.damageNormal(p, (LivingEntity) e, dr)) {
						((LivingEntity) e).addPotionEffect(effect);
					}
				});
				
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
				}
				
				loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar()).parallelStream().filter(e -> {
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
				}).forEach(e -> {
					int number = new Random().nextInt(100);
					if(number < 10) {
						e.setVelocity(new Vector(0, Math.random()*0.5+0.01, 0));
					}
				});
				
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
