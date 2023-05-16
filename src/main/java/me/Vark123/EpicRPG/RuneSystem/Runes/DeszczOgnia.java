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
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;

public class DeszczOgnia extends ARune{

	List<Entity> entitiesList = new ArrayList<>();
	
	public DeszczOgnia(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier2 = true;
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 10, 1);
		new BukkitRunnable() {
			double t = 0;
			LivingEntity le;
			@Override
			public void run() {
				Location loc = p.getLocation();
				t++;
				double x = loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar();
				double z = loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar();
				Location target = new Location(loc.getWorld(), x, loc.getY(), z);
				spellEffect(target.clone());
				x = loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar();
				z = loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar();
				target = new Location(loc.getWorld(), x, loc.getY(), z);
				spellEffect(target.clone());
				x = loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar();
				z = loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar();
				target = new Location(loc.getWorld(), x, loc.getY(), z);
				spellEffect(target.clone());
				
				Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc,dr.getObszar(), dr.getObszar(), dr.getObszar());
				
				tmpList.parallelStream().filter(e -> {
					if(entitiesList.contains(e))
						return false;
					entitiesList.add(e);
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
					le = (LivingEntity) e;
					RuneDamage.damageNormal(p, le, dr, (p, le, dr) -> {
						new BukkitRunnable() {
							int timer = 20;
							double dmg = dr.getDamage()/50.0;
							@Override
							public void run() {
								if(timer <= 0 || !casterInCastWorld() || !entityInCastWorld(e)) {
									this.cancel();
									return;
								}
								--timer;
								boolean end = RuneDamage.damageTiming(p, le, dr, dmg);
								if(!end) {
									this.cancel();
									return;
								}
								Location loc = le.getLocation().add(0,1,0);
								p.getWorld().spawnParticle(Particle.FLAME, loc,10,0.2,0.2,0.2,0.05);
								p.getWorld().playSound(loc, Sound.ENTITY_GENERIC_BURN, 1, 1);
							}
						}.runTaskTimer(Main.getInstance(), 0, 20);
					});
				});
				
				if(t>30 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
		
		RuneManager.getInstance().getObszarowkiCd().put(p, new Date());
		
	}
	
	private void spellEffect(Location target) {
		target.add(0, 20, 0);
		new BukkitRunnable() {
			double t = 20;
			@Override
			public void run() {
				target.add(0, t, 0);
				p.getWorld().spawnParticle(Particle.FLAME, target, 10, 0.2F, 0.2F, 0.2F, 0.05F);

				target.subtract(0, t, 0);
				t--;
				if(t<(-50) || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
