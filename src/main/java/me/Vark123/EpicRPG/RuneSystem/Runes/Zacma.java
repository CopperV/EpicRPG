package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;

public class Zacma extends ARune {

	private List<Entity> shooted;
	
	public Zacma(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		shooted = new ArrayList<>();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CREEPER_HURT, 3, 0.5f);
		new BukkitRunnable() {
			
			Location loc = p.getLocation();
			Location check = p.getLocation();
			double t = 0.5;
			
			@Override
			public void run() {
				
				double progress = Math.PI/(2*t);
				
				for(double theta = 0; theta <= (Math.PI*2); theta+=progress) {
					
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x,0,z);
					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
						this.cancel();
						return;
					}
					loc.add(0, 0.75, 0);
					
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 5, 0.1F, 0.1F, 0.1F, 0.05F, Bukkit.createBlockData(Material.DIRT));
					
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
							return false;
					}
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).forEach(e -> {
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 2));
					((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*dr.getDurationTime(), 2));
					e.getWorld().playSound(e.getLocation(), Sound.ENTITY_CREEPER_DEATH, 1, 0.3f);
					shooted.add(e);
				});
				
				t+=0.5;
					
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

}
