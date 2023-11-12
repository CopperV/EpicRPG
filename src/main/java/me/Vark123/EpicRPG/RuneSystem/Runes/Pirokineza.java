package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
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
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import net.minecraft.world.phys.AxisAlignedBB;

public class Pirokineza extends ARune {

	public Pirokineza(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
		new BukkitRunnable() {
			double t = 1;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			LivingEntity le;
			@Override
			public void run() {
				for(int i = 0; i < 2; ++i) {
					t++;
					double x = vec.getX()*t;
					double y = vec.getY()*t+1.5;
					double z = vec.getZ()*t;
					loc.add(x,y,z);
					p.getWorld().spawnParticle(Particle.FLAME, loc, 10,0.3F,0.3F,0.3F,0);
					p.getWorld().spawnParticle(Particle.FLAME, loc, 10,0.1F,0.1F,0.1F,0);

					loc.getWorld().getNearbyEntities(loc, 4, 4, 4, e -> {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1, loc.getZ()-1, loc.getX()+1, loc.getY()+1, loc.getZ()+1);
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
						le = (LivingEntity) e;
						if(RuneDamage.damageNormal(p, le, dr, (p, le, dr)->{
							le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 10));
							new BukkitRunnable() {
								@Override
								public void run() {
									new BukkitRunnable() {
										int t = 1;
										@Override
										public void run() {
											if(t>=dr.getDurationTime() || !casterInCastWorld()) {
												this.cancel();
												return;
											}
											p.getWorld().spawnParticle(Particle.FLAME, e.getLocation().add(0, 1, 0),10,0.3F,0.3F,0.3F,0);
											if(!RuneDamage.damageTiming(p, le, dr)) {
												this.cancel();
												return;
											};
											t++;
										}
									}.runTaskTimer(Main.getInstance(), 0, 20);
								}
							}.runTaskLater(Main.getInstance(), 20);
						})) {
							e.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
						}
						cancel();
					});
					
					if(this.isCancelled()) {
						return;
					}
					
					if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
						this.cancel();
						return;
					}
					loc.subtract(x, y, z);
					if(t>30 || !casterInCastWorld()) {
						this.cancel();
						return;
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
