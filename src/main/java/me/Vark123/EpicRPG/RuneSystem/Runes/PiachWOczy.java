package me.Vark123.EpicRPG.RuneSystem.Runes;

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
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class PiachWOczy extends ARune{

	public PiachWOczy(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			public void run() {
				t+=0.25;
				double x = vec.getX()*t;
				double y = vec.getY()*t+0.1;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.CRIT, loc, 1, 0, 0, 0, 0);
				p.getWorld().spawnParticle(Particle.CRIT, loc, 10, 0.75F,0.75F,0.75F,0.4F);
				loc.getWorld().playSound(loc, Sound.BLOCK_SAND_STEP, 1, 1);
				for(Entity e:loc.getChunk().getEntities()) {
					if(e.getLocation().distance(loc) < 1.0 || e.getLocation().add(0, 1, 0).distance(loc) < 1.0) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
							p.getWorld().spawnParticle(Particle.CRIT, e.getLocation().add(0,1,0),20,0.5F,0.5F,0.5F,0);
							loc.getWorld().playSound(loc, Sound.BLOCK_SAND_STEP, 10, 1);
							PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 3);
							PotionEffect pe2 = new PotionEffect(PotionEffectType.BLINDNESS, 20*dr.getDurationTime(), 3);
							((LivingEntity)e).addPotionEffect(pe);
							((LivingEntity)e).addPotionEffect(pe2);
							this.cancel();
							return;
						}
					}
				}
				loc.subtract(x, y, z);
				if(t>10 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
