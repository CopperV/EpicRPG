package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
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

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class WulkanicznyGejzer extends ARune {

	private static final Random rand = new Random();
	private static final Vector horizontalVector = new Vector(0, 1, 0).multiply(0.6);

	public WulkanicznyGejzer(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation().add(0, 1, 0);
		p.getWorld().playSound(loc, Sound.BLOCK_LAVA_EXTINGUISH, 1.5f, 0.7f);
		p.getWorld().spawnParticle(Particle.LAVA, loc, 12, .4f, .8f, .4f, .1f);

		double radius = dr.getObszar();
		List<Location> targets = loc.getWorld().getNearbyEntities(loc, radius, radius, radius, e -> {
			if (e.getLocation().distanceSquared(loc) > (radius * radius))
				return false;
			if (e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if (e instanceof Player) {
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
				State flag = set.queryValue(null, Flags.PVP);
				if (flag != null && flag.equals(State.ALLOW)
						&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
					return true;
				return false;
			}
			if (!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
				return false;
			return true;
		}).stream()
				.map(e -> e.getLocation().add(0, 0.1, 0))
				.collect(Collectors.toList());
		
		targets.forEach(this::spellPrepareEffect);
	}
	
	private void spellPrepareEffect(Location loc) {
		new BukkitRunnable() {
			int timer = 5*3;
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!casterInCastWorld()) {
					cancel();
					return;
				}
				if(timer <= 0) {
					spellEffect(loc);
					cancel();
					return;
				}
				
				loc.getWorld().spawnParticle(Particle.LAVA, loc, 8, .25f, .25f, .25f, .15f);
				if(rand.nextDouble() < 0.15)
					loc.getWorld().playSound(loc, Sound.BLOCK_LAVA_POP, rand.nextFloat(.5f) + .75f, rand.nextFloat(.5f) + .75f);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 4);
	}

	private void spellEffect(Location loc) {
		loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_DEATH, 2.5f, .7f);
		
		loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
			AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
			AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-0.5, loc.getZ()-1, loc.getX()+1, loc.getY()+3, loc.getZ()+1);
			if(!aabb.c(aabb2))
				return false;
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
			RuneDamage.damageNormal(p, (LivingEntity)e, dr, (p, le, dr) -> {
				new BukkitRunnable() {
					int timer = 11;
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
		
		final Location startLoc = loc.clone();
		new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(loc.distanceSquared(startLoc) > 7*7) {
					cancel();
					return;
				}
				if(!casterInCastWorld()) {
					cancel();
					return;
				}

				loc.getWorld().spawnParticle(Particle.FALLING_LAVA, loc, 12, .8f, .25f, .8f, .1f);
				loc.add(horizontalVector);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
		
	}

}
