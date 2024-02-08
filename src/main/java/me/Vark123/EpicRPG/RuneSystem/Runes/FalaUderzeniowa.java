package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
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

public class FalaUderzeniowa extends ARune {
	
	private List<Entity> shooted;

	public FalaUderzeniowa(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		shooted = new ArrayList<>();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_BIG_FALL, 2, 0.6f);
		new BukkitRunnable() {
			BlockData data = Bukkit.createBlockData(Material.DIRT);
			Location loc = p.getLocation();
			double r = 0;
			@Override
			public void run() {
				if(r > dr.getObszar() || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				r += 0.17;
				
				for(double theta = 0; theta <=(2*Math.PI); theta = theta + (Math.PI/(r*2))) {
					double x = r*Math.sin(theta);
					double z = r*Math.cos(theta);
					Location tmp = new Location(loc.getWorld(), loc.getX()+x, loc.getY(), loc.getZ()+z);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
					tmp.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 2, 0.2F, 0.2F, 0.2F, 0.05F, data);
				}
				
				loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar()).stream().filter(e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
						return false;
					if(e.getLocation().distance(loc) > r)
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
					if(RuneDamage.damageNormal(p, (LivingEntity) e, dr)) {
						Location eLoc = e.getLocation();
						Vector tmp = new Vector(eLoc.getX() - loc.getX(),
								0,
								eLoc.getZ() - loc.getZ()).normalize().setY(1).multiply(3);
						e.setVelocity(tmp);
					}
					e.getLocation().getWorld().playSound(e.getLocation(), Sound.ENTITY_GENERIC_SMALL_FALL, 1, 0.8f);
				});
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
