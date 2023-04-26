package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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

public class Zamiec extends ARune {
	
//	List<Entity> entitiesList = new ArrayList<>();

	public Zamiec(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier2 = true;
	}

	@Override
	public void castSpell() {
		
		//DZWIEK
		new BukkitRunnable() {
			
			int timer = dr.getDurationTime()/4;
			int t = 0;
			
			@Override
			public void run() {
				if(t > timer || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_MINECART_INSIDE, .6F, 1.35F);
			}
		}.runTaskTimer(Main.getInstance(), 0, 20*4);
		
		new BukkitRunnable() {
			double t = 0;
			double timer = dr.getDurationTime();
			@Override
			public void run() {
				if(t > timer || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				Location loc = p.getLocation();
				Location particle = loc.clone();
				for(int i = 0; i < 12*dr.getObszar(); ++i) {
					randomParticle(particle);
				}
				
				Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
//				Bukkit.broadcastMessage(dr.getObszar()+" "+tmpList.size());
				for(Entity en : tmpList) {
//					if(entitiesList.contains(en)) 
//						continue;
//					entitiesList.add(en);
					if(!en.equals(p) && en instanceof LivingEntity) {
						if(en instanceof Player || en.hasMetadata("NPC")) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(en.getLocation()));
							if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
								continue;
						}
						loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 0.75F, .5F);
						RuneDamage.damageNormal(p, (LivingEntity) en, dr);
					}
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);

		RunesTimerCheck.getObszarowkiCd().put(p.getUniqueId(), new Date());
	}
	
	private void randomParticle(Location loc) {
		double theta1 = Math.random() * Math.PI * 2;
		double theta2 = Math.random() * Math.PI * 2;
		double x = dr.getObszar() * Math.sin(theta1);
		double y = Math.random()*4+-2;
		double z = dr.getObszar() * Math.cos(theta2);
		Vector v = Vector.getRandom().normalize();
		loc.add(x, y, z);
		loc.getWorld().spawnParticle(Particle.CLOUD, loc, 0, v.getX(), v.getY(), v.getZ());
	}

}
