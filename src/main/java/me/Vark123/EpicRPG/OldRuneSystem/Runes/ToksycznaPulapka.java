package me.Vark123.EpicRPG.OldRuneSystem.Runes;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.OldFightSystem.RuneDamage;
import me.Vark123.EpicRPG.OldRuneSystem.ARune;
import me.Vark123.EpicRPG.OldRuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;

public class ToksycznaPulapka extends ARune {
	
	private static final Random rand = new Random();
	private static final DustOptions dustData = new DustOptions(Color.fromRGB(0, 192, 0), 1.15f);
	private static final BlockData chainData = Material.CHAIN.createBlockData();
	private static final PotionEffect effect = new PotionEffect(PotionEffectType.SLOWNESS, 20*12, 1);
	
	public ToksycznaPulapka(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 0.9f, 0.8f);

		Location loc = p.getLocation().clone().add(0, 0.1, 0);
		int duration = 24;
		double radius = dr.getObszar();
		
		new BukkitRunnable() {
			int timer = duration * 10;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				if(this.isCancelled()) {
					return;
				}
				--timer;
				
				int points = (int) (4 * radius * Math.log(radius));
				for(int i = 0; i < points * 2; ++i) {
					double r = rand.nextDouble(radius);
					double a = rand.nextDouble(Math.PI*2);
					double x = r * Math.sin(a);
					double z = r * Math.cos(a);
					Location tmp = loc.clone().add(x,0,z);
					if(i % 2 == 0)
						loc.getWorld().spawnParticle(Particle.DUST, tmp, 1, 0.05f, 0.05f, 0.05f, 0.02f, dustData);
					else
						loc.getWorld().spawnParticle(Particle.BLOCK, tmp, 1, 0.05f, 0.05f, 0.05f, 0.02f, chainData);
				}
				
				loc.getWorld().getNearbyEntities(loc, radius, radius, radius, e -> {
					if(e.getLocation().distanceSquared(loc) > (radius * radius))
						return false;
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(e instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !(e.getWorld().getName().toLowerCase().contains("dungeon") || e.getWorld().getName().toLowerCase().contains("raid")))
							return true;
						return false;
					}
					if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
							&& e.getType().equals(EntityType.ARMOR_STAND))
						return false;
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).stream().findAny().ifPresent(e -> {
					trapEffect((LivingEntity) e);
					cancel();
				});
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
		
	}
	
	private void trapEffect(LivingEntity victim) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		
		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 0.9f, 0.9f);
		
		{
			double explosionModifier = 1. + stats.getFinalZdolnosciMysliwskie() / 50. + stats.getFinalZrecznosc() / 90.;
			Location vLoc = victim.getLocation().clone();
			int radius = dr.getObszar() * 3;
			
			int points = (int) (16 * radius * Math.log(radius));
			for(int i = 0; i < points; ++i) {
				double r = rand.nextDouble(radius);
				double a1 = rand.nextDouble(Math.PI*2);
				double a2 = rand.nextDouble(Math.PI*2);
				
				double x = r * Math.sin(a1) * Math.sin(a2);
				double y = r * Math.sin(a2);
				double z = r * Math.cos(a1) * Math.sin(a2);
				
				Location tmp = vLoc.clone().add(0,0.5,0).add(x,y,z);
				tmp.getWorld().spawnParticle(Particle.DUST, tmp, 1, 0.05f, 0.05f, 0.05f, 0.1f, dustData);
			}
			
			vLoc.getWorld().getNearbyEntities(vLoc, radius, radius, radius, e -> {
				if(e.getLocation().distanceSquared(vLoc) > (radius * radius))
					return false;
				if(e.equals(p) || !(e instanceof LivingEntity))
					return false;
				if(e instanceof Player) {
					RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
					ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
					State flag = set.queryValue(null, Flags.PVP);
					if(flag != null && flag.equals(State.ALLOW)
							&& !(e.getWorld().getName().toLowerCase().contains("dungeon") || e.getWorld().getName().toLowerCase().contains("raid")))
						return true;
					return false;
				}
				if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
						&& e.getType().equals(EntityType.ARMOR_STAND))
					return false;
				if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
					return false;
				return true;
			}).forEach(e -> {
				if(RuneDamage.damageNormal(p, (LivingEntity) e, dr, dr.getDamage() * explosionModifier)) {
					((LivingEntity)e).addPotionEffect(effect);
				}
			});
		}
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*2;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld() || !entityInCastWorld(victim)) {
					this.cancel();
					return;
				}
				if(isCancelled())
					return;
				--timer;
				
				double poisonModifier = 1. + stats.getFinalZdolnosciMysliwskie() / 175. + stats.getFinalZrecznosc() / 250.;
				if(!RuneDamage.damageTiming(p, victim, dr, dr.getDamage() * poisonModifier)) {
					this.cancel();
					return;
				}
				
				Location vLoc = victim.getLocation().clone().add(0,1,0);
				vLoc.getWorld().spawnParticle(Particle.BLOCK, vLoc, 12, 0.5f, 0.9f, 0.5f, 0.06f, chainData);
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
	}

}
