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
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARunesTimerCheck;

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
						le = (LivingEntity) en;
						RuneDamage.damageNormal(p, le, dr, (p,le,dr)->{
							PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, 20*25, 1);
							le.addPotionEffect(pe);
						});
					}
				}
				
				if(t>40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);

		RunesTimerCheck.getObszarowkiCd().put(p.getUniqueId(), new Date());
		
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
