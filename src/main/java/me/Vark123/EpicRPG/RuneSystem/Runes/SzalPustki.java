package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class SzalPustki extends ARune {

	public SzalPustki(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation().add(0, 2, 0);
		loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_SCREAM, 1, 0.3F);
		new BukkitRunnable() {
			int timer1 = dr.getDurationTime()*4;
			@Override
			public void run() {
				
				if(timer1 <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc,10,0.75F,0.75F,0.75F,0.05F);
				
				--timer1;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
		new BukkitRunnable() {
			int timer2 = dr.getDurationTime();
			@Override
			public void run() {
				
				if(timer2 <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				
				entities.stream().filter(e -> {
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
					if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
							&& e.getType().equals(EntityType.ARMOR_STAND))
						return false;
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).forEach(e -> {
					createLine(loc, e.getLocation().add(0,1,0));
					loc.getWorld().playSound(e.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 3, 0.2F);
					RuneDamage.damageNormal(p, (LivingEntity) e, dr);
				});
				
				--timer2;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
	
	private void createLine(Location loc1, Location loc2) {
		double space = 0.25;
		Vector p1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		double distance = loc1.distance(loc2);
		Vector vec = p2.clone().subtract(p1).normalize().multiply(space);
		for(double length = 0; length < distance; p1.add(vec), length += space) {
			loc1.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p1.getX(), p1.getY(), p1.getZ() , 3,0.1F,0.1F,0.1F,0.01F);
		}
	}

}
