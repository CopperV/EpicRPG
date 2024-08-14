package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

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

public class KrwawyBicz extends ARune {

	private static final Random rand = new Random();
	private static final DustOptions lineDust = new DustOptions(Color.fromRGB(138, 3, 3), 0.5f);
	private static final DustOptions cloudDust = new DustOptions(Color.fromRGB(138, 3, 3), 2.25f);
	
	public KrwawyBicz(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CAT_HISS, 1, 0.7f);
		//WHIP EFFECT
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*2;
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(timer <= 0 || !casterInCastWorld()) {
					cancel();
					return;
				}
				--timer;
				
				p.getWorld().getNearbyEntities(p.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar(), e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(e instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(e.getLocation()));
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
				}).stream().min((e1, e2) -> Double.compare(
						e1.getLocation().distanceSquared(p.getLocation()), 
						e2.getLocation().distanceSquared(p.getLocation())))
				.ifPresent(e -> {
					RuneDamage.damageNormal(p, (LivingEntity) e, dr, (_p, _e, _dr) -> {
						_p.playSound(_e.getLocation(), Sound.ENTITY_CREEPER_HURT, 1.2f, .7f);
						createLine(p.getLocation().clone().add(0, 2.5, 0), e.getLocation().clone().add(0,1,0));
						
						Location loc1 = _e.getLocation();
						Location loc2 = _p.getLocation();
						Vector vec = new Vector(loc1.getX() - loc2.getX(), loc1.getY() - loc2.getY(), loc1.getZ() - loc2.getZ())
								.normalize()
								.multiply(0.8)
								.setY(0.2);
						_e.setVelocity(vec);
					});
				});
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
		//PARTICLE EFFECT
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(timer <= 0 || !casterInCastWorld()) {
					cancel();
					return;
				}
				--timer;

				Location loc = p.getLocation().add(0,3,0);
				for(int i = 0; i < 6; ++i) {
					double x = rand.nextDouble(1.5) - 0.75;
					double y = rand.nextDouble(0.8) - 0.4;
					double z = rand.nextDouble(1.5) - 0.75;
					float speed = rand.nextFloat(0.25f) + 0.1f;
					Location tmp = loc.clone().add(x,y,z);
					p.getWorld().spawnParticle(Particle.REDSTONE, tmp, 0, 0, 1, 0, speed, cloudDust);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}
	
	private void createLine(Location loc1, Location loc2) {
		double space = 0.25;
		Vector p1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		double distance = loc1.distance(loc2);
		Vector vec = p2.clone().subtract(p1).normalize().multiply(space);
		for(double length = 0; length < distance; p1.add(vec), length += space) {
			loc1.getWorld().spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ() , 3,0.1F,0.1F,0.1F,0.01F, lineDust);
		}
	}

}