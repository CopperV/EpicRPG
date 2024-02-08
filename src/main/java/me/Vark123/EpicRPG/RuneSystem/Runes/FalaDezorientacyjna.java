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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

public class FalaDezorientacyjna extends ARune {
	
	private List<Entity> shooted = new ArrayList<>();

	public FalaDezorientacyjna(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1.5f, 0.8f);
		
		new BukkitRunnable() {
			Location check = p.getLocation();
			double t = 0.5;
			@Override
			public void run() {
				if(t > dr.getObszar() || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				double progress = Math.PI/(2*t);
				for(double theta = 0; theta <= (Math.PI * 2); theta += progress) {
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					Location loc = check.clone()
							.add(x, 0.75, z);
					p.getWorld().spawnParticle(Particle.SNEEZE, loc, 5, 0.1F, 0.1F, 0.1F, 0.05F);
				}
				
				check.getWorld().getNearbyEntities(check, t, t, t, e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
						return false;
					if(e.getLocation().distance(check) > t)
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
					shooted.add(e);
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 1));
					e.getWorld().playSound(e.getLocation(), Sound.ENTITY_HUSK_CONVERTED_TO_ZOMBIE, 1, 0.6f);
				});
				
				t += 0.5;
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
		
	}

}
