package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
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

public class Rozerwanie extends ARune {

	private static final double MAX_DISTANCE = 27;
	private static final double MIN = 0.5;
	private static final double MAX = 0.1;
	
	public Rozerwanie(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1, .7f);
		new BukkitRunnable() {
			Location startLoc = p.getLocation().add(0,1.15,0);
			Location loc = startLoc.clone();
			Vector vec = loc.getDirection().normalize().multiply(1.15);
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(startLoc.distanceSquared(loc) > (MAX_DISTANCE*MAX_DISTANCE)) {
					cancel();
					return;
				}
				if(!casterInCastWorld()) {
					cancel();
					return;
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				
				p.getWorld().spawnParticle(Particle.FLAME, loc, 4, .25f, .25f, .25f, .05f);
				p.getWorld().spawnParticle(Particle.FALLING_LAVA, loc, 7, .4f, .4f, .4f, .1f);
				
				loc.getWorld().getNearbyEntities(loc, 4, 4, 4, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1.5, loc.getZ()-1, loc.getX()+1, loc.getY()+1.5, loc.getZ()+1);
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
				}).stream().min((e1, e2) -> {
					double dist1 = e1.getLocation().distanceSquared(loc);
					double dist2 = e2.getLocation().distanceSquared(loc);
					if(dist1 == dist2)
						return 0;
					return dist1 < dist2 ? -1 : 1;
				}).ifPresent(e -> {
					spellEffect(e);
					cancel();
				});
				
				loc.add(vec);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void spellEffect(Entity e) {
		new BukkitRunnable() {
			int timer = 4;
			float pitchMod = (float) (0.5 / (timer - 1.));
			float pitch = 1f;
			@Override
			public void run() {
				if(timer <= 0) {
					double radius = dr.getObszar();
					Location loc = e.getLocation().add(0, 0.1, 0);
					loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_BREAK_BLOCK, 3, 0.7f);
					loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 20, radius, 0, radius);
					
					RuneDamage.damageNormal(p, (LivingEntity) e, dr);
					loc.getWorld().getNearbyEntities(loc, radius, radius, radius, en -> {
						if(en.equals(e))
							return false;
						if(en.getLocation().distanceSquared(loc) > (radius * radius))
							return false;
						if(en.equals(p) || !(en instanceof LivingEntity))
							return false;
						if(en instanceof Player) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
							State flag = set.queryValue(null, Flags.PVP);
							if(flag != null && flag.equals(State.ALLOW)
									&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
								return true;
							return false;
						}
						if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(en).isDamageable())
							return false;
						return true;
					}).forEach(en -> {
						double dist = en.getLocation().distance(loc);
						double closePercent = dist / (double) dr.getObszar();
						
						double percent = MAX - (MAX-MIN) * closePercent;
						double dmg = dr.getDamage() * percent;
						RuneDamage.damageNormal(p, (LivingEntity) en, dr, dmg);
					});
					
					cancel();
					return;
				}
				
				Location loc = e.getLocation().add(0,1,0);
				loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, pitch);
				loc.getWorld().spawnParticle(Particle.FLAME, loc, 16, .4f, .4f, .4f, .12f);
				
				--timer;
				pitch += pitchMod;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

}
