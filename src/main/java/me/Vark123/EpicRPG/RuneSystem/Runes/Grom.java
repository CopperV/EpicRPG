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

public class Grom extends ARune {
	
	List<Entity> entitiesList = new ArrayList<>();

	public Grom(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier2 = true;
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, 1);
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
				
				Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				
				tmpList.stream().filter(e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(entitiesList.contains(e))
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
					entitiesList.add(e);
					le = (LivingEntity) e;
					RuneDamage.damageNormal(p, le, dr, (p,le,dr)->{
						PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, 20*25, 1);
						le.addPotionEffect(pe);
					});
				});
				
				if(t>40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);

		RuneManager.getInstance().getObszarowkiCd().put(p, new Date());
		
	}

	private void spellEffect(Location target) {
		target.add(0, 20, 0);
//		List<Entity> tempList = new ArrayList<>();
		new BukkitRunnable() {
			double t = 20;
			@Override
			public void run() {
				double y = t;
				target.add(0, y, 0);
				p.getWorld().spawnParticle(Particle.SNOWBALL, target, 5, 0.3F, 0.3F, 0.3F, 0.01F);
				p.getWorld().spawnParticle(Particle.CLOUD, target, 5, 0.3F, 0.3F, 0.3F, 0.01F);
				if(t<(-50) || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				target.subtract(0, y, 0);
				t--;
				y = t;
				target.add(0, y, 0);
				p.getWorld().spawnParticle(Particle.SNOWBALL, target, 5, 0.3F, 0.3F, 0.3F, 0.01F);
				p.getWorld().spawnParticle(Particle.CLOUD, target, 5, 0.3F, 0.3F, 0.3F, 0.01F);
				if(t<(-50) || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				target.subtract(0, y, 0);
				t--;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
}
