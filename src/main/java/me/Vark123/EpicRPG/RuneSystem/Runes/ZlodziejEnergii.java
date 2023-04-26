package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Healing.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;

public class ZlodziejEnergii extends ARune {

	public ZlodziejEnergii(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation().clone().add(0,0.2d,0);
		loc.getWorld().playSound(loc, Sound.ENTITY_CAT_HISS, 1, 0.3f);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime();
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}

				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				List<Player> players = new ArrayList<>();
				List<Entity> entitiesList = new ArrayList<>();
				
				for(Entity e : entities) {
					if(e instanceof Player) {
						Player tmp = (Player) e;
						if(tmp.equals(p)) {
							players.add(p);
							continue;
						}
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(tmp.getLocation()));
						if(set.queryValue(null, Flags.PVP).equals(StateFlag.State.ALLOW)) {
							continue;
						}
						players.add(tmp);
						continue;	
					}
					if(!(e instanceof LivingEntity)) continue;
					entitiesList.add(e);
					continue;
				}
				
				if(!entitiesList.isEmpty() && !players.isEmpty()) {
					for(Entity e : entitiesList) {
						e.getWorld().spawnParticle(Particle.SOUL, e.getLocation().add(0, 1, 0), 10, 0.3f, 0.3f, 0.3f, 0.1f);
						RuneDamage.damageNormal(p, (LivingEntity) e, dr);
					}
					for(Player tmp : players) {
						
						RpgPlayer rpg = Main.getListaRPG().get(tmp.getUniqueId().toString());
						double amount = tmp.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.02;
						
						RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, amount);
						Bukkit.getPluginManager().callEvent(event);
						
//						if(tmp.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() > ()) {
//							tmp.setHealth((int)(tmp.getHealth()+tmp.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.02));
//						}else {
//							tmp.setHealth(tmp.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
//						}
						
						tmp.getWorld().spawnParticle(Particle.HEART, tmp.getLocation().add(0,1,0),10,0.3F,0.3F,0.3F,0.1F);
					}
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
								
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/(dr.getObszar()*2))) {
					double x = dr.getObszar()*Math.sin(theta);
					double z = dr.getObszar()*Math.cos(theta);
					Location tmp = new Location(loc.getWorld(), loc.getX()+x, loc.getY(), loc.getZ()+z);
					p.getWorld().spawnParticle(Particle.SOUL, tmp, 3, 0.15F, 0.15F, 0.15F, 0.1F);
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
