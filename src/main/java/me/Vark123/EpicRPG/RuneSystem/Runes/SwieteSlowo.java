package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

public class SwieteSlowo extends ARune {

	private List<Entity> shooted;
	
	public SwieteSlowo(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		shooted = new ArrayList<>();
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 4, 1.5f);
		new BukkitRunnable() {
			Location loc = p.getLocation();
			Location check = p.getLocation();
			double t = 0.25;
			@Override
			public void run() {
				
				spawnColumnParticle(loc.clone(), Particle.FIREWORKS_SPARK);
				
				double progress = Math.PI/(4*t);
				for(double theta = 0; theta <= (Math.PI*2); theta+=progress) {
					
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					
					loc.add(x, 0, z);
					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
						this.cancel();
						return;
					}
					loc.add(0, 0.75, 0);
					
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 3, 0.1F, 0.1F, 0.1F, 0.05F);
					
					loc.subtract(x, 0.75, z);
					
				}
				
				loc.getWorld().getNearbyEntities(loc, t, t, t, e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
						return false;
					if(e.getLocation().distance(loc) > t)
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
					RuneDamage.damageNormal(p, (LivingEntity) e, dr);
					e.getWorld().playSound(e.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1.6f);
					shooted.add(e);
				});
				
				t += 0.25;
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

	private void spawnColumnParticle(Location loc, Particle p) {
		for(double i = 0; i<40; i+=0.5) {
			loc.add(0, i, 0);
			
			loc.getWorld().spawnParticle(p, loc, 3, 0.25F, 0.25F, 0.25F, 0.01F);
			
			loc.subtract(0, i, 0);
		}
	}
	
}
