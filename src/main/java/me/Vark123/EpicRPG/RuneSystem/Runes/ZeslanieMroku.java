package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARunesTimerCheck;

public class ZeslanieMroku extends ARune {

	List<Entity> entitiesList = new ArrayList<>();
	
	public ZeslanieMroku(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier2 = true;
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1, 0.2F);
		new BukkitRunnable() {
			
			double t = 0;
			
			@Override
			public void run() {
				if(t>30 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				
				Location loc = p.getLocation();
				Location l1 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar(), 
						loc.getY(), 
						loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar());
				Location l2 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar(), 
						loc.getY(), 
						loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar());
				Location l3 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar(), 
						loc.getY(), 
						loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar());
				Location l4 = new Location(loc.getWorld(), 
						loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar(), 
						loc.getY(), 
						loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar());
				spellEffect(l1);
				spellEffect(l2);
				spellEffect(l3);
				spellEffect(l4);
				
				Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc,dr.getObszar(), dr.getObszar(), dr.getObszar());
				for(Entity en : tmpList) {
					if(entitiesList.contains(en)) continue;
					entitiesList.add(en);
					if(!en.equals(p) && en instanceof LivingEntity) {
						if(en instanceof Player || en.hasMetadata("NPC")) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(en.getLocation()));
							if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
								continue;
						}
						RuneDamage.damageNormal(p, (LivingEntity)en, dr);
						((LivingEntity)en).addPotionEffect(new PotionEffect(
								PotionEffectType.SLOW, 15*20, 1));
						((LivingEntity)en).addPotionEffect(new PotionEffect(
								PotionEffectType.BLINDNESS, 15*20, 1));
						
					}
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
		

		RunesTimerCheck.getObszarowkiCd().put(p.getUniqueId(), new Date());
	}
	
	private void spellEffect(Location loc) {
		Location loc2 = new Location(loc.getWorld(), 
				loc.getX()+Math.random()*2*8-8, 
				loc.getY()+35, 
				loc.getZ()+Math.random()*2*8-8);
		Vector p1 = new Vector(loc.getX(), loc.getY(), loc.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		Vector vec = p1.clone().subtract(p2).normalize();
//		Bukkit.broadcastMessage(vec.toString());
		new BukkitRunnable() {
			
			Location loc3 = loc2.clone();
			double distance = loc2.distance(loc);
			
			@Override
			public void run() {
				
//				p.sendMessage(distance+" : "+loc3.distance(loc2));
				
				if(loc3.distance(loc2) > distance || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc3, 2, 0.2, 0.4, 0.2, 0.05);
				
				loc3.add(vec.getX(), vec.getY(), vec.getZ());
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
